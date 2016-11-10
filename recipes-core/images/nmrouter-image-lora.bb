require nmrouter-image.bb

IMAGE_INSTALL_append = " \
                lora-gateway-utils \
                lora-packet-forwarder \
                "
