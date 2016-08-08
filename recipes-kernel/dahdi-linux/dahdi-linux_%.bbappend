FILES_${PN}-dev_append = " \
                        /usr/share/dahdi-linux/Module.symvers \
                        "

do_install_append() {
    mkdir -p ${D}/usr/share/dahdi-linux/
    cp drivers/dahdi/Module.symvers ${D}/usr/share/dahdi-linux/
}

