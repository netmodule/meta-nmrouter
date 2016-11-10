DESCRIPTION = "FTDI Libmpsse Library"
HOMEPAGE = "http://googlecode.com/"
PRIORITY = "optional"
SECTION = "console/utils"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

PR = "r1"

DEPENDS = "libftdi"

SRCREV = "cae99f00927f795b0e8f242d9881b1b88b461b2e"
SRC_URI = "git://github.com/devttys0/libmpsse.git;protocol=https \
          "

S = "${WORKDIR}/git/src"

inherit autotools

B = "${S}"

EXTRA_OECONF += "--disable-python"
CFLAGS += "-DLIBFTDI1=1"

do_install_append() {
	mv ${D}${libdir}/libmpsse.so ${D}${libdir}/libmpsse.so.1
	( cd ${D}${libdir}; ln -s libmpsse.so.1 libmpsse.so )
}
