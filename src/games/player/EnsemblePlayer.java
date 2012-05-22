package games.player;

import java.util.Arrays;

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
	private double[] boundaries;
	private int[] order;
	
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
		
		for (int i = 0; i < order.length; i++)
			order[i] = i;

		for (int i = 0; i < boundaries.length; i++)
			boundaries[i] = (i+1)/(boundaries.length+1);
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
			order = ensemble.getOrder();
			boundaries = ensemble.getBoundaries();
			
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
		ind.setBoundaries(this.boundaries);
		ind.setOrder(this.order);
		ind.fitness = new SimpleFitness();
				
		return ind;
	}

	public void randomizeWeights(MersenneTwisterFast random, double range) {
		for (EvolvedPlayer player : playersEnsemble)
			player.randomizeWeights(random, range);
		
		for (int i = 0; i < boundaries.length; i++){
			do{
				boundaries[i] = random.nextDouble();
			} while (boundaries[i] == 0);
		}
		Arrays.sort(boundaries);

		Arrays.fill(order, -1);
		int index = 0;
		for (int i = 0; i < order.length; i++){
			do{
				index = random.nextInt(order.length);
			} while (order[index] == -1);
			order[index] = i;
		}
		
	}

}
