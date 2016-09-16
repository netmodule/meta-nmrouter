SUMMARY = "ModemManager is a daemon controlling broadband devices/connections"
DESCRIPTION = "ModemManager is a DBus-activated daemon which controls mobile broadband (2G/3G/4G) devices and connections"
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/ModemManager/"
LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://COPYING.LIB;md5=4fbd65380cdd255951079008b364516c \
"

inherit autotools gettext gtk-doc systemd

DEPENDS = "glib-2.0 libmbim libqmi polkit libgudev dbus-glib"

SRC_URI = " \
    http://www.freedesktop.org/software/ModemManager/ModemManager-${PV}.tar.xz \
    file://add-me909s-support.patch \
    file://disable-dhcp-on-me909u.patch \
    "

SRC_URI[md5sum] = "d9d93d2961ee35b4cd8a75a6a8631cb4"
SRC_URI[sha256sum] = "a94f4657a8fa6835e2734fcc6edf20aa8c8d452f62299d7748541021c3eb2445"

S = "${WORKDIR}/ModemManager-${PV}"

FILES_${PN} += " \
    ${datadir}/icons \
    ${datadir}/polkit-1 \
    ${datadir}/bash-completion \
    ${libdir}/ModemManager \
    ${systemd_unitdir}/system \
"

FILES_${PN}-dev += " \
    ${datadir}/dbus-1 \
    ${libdir}/ModemManager/*.la \
"

FILES_${PN}-staticdev += " \
    ${libdir}/ModemManager/*.a \
"

FILES_${PN}-dbg += "${libdir}/ModemManager/.debug"

SYSTEMD_SERVICE_${PN} = "ModemManager.service"
