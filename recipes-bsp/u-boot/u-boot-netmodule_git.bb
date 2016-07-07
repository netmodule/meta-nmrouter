require u-boot.inc

DEPENDS += "dtc-native"

SRCREV ?= "${AUTOREV}"
BRANCH ?= "master"

SRC_URI = "git://gitolite@git/usr/se/u-boot;protocol=ssh;branch=${BRANCH}"

PV = "v2016.04+git${SRCPV}"

SPL_BINARY = "MLO"
UBOOT_SUFFIX = "img"

