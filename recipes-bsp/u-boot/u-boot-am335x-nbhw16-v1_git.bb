# Force machine configuration for this recipe
UBOOT_MACHINE = "am335x_nbhw16_defconfig"

require u-boot-nm.inc

SRC_URI = "git://github.com/netmodule/u-boot.git;protocol=https;branch=nbhw16-2016.04"

SRCREV ?= "a6f157c6dbd048b452c7e2640215c55be658780c"
PV = "v2016.04+git${SRCPV}"

SPL_BINARY = "MLO"
UBOOT_SUFFIX = "img"

do_deploy_append() {
    rm -f ${DEPLOYDIR}/${PN}-spl.bin
    rm -f ${DEPLOYDIR}/${PN}.bin
    rm -f ${DEPLOYDIR}/${PN}.img
    rm -f ${DEPLOYDIR}/MLO-${PN}
    cp ${S}/spl/u-boot-spl.bin ${DEPLOYDIR}/${PN}-spl.bin
    cp ${S}/u-boot.bin ${DEPLOYDIR}/${PN}.bin
    cp ${S}/u-boot.img ${DEPLOYDIR}/${PN}.img
    cp ${S}/MLO ${DEPLOYDIR}/MLO-${PN}
}

