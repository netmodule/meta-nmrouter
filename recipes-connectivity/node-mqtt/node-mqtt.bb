DESCRIPTION = "MQTT Library for NodeJS"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=261aa46f11e9a7bdbea1dea7eb8bcb6c"

PV = "2.1.3"
PR = "r2"

SRC_URI = "https://github.com/mqttjs/MQTT.js/archive/v2.1.3.zip;name=src"

SRC_URI[src.md5sum] = "6013d1df7abe138c0c379fbe48be0b30"
SRC_URI[src.sha256sum] = "d848e9ff133d015dd99b30a1b26a057026deebe9328efa9f9e84e6f64c8e5608"

RDEPENDS_${PN} = "nodejs"

S="${WORKDIR}/MQTT.js-${PV}"

inherit allarch npm-install-global
