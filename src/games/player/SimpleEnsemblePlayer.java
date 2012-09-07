package games.player;

import games.Board;

public class SimpleEnsemblePlayer extends EnsemblePlayer {

	public EvolvedPlayer createEmptyCopy() {
		return new SimpleEnsemblePlayer();
	}
	
	public double evaluate(Board board) {
		double maxValue = Double.MIN_VALUE;
		double currValue;
		for (EvolvedPlayer player : playersEnsemble){
			currValue = player.evaluate(board);
			maxValue = (currValue > maxValue) ? (currValue) : (maxValue);
		}
		return maxValue;
	}
	
}
