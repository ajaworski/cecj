package cecj.ensemble;

import cecj.ntuple.NTupleDefaults;
import cecj.ntuple.NTupleIndividual;
import ec.BreedingPipeline;
import ec.EvolutionState;
import ec.Individual;
import ec.util.Parameter;

public class EnsembleCrossoverPipeline extends BreedingPipeline {

	public static final String P_CROSSOVER = "xover";
	public static final int NUM_SOURCES = 2;

	private EnsembleIndividual parents[];
	
	public EnsembleCrossoverPipeline() {
		parents = new EnsembleIndividual[2];
	}
	
	public Parameter defaultBase() {
		return EnsembleDefaults.base().push(P_CROSSOVER);
	}

	@Override
	public int numSources() {
		return NUM_SOURCES;
	}

	@Override
	public int produce(int min, int max, int start, int subpopulation,
			Individual[] inds, EvolutionState state, int thread) {
		// XXX implementacja krzyzowania
		return 0;
	}

}
