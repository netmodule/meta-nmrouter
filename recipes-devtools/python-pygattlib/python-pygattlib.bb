SUMMARY = "Python module binding for gatttool"
DESCRIPTION = "A Python module as a binding above the bluez5 gatttool"
LICENSE = "Apachev2"
LIC_FILES_CHKSUM = "file://PKG-INFO;md5=04b8c127c48df6de657545a898a82401"

PV = "0.20150805"

SRC_URI = "https://pypi.python.org/packages/be/2f/5b1aecec551b42b59d8b399ad444b5672972efb590ca83d784dbe616a3e1/gattlib-${PV}.tar.gz"
SRC_URI[md5sum] = "f620eca190bb7acd67c7aafecaedb6c2"
SRC_URI[sha256sum] = "7f5fbc1613a0225f2af79683ca907fed40b765369c61cf90d023cbf4a92bdb0c"

S = "${WORKDIR}/gattlib-${PV}"

DEPENDS = "python bluez5 boost"
# opnessh-misc gets ssh-agent which is needed to execute ssh commands on the target
RDEPENDS_${PN} =  "python bluez5 boost"

inherit setuptools
