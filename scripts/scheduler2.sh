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

test="/home/inf84813/cecj/tests/08_hybrid"
for num in 1 6 11 16 21 26
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
	echo -n `date `
	echo "Starting test $test with $num"
	cd $test
	distribute.sh $num
done

exit

while [ 1 = 1 ]
do
	update_jobs_running
        if [ "$JOBS_RUNNING" = "0" ]
        then
        	break
        fi
        sleep 60
done
echo "Jobs done"
DATE=`date +"%Y.%m.%d_%H:%M:%S"`
ssh jawora@hydra.dyndns.biz mkdir /home/jawora/tests/$DATE
scp -r /home/inf84813/cecj/tests jawora@hydra.dyndns.biz:/home/jawora/tests/$DATE
echo "Results copied"
