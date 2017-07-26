#!/bin/sh

hw=$1
sim=$2
slot=$3

usage()
{
    echo "switch-sim.sh fpga sim slot"
    echo "  fpga: "
    echo "        0 means FPGA on hw14/08"
    echo "        1 means FPGA on hw17/hw18"
    echo "  sim: SIM card slot starting with 1"
    echo "  slot: PCIe slot where the SIM should be connected to. Starting with 1, 0 means disable"
    echo
}

if [ "$hw" = "" ]; then
    usage
    exit 1
fi

if [ "$sim" = "" ]; then
    usage
    exit 1
fi

if [ "$slot" = "" ]; then
    usage
    exit 1
fi

switch_fpga_0()
{
    sim=$1
    slot=$2

    if [ $sim -lt 1 -o $sim -gt 4 ]; then
        echo "SIM should be between 1 and 4"
        exit 2
    fi

    if [ $slot -lt 0 -o $slot -gt 6 ]; then
        echo "SLOT should be between 0 and 4"
        exit 3
    fi

    # Use incredible simbus function
    simbus_path="/sys/class/leds/simbus"
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

    # On HW08/HW14 we need to enable/disable sim slot
    sim_path="/sys/class/leds/sim$sim"
    if [ $sim -eq 0 ]; then
        echo 0 > "$sim_path:en"
    fi

    # Now set sim sel according to slot selection
    sim_path="/sys/class/leds/sim$sim:sel"
    # Because we enable/disable sim, 0 already means sim 1 (starting at 1)
    slot=$(($slot - 1))
    for i in $(seq 0 1); do
        # inverse logic test exits 0 on success
        test 0 -eq $(($slot & (2**$i)))
        val=$?
        echo $val > "$sim_path$i/brightness"
    done
}

switch_fpga_1()
{
    sim=$1
    slot=$2

    if [ $sim -lt 1 -o $sim -gt 4 ]; then
        echo "SIM should be between 1 and 4"
        exit 2
    fi

    if [ $slot -lt 0 -o $slot -gt 4 ]; then
        echo "SLOT should be between 0 and 4"
        exit 3
    fi

    # Set sim sel according to slot
    sim_path="/sys/class/leds/sim$sim:sel"
    for i in $(seq 0 2); do
        # inverse logic test exits 0 on success
        test 0 -eq $(($slot & (2**$i)))
        val=$?
        echo $val > "$sim_path$i/brightness"
    done
}

case $hw in
    0)
        exit switch_fpga_0 $sim $slot
        ;;
    1)
        exit switch_fpga_1 $sim $slot
        ;;
    *)
        usage
        ;;
esac

exit 0
