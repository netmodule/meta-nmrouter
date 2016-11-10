DESCRIPTION = "MultiTech LoRa Network Server"
PRIORITY = "optional"
SECTION = "console/utils"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7ffae4666a986c4ccf45e99e464f8402"
DEPENDS = "jsoncpp libmts mosquitto sqlite3"
RDEPENDS_${PN} += "lora-packet-forwarder logrotate"
PR = "r0"

SRC_URI = "http://multitech.net/downloads/lora-network-server_${TUNE_PKGARCH}_${PV}.tar.gz \
           file://lora-network-server.init \
           file://lora-network-server.default \
           file://lora-network-server.logrotate.conf \
          "

SRC_URI[md5sum] = "8785cbe4a70ee288fa09f00593b1fdb1"
SRC_URI[sha256sum] = "5e6450244f21b4c28e780f36b3d1e7bf502ab25f835a0c21295caac11faec69b"

# binaries are already stripped, so suppress warning
INSANE_SKIP_${PN} = "already-stripped"

S = "${WORKDIR}"

LORA_DIR = "/opt/lora"

do_compile() {
}

inherit update-rc.d

INITSCRIPT_NAME = "lora-network-server"
INITSCRIPT_PARAMS = "defaults 80 30"

do_install() {
    install -d ${D}${LORA_DIR}
    install -m 0755 lora-network-server ${D}${LORA_DIR}/
    install -m 0644 lora-network-server.conf.sample ${D}${LORA_DIR}/lora-network-server.conf.sample

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
