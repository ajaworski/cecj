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
		//TODO parameters
				
//				this.individualsEnsemble = new Individual[3];
//				for (int i = 0; i < 3; i++){
//					this.individualsEnsemble[i] = new DoubleVectorIndividual();
//					this.individualsEnsemble[i].species = fvSpecies;
//					this.individualsEnsemble[i].setup(state, base);
//					((DoubleVectorIndividual)(this.individualsEnsemble[i])).reset(state, 0);
//				}
		
		//TODO add parameters
		//Create individuals in ensemble
		
		for (int i = 0; i < ind.getIndividualsEnsemble().length; i++){
			((DoubleVectorIndividual)(ind.getIndividualsEnsemble()[i])).reset(state, thread);
		}
		
	}

}
