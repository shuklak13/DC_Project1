#!/bin/bash

# Change this to your netid
#netid=rkp170230
netid=kxs141930

# Root directory of your project
PROJDIR=$HOME/DC_Project1
$PROJDIR/scripts/incr.sh

# Directory where the config file is located on your local system
CONFIGLOCAL=$HOME/DC_Project1/config/configDemo.txt

# Directory your java classes are in
javac $HOME/DC_Project1/src/dc_project1/*.java
BINDIR=$HOME/DC_Project1/src/dc_project1

# Your main project class
PROG=DC_Project1

JAR=$HOME/DC_Project1/dist/DC_Project1.jar

cat $CONFIGLOCAL | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
    echo $i
    while [[ $i -gt 0 ]]
    do
    	read line
    	node=$( echo $line | awk '{ print $1 }' )
        host=$( echo $line | awk '{ print $2 }' )
	echo $i
        echo $node
        echo $host
	
	gnome-terminal -e "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $netid@$host java -jar $JAR $CONFIGLOCAL $node; $SHELL" &

        i=$(( i - 1 ))
    done
)
