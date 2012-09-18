#!/bin/bash
NUMBER="$1"
SEED="$2"
TEST="$3"

NUMBER=`printf "%02d" $NUMBER`

echo "$HOSTNAME: Number: $NUMBER, seed: $SEED" > $TEST/fit.$NUMBER.out
sleep 30
#cecj.sh $TEST/evolution.params "-p seed.0=$SEED -p stat.file=out.$NUMBER.stat -p stat.child.0.fitness-file=fitness.$NUMBER.stat -p stat.child.0.ind-file=ind.$NUMBER.stat"

