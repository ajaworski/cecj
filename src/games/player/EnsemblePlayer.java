package games.player;

import cecj.ensemble.EnsembleIndividual;
import cecj.ensemble.EnsembleSystem;
import ec.EvolutionState;
import ec.Individual;
import ec.simple.SimpleFitness;
import ec.util.MersenneTwisterFast;
import ec.util.Parameter;
import games.Board;

public class EnsemblePlayer implements EvolvedPlayer {

	private static final String P_ENSEMBLE_SYSTEM = "ensemble-system";
	
	private EvolvedPlayer[] playersEnsemble;
	
	public void setup(EvolutionState state, Parameter base) {
		EnsembleSystem system = new EnsembleSystem();
		system.setup(state, base.push(P_ENSEMBLE_SYSTEM));

		EnsembleIndividual ind = new EnsembleIndividual();
		system.randomizeIndividual(state, 0, ind);

		readFromIndividual(ind);		
	}
	
	public double evaluate(Board board) {
		//XXX evaluate
		return 0;
	}

	public void reset() {
		for (EvolvedPlayer player : playersEnsemble)
			player.reset();
	}



	public EvolvedPlayer createEmptyCopy() {
		return new EnsemblePlayer();
	}

	public void readFromIndividual(Individual ind)
			throws IllegalArgumentException {
		if (ind instanceof EnsembleIndividual) {
			EnsembleIndividual ensemble = ((EnsembleIndividual) ind);
			
			playersEnsemble = new EvolvedPlayer[ensemble.getIndividualsEnsemble().length];
			
			//TODO mapowanie z różnych indiwiduali na playerow
			for (int i = 0; i < ensemble.getIndividualsEnsemble().length; i++){
				WPCPlayer player = new WPCPlayer(8);
				player.readFromIndividual(ensemble.getIndividualsEnsemble()[i]);
				playersEnsemble[i] = player;
			}
			
		} else {
			throw new IllegalArgumentException("Individual should be of type EnsembleIndividual");
		}
		
	}

	public Individual createIndividual() {
		EnsembleIndividual ind = new EnsembleIndividual();
		
		Individual[] individuals = new Individual[playersEnsemble.length];
		
		for (int i = 0; i < playersEnsemble.length; i++){
			individuals[i] = playersEnsemble[i].createIndividual();
		}
		
		ind.setIndividualsEnsemble(individuals);
		ind.fitness = new SimpleFitness();
				
		return ind;
	}

	public void randomizeWeights(MersenneTwisterFast random, double range) {
		for (EvolvedPlayer player : playersEnsemble)
			player.randomizeWeights(random, range);
	}

}
