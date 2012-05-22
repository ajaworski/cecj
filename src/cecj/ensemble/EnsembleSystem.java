package cecj.ensemble;

import java.util.Arrays;
import java.util.Collections;
import java.util.TreeSet;

import cecj.ntuple.NTupleIndividual;

import ec.EvolutionState;
import ec.Individual;
import ec.Setup;
import ec.util.MersenneTwisterFast;
import ec.util.Parameter;
import ec.vector.DoubleVectorIndividual;

public class EnsembleSystem implements Setup {
	
	public void setup(EvolutionState state, Parameter base) {
		//TODO add parameters		
	}
	
	public void randomizeIndividual(EvolutionState state, int thread, EnsembleIndividual ind) {
		//TODO add parameters
		//Create individuals in ensemble
		Individual[] individuals = new Individual[3];
		for (int i = 0; i < 3; i++){
			DoubleVectorIndividual newInd = new DoubleVectorIndividual();
			newInd.setGenomeLength(64);
			newInd.reset(state, thread);
			individuals[i] = newInd;
		}
		
		//Init boundaries and order
		double[] boundaries = new double[individuals.length - 1];
		int[] order = new int[individuals.length];
		
		MersenneTwisterFast rng = state.random[thread];		
		for (int i = 0; i < boundaries.length; i++){
			do{
				boundaries[i] = rng.nextDouble();
			} while (boundaries[i] == 0);
		}
		Arrays.sort(boundaries);

		Arrays.fill(order, -1);
		int index = 0;
		for (int i = 0; i < order.length; i++){
			do{
				index = rng.nextInt(order.length);
			} while (order[index] == -1);
			order[index] = i;
		}


		//Set variables in ensemble
		ind.setIndividualsEnsemble(individuals);
		ind.setBoundaries(boundaries);
		ind.setOrder(order);
	}

}
