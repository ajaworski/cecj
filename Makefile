#
# Makefile created by Jeff Bassett, with some
# tweaks by Sean Luke
#
# To compile everything but GUI:	make 
# To delete class files:		make clean
# To build the documentation:		make docs
# To auto-indent using Emacs:		make indent
# To build everything + GUI:		make gui 
#	(requires JFreeChart: www.jfree.org/jfreechart/)
#	(requires iText: www.lowagie.com/iText/)
# [also, used here at GMU, you can ignore it...]
# Prepare for distribution:		make dist
#

JAVAC = javac
# ${JAVACFLAGS}

JAVACFLAGS = -target 1.4 -source 1.4 ${FLAGS}
FLAGS = -g -Xlint:deprecation -Xlint:unchecked

DIRS = \
src/utils/*.java \
src/utils/league/*.java \
src/utils/display/*.java \
src/ec/display/*.java \
src/ec/select/*.java \
src/framsticks/*.java \
src/cecj/ensemble/*.java \
src/cecj/utils/*.java \
src/cecj/ntuple/*.java \
src/cecj/sampling/*.java \
src/cecj/app/*.java \
src/cecj/app/numbers_game/*.java \
src/cecj/app/go/*.java \
src/cecj/app/othello/*.java \
src/cecj/interaction/*.java \
src/cecj/fitness/*.java \
src/cecj/eval/*.java \
src/cecj/problem/*.java \
src/cecj/subgame/*.java \
src/cecj/statistics/*.java \
src/cecj/archive/*.java \
src/cecj/archive/wojtek/*.java \
src/games/*.java \
src/games/player/*.java \
src/games/player/mlp/*.java \
src/games/scenario/*.java \

all: base

base:
	@ echo This builds the code except for gui
	@ echo For other Makefile options, type:  make help
	@ echo
	${JAVAC} -cp "../ECJ/:../ECJ/libraries/itext-2.1.3.jar:../ECJ/libraries/jcommon-1.0.14.jar:../ECJ/libraries/jfreechart-1.0.11.jar" ${DIRS}

clean:
	find . -name "*.class" -exec rm -f {} \;
	find . -name "*.stat" -exec rm -f {} \;
	find . -name ".DS_Store" -exec rm -rf {} \;
	find . -name "*.java*~" -exec rm -rf {} \;
