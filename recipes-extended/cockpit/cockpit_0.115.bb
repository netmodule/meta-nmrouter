SUMMARY = "Display or change ethernet card settings"
DESCRIPTION = "A small utility for examining and tuning the settings of your ethernet-based network interfaces."
HOMEPAGE = "http://www.kernel.org/pub/software/network/ethtool/"
SECTION = "console/network"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

SRCREV = "39be0a69e4b03907a947d957afe20d394179677e"
SRC_URI = "git://github.com/cockpit-project/cockpit.git;protocol=https \
           file://cockpit.pam \
           "

S = "${WORKDIR}/git"

DEPENDS = "node-native json-glib libssh libpam keyutils krb5 systemd polkit glib-networking"
RDEPENDS_${PN} =  "json-glib libssh libpam keyutils krb5 libsystemd polkit bash glib-networking"

EXTRA_AUTORECONF_append = " -I ${STAGING_DATADIR}/aclocal"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"

FILES_${PN}_append = " \
	/lib/* \
	/usr/* \
    /etc/* \
	"

do_configure() {
    bbnote "$(pwd)"
    npm install
    find node_modules -name test | xargs rm -rf
    sed -i "s#autreconf.*#autoreconf -f -i -I tools -I /opt/nmrouter/0.0.1/sysroots/cortexa8hf-vfp-neon-netmodule-linux-gnueabi/usr/share/aclocal/ -Wcross#" ./autogen.sh
    ./autogen.sh --host=cortexa8hf-vfp-neon-netmodule-linux-gnueabi --disable-pcp --disable-doc
}

do_install() {
    sed -i "s#install-data-local:: doc/guide/html/index.html#bla-install-data-local:: doc/guide/html/index.html#" ./Makefile
    oe_runmake DESTDIR=${D} install

    mkdir -p ${D}/etc/pam.d/
    cp ${WORKDIR}/cockpit.pam ${D}/etc/pam.d/cockpit
}
