inherit core-image

SUMMARY = "nmrouter image"

KERNEL_IMAGETYPE = "uImage"

IMAGE_FSTYPES_append = " tar.gz "

IMAGE_FEATURES_append = " \
                package-management \
                ssh-server-openssh \
                "

PREFERED_VERSION-pn_nodejs = "4.4.5"

FIRMWARE ?= "linux-firmware"
FIRMWARE_am335x-nbhw16 = " \
                linux-firmware-ath6k \
                linux-firmware-ath9k \
                linux-firmware-wl12xx\
                linux-firmware-wl18xx\
                "

IMAGE_INSTALL_append = " \
                hostapd \
                iw \
                crda \
                wpa-supplicant \
                openssh-sftp-server \
                bash \
                iproute2 \
                ethtool \
                devmem2 \
                openvpn \
                iptables \
                pciutils \
                kernel-modules \
                kernel-devicetree \
                python-subprocess \
                networkmanager \
                cockpit \
                board-descriptor \
                sw-update \
                ${FIRMWARE} \
                "

IMAGE_INSTALL_cortex9hf-neon_append = " kernel-devicetree "
IMAGE_INSTALL_cortex9hf_append = " kernel-devicetree "
IMAGE_INSTALL_append_am335x-nbhw16 = " \
                nbhw16-ctrl \
                tibluetooth \
                "

LICENSE = "BSD"

