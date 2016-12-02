DESCRIPTION = "LoRa Gateway library"
HOMEPAGE = "https://www.semtech.com/"
PRIORITY = "optional"
SECTION = "console/utils"
# Semtech license is a modified BSD-style license
LICENSE = "SEMTECH"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a2bdef95625509f821ba00460e3ae0eb"
DEPENDS = "libftdi libmpsse libusb1"
INC_PR = "r9"
PR = "${INC_PR}.1"
BRANCH = "1.7.0-mts"
SRCREV = "631dfedf1b535b6c3861d3c3868e02f48a84fb06"

SRC_URI = "git://git.multitech.net/lora_gateway;protocol=git;branch=${BRANCH} \
           file://lora-gateway-debug.patch \
           file://lora-gateway-sync-word.patch \
           file://library.cfg \
          "

S = "${WORKDIR}/git"

CFLAGS += "-Iinc -I. -DLIBFTDI1=1"

do_configure_append() {
    # copy over custom library.cfg
    cp ${WORKDIR}/library.cfg ${S}/libloragw/
}

do_compile() {
    oe_runmake
}

do_install() {
	install -d ${D}${includedir}/lora
	install -d ${D}${libdir}/lora
	install -m 0644 libloragw/libloragw.a ${D}${libdir}/lora
	install -m 0644 libloragw/library.cfg ${D}${libdir}/lora
	install -m 0644 libloragw/inc/* ${D}${includedir}/lora

	install -d ${D}/opt/lora
	install -m 0755 libloragw/test_* ${D}/opt/lora/
	install -m 0755 util_pkt_logger/util_pkt_logger ${D}/opt/lora/
	install -m 0755 util_band_survey/util_band_survey ${D}/opt/lora/
	install -m 0755 util_spi_stress/util_spi_stress ${D}/opt/lora/
	install -m 0755 util_tx_test/util_tx_test ${D}/opt/lora/
}

PACKAGES += "${PN}-utils ${PN}-utils-dbg"

FILES_${PN}-utils = "/opt/lora/* /lib"
FILES_${PN}-utils-dbg = "/opt/lora/.debug"
FILES_${PN}-dev = "${includedir}/lora ${libdir}/lora/library.cfg"
FILES_${PN}-staticdev = "${libdir}/lora/libloragw.a"

# disable this on purpose for dev purposes
do_rm_work() {
	echo "skipping"
}

