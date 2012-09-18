#!/bin/bash
JOBS_RUNNING=""
MAX_JOBS_RUNNING=4
NUMBER=$1

if [ -z $NUMBER ]
then
	NUMBER=1
fi

update_jobs_running(){
	JOBS_RUNNING=0
	for i in `cat ~/cecj/scripts/nodes | grep -v "^#"`
	do
		TMP=`ssh $i numberofjobs.sh`
		if [ "$TMP" -gt "$JOBS_RUNNING" ]
		then
			JOBS_RUNNING=$TMP
		fi
	done
}

for test in `cat ~/cecj/scripts/tests | grep -v "^#"`
do
	while [ 1 = 1 ]
	do
		update_jobs_running
		if [ "$JOBS_RUNNING" -lt "$MAX_JOBS_RUNNING" ]
		then
			break
		fi
		sleep 60
	done
	echo "Starting test $test"
	cd $test
	distribute.sh $NUMBER
done
