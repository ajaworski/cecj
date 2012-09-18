#!/bin/bash
ps -e | grep runlocaljob.sh | wc | tr -s " " | cut -d " " -f 2
