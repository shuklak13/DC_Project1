#!/bin/bash

netid=kxs141930
PROJDIR=$HOME/DC_Project1
CONFIGLOCAL=$PROJDIR/config/config2copy.txt

cat $CONFIGLOCAL | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
    echo $i
    while [[ $i -gt 0 ]]
    do
    	read line
	echo $line
    	node=$( echo $line | awk '{ print $1 }' )
        host=$( echo $line | awk '{ print $2 }' )
	echo $node
	echo $host
        gnome-terminal -e "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $netid@$host killall -u $netid" &
        i=$(( i - 1 ))
    done
)

