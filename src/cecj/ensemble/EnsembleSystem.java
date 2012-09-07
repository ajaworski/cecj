package cecj.ensemble;

import java.util.Arrays;
import java.util.Collections;

import ec.EvolutionState;
import ec.Individual;
import ec.Setup;
import ec.util.MersenneTwisterFast;
import ec.util.Parameter;
import ec.vector.DoubleVectorIndividual;
import ec.vector.FloatVectorSpecies;

public class EnsembleSystem implements Setup {
	
	public void setup(EvolutionState state, Parameter base) {
		//TODO add parameters		
	}
	
	public void randomizeIndividual(EvolutionState state, int thread, EnsembleIndividual ind) {
		//randomize boundaries
		MersenneTwisterFast rand = state.random[thread];
		int value;		
		boolean out;
		
		for (int i = 0; i < ind.getBoundaries().length; i++){
			do{
				value = rand.nextInt(62) + 1; //XXX hardcoded!!!
				out = true;
				for (int j = 0; j < i; j++){
					if (ind.getBoundaries()[j] == value){
						out = false;
						break;
					}
				}
				
			} while (!out);
			ind.getBoundaries()[i] = value;
		}
		Arrays.sort(ind.getBoundaries());
		for(int i = 0; i < ind.getBoundaries().length / 2; i++)
		{
		    int temp = ind.getBoundaries()[i];
		    ind.getBoundaries()[i] = ind.getBoundaries()[ind.getBoundaries().length - i - 1];
		    ind.getBoundaries()[ind.getBoundaries().length - i - 1] = temp;
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
