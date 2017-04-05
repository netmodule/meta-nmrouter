SUMMARY = "Start ti bluetooth"
DESCRIPTION = "Add ti bluetooth firmware (important non_ST!) and run hciattach"
AUTHOR = "Stefan Eichenberger"

SECTION = "connectivity"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
PR = "r2"

inherit systemd

SRC_URI =  " \
    file://tibluetooth.service\
    file://TIInit_11.8.32.bts \
    "

S = "${WORKDIR}"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

SYSTEMD_SERVICE_${PN} = "tibluetooth.service"
SYSTEMD_AUTO_ENABLE ?= "enable"

FILES_${PN}_append = " \
                    /lib \
                    "

do_install () {
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 tibluetooth.service ${D}${systemd_unitdir}/system/

    install -d ${D}/lib/firmware/ti-connectivity
    install -m 0644 TIInit_11.8.32.bts ${D}/lib/firmware/ti-connectivity/

}
