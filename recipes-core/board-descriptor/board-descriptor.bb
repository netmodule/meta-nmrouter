SUMMARY = "Boarddescriptor Tool"
DESCRIPTION = "Read and write from/to the boarddescriptor"
AUTHOR = "Stefan Eichenberger (stefan.eichenberger@netmodule.com)"

SECTION = "core"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
PR = "r0"

RDEPENDS_${PN} = "python-json python-textutils"

SRC_URI =  " \
            file://bd.py \
            file://descriptor.py \
            file://config.json \
            file://bd.json \
           "

S = "${WORKDIR}"

FILES_${PN} = " \
            ${bindir}/* \
            ${libdir}/* \
            ${sysconfdir}/* \
            "

do_install () {
	install -d ${D}${bindir}
	install -m 0755 bd.py ${D}${bindir}/bd

	install -d ${D}${libdir}/python2.7/
	install -m 0644 descriptor.py ${D}${libdir}/python2.7/

	install -d ${D}${sysconfdir}/bd
	install -m 0644 *.json ${D}${sysconfdir}/bd/
}
