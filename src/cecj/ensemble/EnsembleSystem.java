package cecj.ensemble;

import java.util.Arrays;
import java.util.Collections;

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
		if (!(ind.species instanceof EnsembleSpecies))
			state.output.fatal("OuterCrossover should work on EnsembleSpecies");
		
		EnsembleSpecies species = (EnsembleSpecies) ind.species;
		MersenneTwisterFast rand = state.random[thread];
		int value;		
		boolean out;
		int tries;

		
		//randomize boundaries if bound_mutation is greater than 0, distribute evenly otherwise
		if (species.getOuterMutationBoundariesChangeLikelihood() > 0.0){
			for (int i = 0; i < ind.getBoundaries().length; i++){
				tries = 100;
				do{
					value = rand.nextInt(59) + 2; //XXX hardcoded!!!
					out = true;
					for (int j = 0; j < i; j++){
						if (ind.getBoundaries()[j] == value){
							out = false;
							break;
						}
					}
					tries--;
				} while (!out && tries >= 0);
				if (out)
					ind.getBoundaries()[i] = value;
				else
					state.output.fatal("Couldn't randomize boundaries. Consider lowering boundaries count.");
				
			}
			Arrays.sort(ind.getBoundaries(), Collections.reverseOrder());
		} else {
			int length = 60 / (ind.getBoundaries().length + 1); //XXX hardcoded
			for (int i = 0; i < ind.getBoundaries().length; i++){
				ind.getBoundaries()[i] = 60 - (i+1)*length;
			}
		}
		
		//Randomize groups if group_mutation is greater than 0, distribute evenly otherwise
		if (species.getOuterMutationGroupsChangeProbability() > 0.0){
			for (int i = 0; i < ind.getGroups().length; i++){
				tries = 100;
				do{
					value = rand.nextInt(ind.getIndividualsEnsemble().length - 1);
					out = true;
					for (int j = 0; j < i; j++){
						if (ind.getGroups()[j] == value){
							out = false;
							break;
						}
					}
					tries--;
				} while (!out && tries >= 0);
				if (out)
					ind.getGroups()[i] = value;
				else
					state.output.fatal("Couldn't randomize groups bounds. Consider lowering group count.");
			}
			Arrays.sort(ind.getGroups());
		} else {
			int length = ind.getIndividualsEnsemble().length / (ind.getGroups().length + 1);
			for (int i = 0; i < ind.getGroups().length; i++){
				ind.getGroups()[i] = (i+1)*length;
			}
		}
		
		//randomize order
		int end = rand.nextInt(ind.getIndividualsEnsemble().length);
		for (int i = 0; i < end; i++){
			int x, y;
			do{
				x = rand.nextInt(ind.getIndividualsEnsemble().length);
				y = rand.nextInt(ind.getIndividualsEnsemble().length);
			} while(x == y);
			Individual temp = ind.getIndividualsEnsemble()[x];
		    ind.getIndividualsEnsemble()[x] = ind.getIndividualsEnsemble()[y];
		    ind.getIndividualsEnsemble()[y] = temp;
		}
		
		
		
		//randomize each individual in an ensemble
		for (int i = 0; i < ind.getIndividualsEnsemble().length; i++){
			((DoubleVectorIndividual)(ind.getIndividualsEnsemble()[i])).reset(state, thread);
		}
		
	}

}
