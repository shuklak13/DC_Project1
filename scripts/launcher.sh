#!/bin/bash

# Change this to your netid
netid=kxs141930

# Root directory of your project
PROJDIR=$HOME/DC_Project1

# Directory where the config file is located on your local system
CONFIGLOCAL=$HOME/DC_Project1/config.txt

# Directory your java classes are in
javac=$HOME/DC_Project1/src/dc_project1/*.java
BINDIR=$HOME/DC_Project1/src/dc_project1

# Your main project class
PROG=DC_Project1

n=6

cat $CONFIGLOCAL | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
    echo $i
    while [[ $n -gt 0 ]]
    # echo $n
    do
    	echo "do loop"
    	read line
    	p=$( echo $line | awk '{ print $1 }' )
        host=$( echo $line | awk '{ print $2 }' )
	
	# gnome-terminal -e "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $netid@$host java -cp $BINDIR $PROG $p; $SHELL" &
	# ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $netid@$host java -cp $BINDIR $PROG $p; $SHELL

        n=$(( n - 1 ))
    done
)
