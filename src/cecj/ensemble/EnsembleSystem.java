package cecj.ensemble;

import ec.EvolutionState;
import ec.Individual;
import ec.Setup;
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
		
		//Set variables in ensemble
		ind.setIndividualsEnsemble(individuals);
	}

}
