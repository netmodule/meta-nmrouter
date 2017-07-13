DESCRIPTION = "Node-CAN extension"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://README.md;md5=e96bd3fe839e97ad96d6b3ea4ea48098"

SRC_URI = " \
    git://github.com/sebi2k1/node-can.git;protocol=https \
	"

SRCREV = "2f5cc70229e255f802521481919ff5ebc7942394"

S = "${WORKDIR}/git"

inherit npm-install-global

do_configure() {
	:
}

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

RDEPENDS_${PN} += " nodejs bash"
