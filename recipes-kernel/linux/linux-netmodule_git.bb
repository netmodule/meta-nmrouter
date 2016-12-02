# Copyright (C) 2013 NetModule AG

inherit kernel
require linux-dtb.inc

DESCRIPTION = "Linux kernel for various NetModule hardware"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

BRANCH ?= "master"
SRC_URI = "git://git@gitlab.com:eichest/meta-nmrouter.git;protocol=ssh;branch=${BRANCH}"

SRCREV ?= "${AUTOREV}"
PV ?= "4.5-${SRCPV}"
PV_beaglebone = "4.4-${SRCPV}"
PV_am335x-nbhw16 = "4.4-${SRCPV}"
PV_armada-385-nbhw17-nb2800 = "4.7-${SRCPV}"

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

