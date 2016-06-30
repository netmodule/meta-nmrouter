FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = " \
        file://force-40mhz.patch;patchdir=${S}/../ \
        "
