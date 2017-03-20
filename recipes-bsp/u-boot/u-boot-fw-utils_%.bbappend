FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://fw_env.config"

do_install_prepend() {
    cp -f ${WORKDIR}/fw_env.config ${S}/tools/env/
}
