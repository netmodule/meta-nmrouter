SRCREV = "40e9ae84308274b91ac069fba2f1a290fcdc83fc"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

PR = "r2"

LIC_FILES_CHKSUM = "\
    file://LICENCE.Abilis;md5=b5ee3f410780e56711ad48eadc22b8bc \
    file://LICENCE.agere;md5=af0133de6b4a9b2522defd5f188afd31 \
    file://LICENCE.atheros_firmware;md5=30a14c7823beedac9fa39c64fdd01a13 \
    file://LICENCE.broadcom_bcm43xx;md5=3160c14df7228891b868060e1951dfbc \
    file://LICENCE.ca0132;md5=209b33e66ee5be0461f13d31da392198 \
    file://LICENCE.chelsio_firmware;md5=819aa8c3fa453f1b258ed8d168a9d903 \
    file://LICENCE.cw1200;md5=f0f770864e7a8444a5c5aa9d12a3a7ed \
    file://LICENCE.ene_firmware;md5=ed67f0f62f8f798130c296720b7d3921 \
    file://LICENCE.fw_sst_0f28;md5=6353931c988ad52818ae733ac61cd293 \
    file://LICENCE.go7007;md5=c0bb9f6aaaba55b0529ee9b30aa66beb \
    file://LICENCE.i2400m;md5=14b901969e23c41881327c0d9e4b7d36 \
    file://LICENCE.ibt_firmware;md5=fdbee1ddfe0fb7ab0b2fcd6b454a366b \
    file://LICENCE.it913x;md5=1fbf727bfb6a949810c4dbfa7e6ce4f8 \
    file://LICENCE.iwlwifi_firmware;md5=3fd842911ea93c29cd32679aa23e1c88 \
    file://LICENCE.IntcSST2;md5=9e7d8bea77612d7cc7d9e9b54b623062 \
    file://LICENCE.Marvell;md5=9ddea1734a4baf3c78d845151f42a37a \
    file://LICENCE.mwl8335;md5=9a6271ee0e644404b2ff3c61fd070983 \
    file://LICENCE.myri10ge_firmware;md5=42e32fb89f6b959ca222e25ac8df8fed \
    file://LICENCE.OLPC;md5=5b917f9d8c061991be4f6f5f108719cd \
    file://LICENCE.phanfw;md5=954dcec0e051f9409812b561ea743bfa \
    file://LICENCE.qla2xxx;md5=f5ce8529ec5c17cb7f911d2721d90e91 \
    file://LICENCE.r8a779x_usb3;md5=4c1671656153025d7076105a5da7e498 \
    file://LICENCE.ralink_a_mediatek_company_firmware;md5=728f1a85fd53fd67fa8d7afb080bc435 \
    file://LICENCE.ralink-firmware.txt;md5=ab2c269277c45476fb449673911a2dfd \
    file://LICENCE.rtlwifi_firmware.txt;md5=00d06cfd3eddd5a2698948ead2ad54a5 \
    file://LICENCE.tda7706-firmware.txt;md5=835997cf5e3c131d0dddd695c7d9103e \
    file://LICENCE.ti-connectivity;md5=186e7a43cf6c274283ad81272ca218ea \
    file://LICENCE.ueagle-atm4-firmware;md5=4ed7ea6b507ccc583b9d594417714118 \
    file://LICENCE.via_vt6656;md5=e4159694cba42d4377a912e78a6e850f \
    file://LICENCE.wl1251;md5=ad3f81922bb9e197014bb187289d3b5b \
    file://LICENCE.xc4000;md5=0ff51d2dc49fce04814c9155081092f0 \
    file://LICENCE.xc5000;md5=1e170c13175323c32c7f4d0998d53f66 \
    file://LICENCE.xc5000c;md5=12b02efa3049db65d524aeb418dd87ca \
    file://LICENSE.amd-ucode;md5=3a0de451253cc1edbf30a3c621effee3 \
    file://LICENSE.dib0700;md5=f7411825c8a555a1a3e5eab9ca773431 \
    file://LICENSE.radeon;md5=6c7f97c6c62bdd9596d0238bb205118c \
    file://LICENCE.siano;md5=602c79ae3f98f1e73d880fd9f940a418 \
"
SRC_URI_append = " \
                 file://ti-connectivity/wl12xx-nvs.bin \
                 file://ti-connectivity/wl18xx-conf.bin \
                 file://ti-connectivity/wl18xx-fw-4.bin \
                 file://am335x-pm-firmware.bin \
                 file://am335x-pm-firmware.elf \
                 "

do_install_append() {
    cp -r ${WORKDIR}/ti-connectivity/* ${D}/lib/firmware/ti-connectivity/
}

