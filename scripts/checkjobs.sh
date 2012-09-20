#!/bin/bash

for i in `cat ~/cecj/scripts/nodes | grep -v "^#"`
	do
                TMP=`ssh $i numberofjobs.sh`
		echo "$i: $TMP"
        done

