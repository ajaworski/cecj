package games.player;

import games.Board;

public class SimpleEnsemblePlayer extends EnsemblePlayer {

	public EvolvedPlayer createEmptyCopy() {
		SimpleEnsemblePlayer player = new SimpleEnsemblePlayer();
		player.subplayer = this.subplayer;
		player.combinationMethod = this.combinationMethod;
		return player;		
	}
	
	public double evaluate(Board board) {
		double returnValue = 0;
		if (this.combinationMethod == COMBINATION_MAX){
			returnValue = Double.MIN_VALUE;
			double currValue;
			for (EvolvedPlayer player : getPlayersEnsemble()){
				currValue = player.evaluate(board);
				returnValue = (currValue > returnValue) ? (currValue) : (returnValue);
				evalCount++;
			}
		} else if (this.combinationMethod == COMBINATION_AVG){
			returnValue = 0;
			for (EvolvedPlayer player : getPlayersEnsemble()){
				returnValue += player.evaluate(board);
				evalCount++;
			}
			returnValue = returnValue / getPlayersEnsemble().length;
		} else if (this.combinationMethod == COMBINATION_MIN){
			returnValue = Double.MAX_VALUE;
			double currValue;
			for (EvolvedPlayer player : getPlayersEnsemble()){
				currValue = player.evaluate(board);
				returnValue = (currValue < returnValue) ? (currValue) : (returnValue);
				evalCount++;
			}
		}
		return returnValue;
	}

}
