package cecj.ensemble;

import ec.EvolutionState;
import ec.Individual;
import ec.Setup;
import ec.util.Parameter;
import ec.vector.DoubleVectorIndividual;
import ec.vector.FloatVectorSpecies;

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
			newInd.species = new FloatVectorSpecies();
			newInd.species.setup(state, newInd.defaultBase());
			newInd.setup(state, newInd.defaultBase());
			newInd.setGenomeLength(64);
			newInd.reset(state, thread);
			individuals[i] = newInd;
		}
		
		//Set variables in ensemble
		ind.setIndividualsEnsemble(individuals);
	}

}
