SUMMARY = "User Mode Init manager for TI shared transport"
DESCRIPTION = "Userpace utility to initialize"
AUTHOR = "Texas Instruments"

SECTION = "connectivity"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
PR = "r0"

SRC_URI =  " \
    file://uim.c \
    file://uim.h \
    "

S = "${WORKDIR}"

do_compile() {
	${CC} uim.c -o uim
}

do_install () {
	install -d ${D}${bindir}
	install -m 0755 uim ${D}${bindir}
}
