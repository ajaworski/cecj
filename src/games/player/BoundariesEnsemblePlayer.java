package games.player;

import games.Board;
import games.SimpleBoard;

public class BoundariesEnsemblePlayer extends EnsemblePlayer {

	public EvolvedPlayer createEmptyCopy() {
		BoundariesEnsemblePlayer player = new BoundariesEnsemblePlayer();
		player.subplayer = this.subplayer;
		player.combinationMethod = this.combinationMethod;
		return player;
	}

	public double evaluate(Board board) {
		int leftPieces = board.countPieces(SimpleBoard.EMPTY);
		int playerIndex = 0;
		while(playerIndex < this.boundaries.length && this.boundaries[playerIndex] > leftPieces)
			playerIndex++;
		evalCount++;
		return this.getPlayersEnsemble()[playerIndex].evaluate(board);
	}

}
