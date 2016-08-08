require nmrouter-image.bb

IMAGE_INSTALL_append = " \
                asterisk \
                dahdi-linux \
                dahdi-tools \
                amfeltec-usb \
                "
