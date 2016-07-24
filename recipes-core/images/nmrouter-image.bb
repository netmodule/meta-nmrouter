inherit core-image

SUMMARY = "Test image"

KERNEL_IMAGETYPE = "uImage"

IMAGE_FSTYPES_append = " tar.gz "

IMAGE_FEATURES_append = " \
                tools-debug \
                "

IMAGE_FEATURES_append = " \
                package-management \
                ssh-server-openssh \
                "

BENCH_TOOLS = " \
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

EASY_EDITOR = " \
                nano \
              "

BENCH_TOOLS_cortexa9hf-neon_append = " cpuburn-neon "
BENCH_TOOLS_cortexa8hf-neon_append = " cpuburn-neon "

PREFERED_VERSION-pn_nodejs = "4.4.5"

IMAGE_INSTALL_append = " \
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
                kernel-devicetree \
                node \
                python-subprocess \
                networkmanager \
                cockpit \
                ${BENCH_TOOLS} \
                ${EASY_EDITOR} \
                "

IMAGE_INSTALL_cortex9hf-neon_append = " kernel-devicetree "
IMAGE_INSTALL_cortex9hf_append = " kernel-devicetree "
IMAGE_INSTALL_append_am335x-nbhw16 = " nbhw16-ctrl "

LICENSE = "BSD"

