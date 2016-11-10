# THIS RECIPE FOR PICOCELL USES PATCHED LORA NETWORK SERVER THAT SUPPORTS
# MTP HARDWARE AND V2 PACKET FORWARDER PROTOCOL
DESCRIPTION = "MultiTech LoRa Network Server"
PRIORITY = "optional"
SECTION = "console/utils"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7ffae4666a986c4ccf45e99e464f8402"
DEPENDS = "jsoncpp libmts mosquitto sqlite3"
RDEPENDS_${PN} += "lora-packet-forwarder logrotate"
PR = "r0"

SRC_URI = "http://multitech.net/downloads/lora-network-server_${TUNE_PKGARCH}_${PV}-mtp.tar.gz \
           file://lora-network-server.init \
           file://lora-network-server.default \
           file://lora-network-server.logrotate.conf \
          "

SRC_URI[md5sum] = "1fdcc48e8d62d4f8896e707f28596bfd"
SRC_URI[sha256sum] = "10ba082ad309b2b4bec0835ecf0741463020d962707942e5bb3e8598181591f1"

# binaries are already stripped, so suppress warning
INSANE_SKIP_${PN} = "already-stripped"

S = "${WORKDIR}"

LORA_DIR = "/opt/lora"

do_compile() {
}

do_install() {
    install -d ${D}${LORA_DIR}
    install -m 0755 lora-network-server ${D}${LORA_DIR}/
    install -m 0644 lora-network-server.conf ${D}${LORA_DIR}/

    install -d ${D}${sysconfdir}/default
    install -m 0644 ${WORKDIR}/lora-network-server.default ${D}${sysconfdir}/default/lora-network-server
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/lora-network-server.init ${D}${sysconfdir}/init.d/lora-network-server
    install -d ${D}${sysconfdir}/logrotate.d
    install -m 0644 ${WORKDIR}/lora-network-server.logrotate.conf ${D}${sysconfdir}/logrotate.d/lora-network-server.conf
}

CONFFILES_${PN} = "${sysconfdir}/default/lora-network-server"
FILES_${PN} += "${LORA_DIR}"
FILES_${PN}-dbg += "${LORA_DIR}/.debug"
