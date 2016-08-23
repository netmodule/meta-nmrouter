#!/usr/bin/python

import subprocess
import time
from optparse import OptionParser


WLAN_INTERFACE = "wlan0"
init = None


def call_and_throw(cmd):
    if subprocess.call(cmd, shell=True):
        raise RuntimeError("Command {0} failed".format(cmd))


def write_to_modem(data, read=False):
    global init
    if init is None:
        init = True
        call_and_throw("stty -F /dev/ttyUSB0 -echo -echok -echoe")

    f = open("/dev/ttyUSB0", "r+")
    f.write(data)
    if read:
        time.sleep(0.5)
        while f.readable() and read:
            data += f.readline()
    f.close()
    return data


def setup_modem(provider):
    apn = "gprs.swisscom.ch"
    if provider == "salt":
        apn = "click"

    modem_config = """[connection]
id=Modem
type=gsm
interface-name=ttyUSB2
permissions=
secondaries=

[gsm]
apn={0}
number=*99#

[ipv4]
dns-search=
method=auto

[ipv6]
dns-search=
method=auto
""".format(apn)

    f = open("/etc/NetworkManager/system-connections/Modem", "w")
    f.write(modem_config)
    f.close()

    call_and_throw("chmod 600 /etc/NetworkManager/system-connections/Modem")
    call_and_throw("nmcli c load /etc/NetworkManager/system-connections/Modem")
    call_and_throw("nmcli c up Modem")


def setup_ap():
    ap_config = """ [connection]
id=Wifi
type=wifi
interface-name=wlan0
autoconnect=true
permissions=
secondaries=

[wifi]
band=bg
mac-address-blacklist=
mode=ap
seen-bssids=
ssid=test

[wifi-security]
auth-alg=open
group=ccmp;
key-mgmt=wpa-psk
pairwise=ccmp;
proto=rsn;
psk=NetModu1e

[ipv4]
address1=192.168.0.1/24
dns-search=
method=manual

[ipv6]
dns-search=
method=auto
"""
    f = open("/etc/NetworkManager/system-connections/Wifi", "w")
    f.write(ap_config)
    f.close()
    call_and_throw("chmod 600 /etc/NetworkManager/system-connections/Wifi")
    call_and_throw("nmcli c load /etc/NetworkManager/system-connections/Wifi")
    call_and_throw("nmcli c up Wifi")

    dnsmasq_config = """
    domain-needed
    bogus-priv
    dhcp-range=192.168.0.100,192.168.0.200,12h
    """
    f = open("/etc/dnsmasq.conf", "w")
    f.write(dnsmasq_config)
    f.close()
    call_and_throw("systemctl restart dnsmasq")


def setup_routing():
    f = open("/proc/sys/net/ipv4/ip_forward", "w")
    f.write("1\n")
    f.close()
    call_and_throw("iptables -t nat -A POSTROUTING -o eth2 -j MASQUERADE")


def main():
    usage = "usage: %prog [options] arg"
    parser = OptionParser(usage)
    parser.add_option("-m", "--modem", action="store_true", dest="modem",
                      help="Create connection over GSM/UMTS/LTE Modem",
                      default=False)
    parser.add_option("-c", "--connection",
                      action="store_const", dest="connection",
                      help="Provider of the modem connection (swisscom/salt)",
                      default="swisscom")
    parser.add_option("-a", "--ap", action="store_true", dest="ap",
                      help="Create an access point", default=False)
    parser.add_option("-r", "--routing", action="store_true", dest="routing",
                      help="Enable routing trough modem", default=False)

    (options, args) = parser.parse_args()

    if options.modem:
        setup_modem(options.connection)

    if options.ap:
        setup_ap()

    if options.routing:
        setup_routing()

if __name__ == "__main__":
    main()
