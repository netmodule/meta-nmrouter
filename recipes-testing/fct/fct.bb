DESCRIPTION = "Factory test scripts"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://README.md;md5=af8334b1c8608313603321c799cea517"

RDEPENDS_${PN}_append = " \
    python3-logging \
    python3-misc \
    python3-threading \
    python3-multiprocessing \
    python3-subprocess \
    python3-shell \
    python3-compression \
    python3-crypt \
    python3-pkgutil \
    python3-netclient \
    python3-importlib \
    python3-pydoc \
    python3-json\
    libpython3 \
    "

PV = "git${SRCPV}"

PR = "r1"

SRCREV ?= "${AUTOREV}"
SRC_URI = "git://git.netmodule.intranet/nmrouter/fct.git;protocol=https"

S = "${WORKDIR}/git"

FILES_${PN} += "{bindir} {datadir}"

inherit systemd
SYSTEMD_SERVICE_${PN} = "fct.service"

do_install_append() {
    install -d ${D}${bindir}
    install -m 0755 fct.sh ${D}${bindir}/

    install -d ${D}${datadir}/fct
    install -m 0755 fct_init.py ${D}${datadir}/fct/
    install -m 0755 fct_provisioning.py ${D}${datadir}/fct/
    install -m 0755 logging.conf ${D}${datadir}/fct/

    install -d ${D}/${base_libdir}/systemd/system/
    install -m 0644 init/fct.service ${D}/${systemd_unitdir}/system/

}

inherit distutils3

