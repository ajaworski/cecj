package games.player;

import games.Board;
import games.SimpleBoard;

public class HybridEnsemblePlayer extends EnsemblePlayer{

	public EvolvedPlayer createEmptyCopy() {
		HybridEnsemblePlayer player = new HybridEnsemblePlayer();
		player.subplayer = this.subplayer;
		player.combinationMethod = this.combinationMethod;
		return player;
	}

	public double evaluate(Board board) {
		int leftPieces = board.countPieces(SimpleBoard.EMPTY);
		int groupIndex = 0;
		while(groupIndex < this.boundaries.length && this.boundaries[groupIndex] > leftPieces)
			groupIndex++;

		int groupStart;
		if (groupIndex == 0)
			groupStart = 0;
		else
			groupStart = groups[groupIndex - 1] + 1;
		
		int groupEnd;
		if (groupIndex == groups.length)
			groupEnd = getPlayersEnsemble().length - 1;
		else
			groupEnd = groups[groupIndex];
		
		double returnValue = 0;
		if (this.combinationMethod == COMBINATION_MAX){
			returnValue = Double.MIN_VALUE;
			double currValue;
			for (int i = groupStart; i <= groupEnd; i++){
				EvolvedPlayer player = getPlayersEnsemble()[i];
				currValue = player.evaluate(board);
				returnValue = (currValue > returnValue) ? (currValue) : (returnValue);
				evalCount++;
			}
		} else if (this.combinationMethod == COMBINATION_AVG){
			returnValue = 0;
			for (int i = groupStart; i <= groupEnd; i++){
				EvolvedPlayer player = getPlayersEnsemble()[i];
				returnValue += player.evaluate(board);
				evalCount++;
			}
			returnValue = returnValue / getPlayersEnsemble().length;
		} else if (this.combinationMethod == COMBINATION_MIN){
			returnValue = Double.MAX_VALUE;
			double currValue;
			for (int i = groupStart; i <= groupEnd; i++){
				EvolvedPlayer player = getPlayersEnsemble()[i];
				currValue = player.evaluate(board);
				returnValue = (currValue < returnValue) ? (currValue) : (returnValue);
				evalCount++;
			}
		}
		return returnValue;
		
	}

}
