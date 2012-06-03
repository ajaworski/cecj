package cecj.ensemble;

import ec.BreedingPipeline;
import ec.EvolutionState;
import ec.Individual;
import ec.util.Parameter;

public class EnsembleOuterCrossoverPipeline extends BreedingPipeline {

	public static final String P_OUTER_CROSSOVER = "outer-xover";
	public static final int NUM_SOURCES = 2;

	private EnsembleIndividual parents[];
	
	public EnsembleOuterCrossoverPipeline() {
		parents = new EnsembleIndividual[2];
	}
	
	public Parameter defaultBase() {
		return EnsembleDefaults.base().push(P_OUTER_CROSSOVER);
	}

	@Override
	public int numSources() {
		return NUM_SOURCES;
	}

	@Override
	public int produce(int min, int max, int start, int subpopulation,
			Individual[] inds, EvolutionState state, int thread) {
		// XXX implementacja zewnetrznego krzyzowania
		return 0;
	}

}
