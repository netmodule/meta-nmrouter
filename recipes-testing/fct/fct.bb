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
    libpython3 \
    "

PV = "git${SRCPV}"

PR = "r1"

SRCREV ?= "${AUTOREV}"
SRC_URI = "git://git.netmodule.intranet/nmrouter/fct.git;protocol=https"

S = "${WORKDIR}/git"

FILES_${PN} += "{bindir} {datadir}"

do_install_append() {
    install -d ${D}${bindir}
    install -m 0755 fct.sh ${D}${bindir}/

    install -d ${D}${datadir}/fct
    install -m 0755 fct_init.py ${D}${datadir}/fct/
    install -m 0755 logging.conf ${D}${datadir}/fct/
}

inherit distutils3

