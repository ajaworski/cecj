#!/bin/bash
java -cp "$HOME/ECJ/:$HOME/ECJ/libraries/itext-2.1.3.jar:$HOME/ECJ/libraries/jcommon-1.0.14.jar:$HOME/ECJ/libraries/jfreechart-1.0.11.jar:$HOME/cecj/src" ec.Evolve -file $1 $2
