require nmrouter-image.bb

SUMMARY = "nmrouter image that includes cockpit"

IMAGE_INSTALL_append = " \
                cockpit \
                "

