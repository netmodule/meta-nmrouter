#!/bin/sh

slot=$1
on=$2

usage()
{
    echo "power-pcie-slot.sh slot on"
    echo "  slot: pcie slot to reset"
    echo "  on: 0 off, 1 on"
}

if [ "$slot" = "" ]; then
    usage
    return 1
fi

if [ "$on" = "" ]; then
    usage
    return 1
fi


power_slot()
{
    slot=$1
    on=$2
    pwr_path="/sys/class/leds/pcie$slot:pwr/brightness"
    echo $on > $pwr_path
}

power_slot $slot $on
