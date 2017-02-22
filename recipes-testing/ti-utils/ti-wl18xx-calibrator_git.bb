DESCRIPTION = "Configuration utility for TI wireless drivers"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://README;md5=df1ae7fbe1a166f497f944d67efbf088"

# Based on TI meta-arago-extras
# Adding wl1271-nvs.bin from linux-firmware repo

PV = "R8.7+git${SRCPV}"

PR = "r1"

SRCREV = "39542357111d1f24e866c2857d561a348c04cce4"
SRC_URI = "git://git.ti.com/wilink8-wlan/18xx-ti-utils.git"

TARGET_CFLAGS += "\
                -DCONFIG_LIBNL32 \
                -O2 \
                -Wall \
                -I${STAGING_DIR_TARGET}/usr/include/libnl3 \
                 "

EXTRA_OEMAKE += "NLVER=3"

S = "${WORKDIR}/git/"

# Install calibrator
do_install() {
	install -d ${D}${sbindir}

	install -m 0755 calibrator ${D}${sbindir}/
}

