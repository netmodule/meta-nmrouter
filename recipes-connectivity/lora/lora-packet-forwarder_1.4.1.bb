DESCRIPTION = "LoRa Packet Forwarder"
HOMEPAGE = "https://github.com/Lora-net"
PRIORITY = "optional"
SECTION = "console/utils"
# Semtech license is a modified BSD-style license
LICENSE = "SEMTECH"
LIC_FILES_CHKSUM = "file://LICENSE;md5=22af7693d7b76ef0fc76161c4be76c45"
DEPENDS = "lora-gateway"
PR = "r10"

# tag v1.4.1
SRCREV = "0011a60759a7d81656a5393e97089daab1ff1a81"

SRC_URI = "git://github.com/Lora-net/packet_forwarder.git;protocol=git \
           file://lora-packet-forwarder-add-no-header-option.patch \
           file://lora-packet-forwarder-set-spi-path.patch \
           file://lora-packet-forwarder-skip-bad-packets.patch \
           file://lora-packet-forwarder-fixb64.patch \
           file://lora-packet-forwarder-mts-enhancements.patch \
           file://lora-packet-forwarder-synch-word.patch \
           file://lora-packet-forwarder-add-queue.patch \
           file://README.md \
"

S = "${WORKDIR}/git"

LORA_DIR = "/opt/lora"

export LGW_PATH = "${STAGING_LIBDIR}/lora"
export LGW_INC = "${STAGING_INCDIR}/lora"

CFLAGS += "-I${LGW_INC} -Iinc -I."

do_compile() {
	oe_runmake
}

do_install() {
	install -d ${D}${LORA_DIR}
	install -m 755 gps_pkt_fwd/gps_pkt_fwd ${D}${LORA_DIR}/
	install -m 755 basic_pkt_fwd/basic_pkt_fwd ${D}${LORA_DIR}/
	install -m 755 beacon_pkt_fwd/beacon_pkt_fwd ${D}${LORA_DIR}/
	install -m 755 util_sink/util_sink ${D}${LORA_DIR}/
	install -m 755 util_ack/util_ack ${D}${LORA_DIR}/
	install -m 755 ${WORKDIR}/README.md  ${D}${LORA_DIR}/


#   skip util_tx_test since it conflicts with one in lora-gateway
#	install -m 755 util_tx_test/util_tx_test ${D}${LORA_DIR}/
}

FILES_${PN} += "${LORA_DIR}"
FILES_${PN}-dbg += "${LORA_DIR}/.debug"

# disable this on purpose for dev purposes
do_rm_work() {
	echo "skipping"
}
