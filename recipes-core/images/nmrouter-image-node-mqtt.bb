require nmrouter-image.bb

SUMMARY = "nmrouter image that includes node-red"

IMAGE_INSTALL_append = " \
                nodejs \
                node-mqtt \
                node-dbus \
                python3 \
                python3-pip \
                python3-misc \
                "

IMAGE_INSTALL_remove = " \
                python-pip \
                "