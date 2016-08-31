DISTRO_FEATURES_BACKFILL_CONSIDERED = " wifi bluetooth"

inherit image

SUMMARY = "Minimal image for bringup"

#IMAGE_FEATURES += " \
#                "

#IMAGE_INSTALL += " \
#                lrzsz \
#                devmem2 \
#                "


IMAGE_INSTALL = " \
    packagegroup-core-boot \
    e2fsprogs \
    lrzsz \
    devmem2 \
    "
LICENSE = "BSD"

