package cecj.ensemble;

import ec.EvolutionState;
import ec.Individual;
import ec.simple.SimpleFitness;
import games.player.EnsemblePlayer;
import games.player.EvolvedPlayer;
import cecj.app.othello.OthelloHeuristicPlayer;

public class EnsembleStatisticsOthelloHeuristicPlayer extends OthelloHeuristicPlayer {
	@Override
	public float calculateObjectiveFitness(EvolutionState state, Individual ind) {
		EvolvedPlayer player = playerPrototype.createEmptyCopy();
		player.readFromIndividual(ind);

		if (player instanceof EnsemblePlayer && (ind.fitness) instanceof SimpleFitness){
			for (int i = 0; i < ((EnsemblePlayer)player).getPlayersEnsemble().length; i++){
				((SimpleFitness)((EnsembleIndividual)ind).getIndividualsEnsemble()[i].fitness).setFitness(state, calculateObjectiveFitness(state, ((EnsemblePlayer)player).getPlayersEnsemble()[i]), false);
			}
				
		}
		return calculateObjectiveFitness(state, player);

	}

}
