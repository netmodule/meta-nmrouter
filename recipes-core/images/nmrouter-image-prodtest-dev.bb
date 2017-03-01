require nmrouter-image-prodtest.bb

IMAGE_INSTALL_append = " \
    packagegroup-core-ssh-openssh \
    "
