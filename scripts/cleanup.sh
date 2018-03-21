#!/bin/bash

# Change this to your netid
#netid=rkp170230
netid=kxs141930

# Root directory of your project
PROJDIR=$HOME/DC_Project1
CONFIGLOCAL=$PROJDIR/config/config2copy.txt

cat $CONFIGLOCAL | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
    echo $i
    while [[ $i -gt 0 ]]
    do
    	read line
        host=$( echo $line | awk '{ print $2 }' )

	echo $i
        echo $host
        gnome-terminal -e "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $netid@$host killall -u $netid" &
        sleep 1

        i=$(( i - 1 ))
    done
   
)


echo "Cleanup complete"
