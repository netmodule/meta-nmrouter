#!/bin/bash

set_gpio() {
    if [ ! -d "/sys/class/gpio/gpio$1" ]; then
        echo $1 > /sys/class/gpio/export
    fi

    echo out > /sys/class/gpio/gpio$1/direction
    echo $2 > /sys/class/gpio/gpio$1/value
}

set_gpio 14 1
set_gpio 15 1
set_gpio 27 1

if ! grep -q gateway_ID /opt/lora/local_conf.json; then
    mac=$(ip link show eth0 | grep link | awk '{print $2 };' | sed 's/://g')
    sed -i "3i\    \"gateway_ID\": \"${mac^^}\"," /opt/lora/local_conf.json
fi

cd /opt/lora

# Wait until power on
sleep 2

while [ $(route -n | wc -l) -lt 3 ]; do
    sleep 5
done

# Fire up the forwarder.
/opt/lora/gps_pkt_fwd
