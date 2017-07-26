SUMMARY = "Some scripts from netmodule"
DESCRIPTION = "This are some helper scripts provided by netmodule"
PR = "r1"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690 \
                    file://${COREBASE}/meta/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI = "file://power-pcie-slot.sh \
           file://reset-pcie-slot.sh \
           file://switch-sim.sh \
          "

S = "${WORKDIR}"

PACKAGES =+ "${PN}-fpga"

do_install () {
	install -d -m 755 ${D}${bindir}/

    install -m 755 *.sh ${D}${bindir}/
}

FILES_${PN}-fpga = " \
                    ${bindir}/reset-pcie-slot.sh \
                    ${bindir}/power-pcie-slot.sh \
                    ${bindir}/switch-sim.sh \
                  "

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
