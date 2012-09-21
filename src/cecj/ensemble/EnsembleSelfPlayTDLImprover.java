package cecj.ensemble;

import ec.EvolutionState;
import ec.Individual;
import games.player.EnsemblePlayer;
import games.player.EvolvedPlayer;
import games.player.LearningPlayer;
import games.scenario.SelfPlayTDLScenario;
import cecj.app.SelfPlayTDLImprover;

public class EnsembleSelfPlayTDLImprover extends SelfPlayTDLImprover {
	@Override
	public void improve(EvolutionState state, Individual ind) {
		if (!(playerPrototype instanceof EnsemblePlayer) || !(ind instanceof EnsembleIndividual))
			state.output.error("EnsembleSelfPlayTDLImprover is meant to be used only with Ensembles");
		
		EnsemblePlayer ensemblePlayer = (EnsemblePlayer) (playerPrototype.createEmptyCopy());
		ensemblePlayer.readFromIndividual(ind);

		for (EvolvedPlayer player : ensemblePlayer.getPlayersEnsemble()){
			if (player instanceof LearningPlayer) {
				SelfPlayTDLScenario selfPlayScenario = new SelfPlayTDLScenario((LearningPlayer) player,
						randomness, learningRate, lambda);
				for (int r = 0; r < repeats; r++) {
					boardGame.reset();
					selfPlayScenario.play(boardGame);
				}
			} else {
				state.output.fatal("Can not improve player which is not a LearningPlayer instance.");
			}
		}
	}

}
