SUMMARY = "Python module binding for gatttool"
DESCRIPTION = "A Python module as a binding above the bluez5 gatttool"
LICENSE = "Apachev2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=29ff8f199c0ea20816e61937dcb62677"

SRCREV = "da51d8ecc40cad9d2760399ea1fa36fc48c03d34"
SRC_URI = "git://github.com/peplin/pygatt.git;protocol=https \
           "
PV = "1.0"

S = "${WORKDIR}/git"

DEPENDS = "python bluez5"
# opnessh-misc gets ssh-agent which is needed to execute ssh commands on the target
RDEPENDS_${PN} =  "python bluez5 python-pyserial python-enum34"

inherit setuptools
