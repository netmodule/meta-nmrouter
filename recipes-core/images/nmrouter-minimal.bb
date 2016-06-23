inherit core-image

SUMMARY = "Minimal image for bringup"

IMAGE_FEATURES += " \
                package-management \
                ssh-server-openssh \
                "

IMAGE_INSTALL += " \
                openssh-sftp-server \
                bash \
                ethtool \
                i2c-tools \
                lrzsz \
                devmem2 \
                "

LICENSE = "BSD"

