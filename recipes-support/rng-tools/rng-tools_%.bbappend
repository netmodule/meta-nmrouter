inherit systemd

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = " file://rngd.service"

SYSTEMD_SERVICE_${PN} = "rngd.service"

do_install_append() {
    install -d ${D}/lib/systemd/system
    install -m 644 ${WORKDIR}/rngd.service ${D}/lib/systemd/system/
}
