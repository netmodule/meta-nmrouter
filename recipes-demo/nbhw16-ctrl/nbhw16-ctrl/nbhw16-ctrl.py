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


def get_modem():
    interface = "eth2"
    try:
        call_and_throw("ifconfig {0} &> /dev/null".format(interface))
    except:
        interface = "usb0"
        # Make sure that we now have a valid modem
        call_and_throw("ifconfig {0} &> /dev/null".format(interface))
    return interface


def setup_modem(provider):
    if provider == "swisscom":
        write_to_modem("AT^NDISDUP=1,1,\"gprs.swisscom.ch\"\r\n")

    if provider == "salt":
        write_to_modem("AT^NDISDUP=1,1,\"click\"\r\n")

    call_and_throw("udhcpc -i {0}".format(get_modem()))


def setup_ap():
    call_and_throw("ifconfig {0} 192.168.0.1".format(WLAN_INTERFACE))
    # Start udhcpd with config from /etc/udhcpd.conf
    call_and_throw("udhcpd")


def setup_routing():
    f = open("/proc/sys/net/ipv4/ip_forward", "w")
    f.write("1\n")
    f.close()
    call_and_throw("iptables -t nat -A POSTROUTING -o {0} -j MASQUERADE".
                   format(get_modem()))


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
