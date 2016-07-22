SUMMARY = "Start ti bluetooth"
DESCRIPTION = "TI needs a special startup sequence for the wilink module"
AUTHOR = "Stefan Eichenberger"

SECTION = "connectivity"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
PR = "r0"

RDEPENDS_${PN} = "tiuim"

inherit update-rc.d

SRC_URI =  " \
    file://tibluetooth.sh \
    "

S = "${WORKDIR}"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

INITSCRIPT_NAME = "tibluetooth.sh"
INITSCRIPT_PARAMS = "start 99 S . stop 0 0 1 6 ."

do_install () {
	install -d ${D}${sysconfdir}
	install -m 0755 tibluetooth.sh ${D}${sysconfdir}
}
