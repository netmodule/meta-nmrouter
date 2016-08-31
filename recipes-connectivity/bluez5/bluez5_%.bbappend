# Dependencies for obexd
RDEPENDS_${PN}_append = " glibc-gconv glibc-gconv-utf-16"

PACKAGECONFIG_append = " experimental obex-profiles"
