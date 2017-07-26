#!/bin/sh

slot=$1

usage()
{
    echo "reset-pcie-slot.sh slot"
    echo "  slot: pcie slot to reset"
}

if [ "$slot" = "" ]; then
    usage
    return 1
fi

reset_slot()
{
    reset_path="/sys/class/leds/pcie$slot:rst/brightness"
    echo 1 > $reset_path
    sleep 1
    echo 0 > $reset_path
}

reset_slot $slot
