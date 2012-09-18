#!/bin/bash
NUMBER=$1
I=1
TEST=`pwd`

for i in `cat ~/bin/seeds`
do
	seeds[$I]=$i
	let "I += 1"
done

if [ -z "$NUMBER" ]
then
	NUMBER=1
fi

for i in `cat ~/bin/nodes | grep -v "^#"`
do
	echo "$i"
	ssh $i "nohup runlocaljob.sh $NUMBER ${seeds[$NUMBER]} $TEST > /dev/null 2> /dev/null &"
	let "NUMBER += 1"
done
