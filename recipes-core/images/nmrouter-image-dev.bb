require nmrouter-image.bb

SUMMARY = "nmrouter image for developement only"

IMAGE_FEATURES_append = " \
                tools-debug \
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


IMAGE_INSTALL_append = " \
                lrzsz \
                ${BENCH_TOOLS} \
                ${EASY_EDITOR} \
                "

