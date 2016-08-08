DESCRIPTION = "A kernel module for the amfeltec usb fxs adapter"
HOMEPAGE = "http://www.netmodule.com/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
PROVIDES = "amfeltec-usb"

inherit module
DEPENDS = "dahdi-linux"
RDEPENDS_${PN} = "dahdi-linux"

S = "${WORKDIR}/amfeltec_usb_${PV}"

SRC_URI = " \
    file://amfeltec_usb_${PV}.tgz \
    file://001_makefile.patch \
    "

GLOBAL_CFLAGS = "-I${STAGING_INCDIR} -DDAHDI_VERSION=29"

do_compile_prepend() {
    export GLOBAL_CFLAGS="${GLOBAL_CFLAGS}"

    cat ${STAGING_DIR_TARGET}/usr/share/dahdi-linux/Module.symvers ${STAGING_KERNEL_BUILDDIR}/Module.symvers > ${S}/driver/Module.symvers
}

do_install() {
    oe_runmake INSTALL_MOD_PATH=${D} install
}
