# Force machine configuration for this recipe
UBOOT_MACHINE = "am335x_nbhw16_defconfig"

require u-boot-nm.inc

SRC_URI = "git://gitolite@git/usr/se/u-boot;protocol=ssh;branch=nbhw16-2016.04"

SRCREV ?= "${AUTOREV}"
PV = "v2016.04+git${SRCPV}"

SPL_BINARY = "MLO"
UBOOT_SUFFIX = "img"

do_deploy_append() {
    cp ${S}/spl/u-boot-spl.bin ${DEPLOYDIR}/${PN}-spl.bin
    cp ${S}/u-boot.bin ${DEPLOYDIR}/${PN}.bin
}

