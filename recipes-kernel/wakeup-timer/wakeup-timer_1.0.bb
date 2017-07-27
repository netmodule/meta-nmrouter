DESCRIPTION = "Driver for wakeup from standby via AM335x dmtimer"
HOMEPAGE = "http://www.netmodule.com/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit module

PV = "1.0.0"
SRCREV ?= "1711171ae3bcc9f804d7973f4068cdd7cade78d0"

SRC_URI = "git://github.com/netmodule/am335x-wakeup-timer.git;protocol=https"

S = "${WORKDIR}/git"
