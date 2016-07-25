#!/bin/sh

UIM="uim"
UIM_OPTS="-s 115200 -d /dev/ttyO5 -c"
UIM_PID="/var/run/uim.pid"

BLUETOOTHD="/usr/lib/bluez5/bluetooth/bluetoothd"
BLUETOOTHD_PID="/var/run/bluetoothd.pid"


case $1 in
start)
    modprobe st_drv &> /dev/null
    eval start-stop-daemon -b -S -q -x $UIM -p $UIM_PID -- "$UIM_OPTS"
    modprobe btwilink
    eval start-stop-daemon -b -S -q -x $BLUETOOTHD -p $BLUETOOTHD_PID
    ;;

stop)
    eval start-stop-daemon --remove-pidfile -K -p $BLUETOOTHD_PID
    rmmod btwilink
    eval start-stop-daemon --remove-pidfile -K -p $UIM_PID
    rmmod st_drv
    ;;

restart)
    $0 stop
    sleep 1
    $0 start
    ;;
*)
    echo "Usage: $0 {start|stop|restart}"
    exit 1
esac


