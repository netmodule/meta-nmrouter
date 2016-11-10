DESCRIPTION = "LoRa network server query tool"
HOMEPAGE = "http://www.multitech.net/"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=94d55d512a9ba36caa9b7df079bae19f"
DEPENDS = "jsoncpp libmts"
PR = "r1"

SRCREV = "${PV}"

SRC_URI = "git://git.multitech.net/lora-query;branch=master"

S = "${WORKDIR}/git"

do_compile() {
    oe_runmake
}

do_install() {
    oe_runmake install DESTDIR=${D}
}

