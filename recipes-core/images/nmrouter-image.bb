inherit core-image

SUMMARY = "nmrouter image"

KERNEL_IMAGETYPE = "uImage"

IMAGE_FSTYPES_append = " tar.gz "

IMAGE_FEATURES_append = " \
                package-management \
                ssh-server-openssh \
                "

FIRMWARE ?= "linux-firmware"
FIRMWARE_am335x-nbhw16 = " \
                linux-firmware-ath6k \
                linux-firmware-ath9k \
                linux-firmware-wl12xx\
                linux-firmware-wl18xx\
                "
FIRMWARE_armada-385 = " \
                linux-firmware-ath6k \
                linux-firmware-ath9k \
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
                modemmanager \
                board-descriptor \
                sw-update \
                python-pip \
                rng-tools \
                glibc-utils \
                glibc-gconv \
                glibc-gconv-utf-16 \
                glibc-gconv-utf-32 \
                bridge-utils \
                gpsd \
                ${FIRMWARE} \
                "

IMAGE_INSTALL_cortex9hf-neon_append = " kernel-devicetree "
IMAGE_INSTALL_cortex9hf_append = " kernel-devicetree "
IMAGE_INSTALL_append_am335x-nbhw16 = " \
                tibluetooth \
                bluez5-obex \
                bluez5-noinst-tools \
                canutils \
                wakeup-timer \
                "

LICENSE = "BSD"

