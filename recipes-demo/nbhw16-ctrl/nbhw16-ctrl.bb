DESCRIPTION = "NBHW16 python script to control the HW"
HOMEPAGE = "http://www.netmodule.com/"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"
RDEPENDS_${PN} = "python-subprocess"

PV = "1.0.0"

SRC_URI = " \
  file://nbhw16-ctrl.py \
  "

do_install () {
    install -D -m 0755 ${WORKDIR}/nbhw16-ctrl.py ${D}/${bindir}/nbhw16-ctrl
}
