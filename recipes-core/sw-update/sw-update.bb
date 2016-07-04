DESCRIPTION = "Software update script"
HOMEPAGE = "http://www.netmodule.com/"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"
RDEPENDS_${PN} = "e2fsprogs"

PV = "1.0.0"

SRC_URI = " \
  file://sw-update.sh \
  file://sw_update_config"

do_install () {
    install -D -m 0755 ${WORKDIR}/sw-update.sh ${D}/${bindir}/sw-update.sh
    install -D -m 0666 ${WORKDIR}/sw_update_config ${D}/${sysconfdir}/default/sw_update_config
}
