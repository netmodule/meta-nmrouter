DISTRO_FEATURES_BACKFILL_CONSIDERED = " wifi bluetooth"

inherit image

IMAGE_FEATURES = "debug-tweaks"

KERNELDEPMODDEPEND = ""

SUMMARY = "Minimal image for bringup"

NO_RECOMMENDATIONS = "1"

IMAGE_INSTALL = " \
    packagegroup-core-boot \
    e2fsprogs-mke2fs \
    devmem2 \
    util-linux-agetty \
    systemd-serialgetty \
    networkmanager \
    modemmanager \
    "

LICENSE = "BSD"
