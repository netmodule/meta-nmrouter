inherit core-image

SUMMARY = "Test image"

UBOOT_CONFIG = "sdcard"

KERNEL_IMAGETYPE = "uImage"

IMAGE_FSTYPES_append = " tar.gz "

IMAGE_FEATURES += " \
                tools-debug \
                "

IMAGE_FEATURES += " \
                package-management \
                ssh-server-openssh \
                "

BENCH_TOOLS += " \
                tcpdump \
                lrzsz \
                lmbench \
                dbench \
                memtester \
                nbench-byte \
                tiobench \
                iozone3 \
                iperf \
                strongswan \
               "

BENCH_TOOLS_cortexa9hf-neon_append += " cpuburn-neon "

IMAGE_INSTALL += " \
                linux-firmware \
                linux-firmware-ath9k \
                hostapd \
                iw \
                crda \
                wpa-supplicant \
                openssh-sftp-server \
                bash \
                iproute2 \
                ethtool \
                i2c-tools \
                lrzsz \
                devmem2 \
                openvpn \
                iptables \
                pciutils \
                kernel-modules \
                ${BENCH_TOOLS} \
                "

IMAGE_INSTALL_cortex9hf-neon_append = " kernel-devicetree "
IMAGE_INSTALL_cortex9hf_append = " kernel-devicetree "

LICENSE = "BSD"

