require u-boot.inc

DEPENDS += "dtc-native"

SRCREV ??= "master"

SRC_URI = "git://gitolite@git/usr/se/u-boot;protocol=ssh;nobranch=1"

PV = "v2016.04+git${SRCPV}"

SPL_BINARY = "MLO"
UBOOT_SUFFIX = "img"

