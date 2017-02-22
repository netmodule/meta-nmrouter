SUMMARY = "node-dbus is a D-Bus binding for Node.js"
SECTION = "nodejs/module"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://README.md;beginline=46;endline=69;md5=d1a9d9b4039fb88c4b6cecd1de356fcc"

inherit npm-install-global
RDEPENDS_${PN} = "nodejs dbus"

PV = "0.2.20+gitr${SRCPV}"

SRC_URI = " \
            git://github.com/Shouqun/node-dbus.git;branch=master;protocol=git \
          "
SRCREV = "34d69ed11c3a1d3147b35d258701fdfeb033624c"
S = "${WORKDIR}/git"

