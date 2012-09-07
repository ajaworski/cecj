package games.player;

import games.Board;
import games.SimpleBoard;

public class BoundariesEnsemblePlayer extends EnsemblePlayer {

	public EvolvedPlayer createEmptyCopy() {
		return new BoundariesEnsemblePlayer();
	}

	public double evaluate(Board board) {
		int leftPieces = board.countPieces(SimpleBoard.EMPTY);
		int playerIndex = 0;
		while(playerIndex < this.boundaries.length && this.boundaries[playerIndex] > leftPieces)
			playerIndex++;
		return this.playersEnsemble[playerIndex].evaluate(board);
	}

}
