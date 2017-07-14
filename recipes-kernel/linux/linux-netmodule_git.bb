# Copyright (C) 2013 NetModule AG

inherit kernel
require linux-dtb.inc

DESCRIPTION = "Linux kernel for various NetModule hardware"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

SRC_URI = "git://github.com/netmodule/linux.git;protocol=https"

SRCREV ?= "6f7da290413ba713f0cdd9ff1a2a9bb129ef4f6c"
PV ?= "4.12.0-${SRCPV}"

PR = "r0"

S = "${WORKDIR}/git"

INSANE_SKIP_${PN} += "version-going-backwards"

#If a KERNEL_DEFCONFIG is specified, the defconfig specified in SRC_URI will be overwritten!
do_configure_append(){
    if [ "${KERNEL_DEFCONFIG}" != "" ]; then
        oe_runmake ${KERNEL_DEFCONFIG}
    fi
}

do_install_append(){
    if [ "${KERNEL_DEVICETREE}" != "" ]; then
        ln -s devicetree-${DTB_SYMLINK_NAME}.dtb ${D}/${KERNEL_IMAGEDEST}/devicetree.dtb
    fi
    if [ "${KERNEL_IMAGETYPE}" = "uImage" ]; then
        ln -s ${KERNEL_IMAGETYPE}-${KERNEL_VERSION} ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}
    fi

    # Module.symvers gets updated during the
    # building of the kernel modules. We need to
    # update this in the shared workdir since some
    # external kernel modules has a dependency on
    # other kernel modules and will look at this
    # file to do symbol lookups
    # THIS WILL BE FIXED IN FUTURE VERSIONS!
    cp Module.symvers ${STAGING_KERNEL_BUILDDIR}/
}

