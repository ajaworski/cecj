package games.player;

import games.Board;

public class SimpleEnsemblePlayer extends EnsemblePlayer {

	public EvolvedPlayer createEmptyCopy() {
		return new SimpleEnsemblePlayer();
	}
	
	public double evaluate(Board board) {
		double returnValue = 0;
		if (this.combinationMethod == COMBINATION_MAX){
			returnValue = Double.MIN_VALUE;
			double currValue;
			for (EvolvedPlayer player : playersEnsemble){
				currValue = player.evaluate(board);
				returnValue = (currValue > returnValue) ? (currValue) : (returnValue);
				evalCount++;
			}
		} else if (this.combinationMethod == COMBINATION_AVG){
			returnValue = 0;
			for (EvolvedPlayer player : playersEnsemble){
				returnValue += player.evaluate(board);
				evalCount++;
			}
			returnValue = returnValue / playersEnsemble.length;
		} else if (this.combinationMethod == COMBINATION_MIN){
			returnValue = Double.MAX_VALUE;
			double currValue;
			for (EvolvedPlayer player : playersEnsemble){
				currValue = player.evaluate(board);
				returnValue = (currValue < returnValue) ? (currValue) : (returnValue);
				evalCount++;
			}
		}
		return returnValue;
	}
	
}
