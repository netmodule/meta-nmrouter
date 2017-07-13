inherit systemd

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = " \
                file://node-red.service \
                "

SYSTEMD_SERVICE_${PN} = "node-red.service"

do_install_append () {
    install -d ${D}/lib/systemd/system
    install -m 644  ${WORKDIR}/node-red.service ${D}/lib/systemd/system
}

