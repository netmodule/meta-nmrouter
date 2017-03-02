# Force machine configuration for this recipe
UBOOT_MACHINE = "am335x_nbhw16_v2_defconfig"

require u-boot-nm.inc

SRC_URI = "git://gitolite@git/usr/se/u-boot;protocol=ssh;branch=nbhw16-2016.04"

SRCREV ?= "${AUTOREV}"
PV = "v2016.04+git${SRCPV}"

SPL_BINARY = "MLO"
UBOOT_SUFFIX = "img"

