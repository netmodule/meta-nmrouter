SUMMARY = "Hydra util"
DESCRIPTION = "Hydra util to verify atsha204"
SECTION = "utils"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"

RDEPENDS_${PN}-util += "${PN}"

SRC_URI = "file://hydra.tar.gz \
           "
inherit module

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

S = "${WORKDIR}/hydra/src"

PACKAGES += "${PN}-util"

FILES_${PN}-util = "/usr/bin"

do_compile_append() {
    cd ../tools_src
    oe_runmake
}

do_install_append() {
    install -d ${D}/usr/bin
    install -m 0775 ${S}/../tools_src/hydra-util ${D}/usr/bin
}
