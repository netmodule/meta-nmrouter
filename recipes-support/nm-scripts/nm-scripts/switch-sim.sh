#!/bin/bash

hw=$1
sim=$2
slot=$3

usage()
{
    echo "switch-sim.sh fpga sim slot"
    echo "  fpga: "
    echo "        0 means FPGA on hw14/08"
    echo "        1 means FPGA on hw17/hw18"
    echo "  sim: simcard slot starting with 1"
    echo "  slot: pcie slot to connect sim to. Starting with 1, 0 means disable"
}

if [ "$hw" = "" ]; then
    usage
    return 1
fi

if [ "$sim" = "" ]; then
    usage
    return 1
fi

if [ "$slot" = "" ]; then
    usage
    return 1
fi

switch_fpga_0()
{
    sim=$1
    slot=$2

    if [ $sim -lt 1 -o $sim -gt 4 ]; then
        echo "SIM should be between 1 and 4"
        return 2
    fi

    if [ $slot -lt 0 -o $slot -gt 6 ]; then
        echo "SLOT should be between 0 and 4"
        return 3
    fi

    simbus_path="/sys/class/leds/simbus"
    # set bitwise
    case $slot in
        1)
            echo 1 > $simbus_path"13:en/brightness"
            echo 0 > $simbus_path"13:sel/brightness"
            slot=1
            ;;
        3)
            echo 1 > $simbus_path"13:en/brightness"
            echo 1 > $simbus_path"13:sel/brightness"
            slot=1
            ;;
        5)
            echo 1 > $simbus_path"56:en/brightness"
            echo 0 > $simbus_path"56:sel/brightness"
            slot=4
            ;;
        6)
            echo 1 > $simbus_path"56:en/brightness"
            echo 1 > $simbus_path"56:sel/brightness"
            slot=4
            ;;
    esac

    sim_path="/sys/class/leds/sim$sim"
    if [ $sim -eq 0 ]; then
        echo 0 > "$sim_path:en"
    fi

    # set bitwise
    sim_path="/sys/class/leds/sim$sim:sel"
    slot=$(($slot - 1))
    for i in $(seq 0 1); do
        # inverse logic test returns 0 on success
        test 0 -eq $(($slot & (2**$i)))
        val=$?
        echo $slot: $i: $val
        echo $val > "$sim_path$i/brightness"
    done
}

switch_fpga_1()
{
    sim=$1
    slot=$2

    if [ $sim -lt 1 -o $sim -gt 4 ]; then
        echo "SIM should be between 1 and 4"
        return 2
    fi

    if [ $slot -lt 0 -o $slot -gt 4 ]; then
        echo "SLOT should be between 0 and 4"
        return 3
    fi

    # set bitwise
    sim_path="/sys/class/leds/sim$sim:sel"
    for i in $(seq 0 2); do
        # inverse logic test returns 0 on success
        test 0 -eq $(($slot & (2**$i)))
        val=$?
        echo $val > "$sim_path$i/brightness"
    done
}

case $hw in
    0)
        switch_fpga_0 $sim $slot
        ;;
    1)
        switch_fpga_1 $sim $slot
        ;;
    *)
        usage
        ;;
esac
