package cecj.ensemble;

import ec.BreedingPipeline;
import ec.EvolutionState;
import ec.Individual;
import ec.util.MersenneTwisterFast;
import ec.util.Parameter;

public class EnsembleOuterMutationPipeline extends BreedingPipeline {

	public static final String P_MUTATION = "mutate";
	public static final int NUM_SOURCES = 1;

	public Parameter defaultBase() {
		return EnsembleDefaults.base().push(P_MUTATION);
	}

	@Override
	public int numSources() {
		return NUM_SOURCES;
	}

	@Override
	public int produce(int min, int max, int start, int subpopulation,
			Individual[] inds, EvolutionState state, int thread) {
		int n = sources[0].produce(min, max, start, subpopulation, inds, state, thread);
		
		if (!(sources[0] instanceof BreedingPipeline))
            for(int q=start;q<n+start;q++)
                inds[q] = (Individual)(inds[q].clone());
		
		if (!(inds[start] instanceof EnsembleIndividual))
			state.output.fatal("OuterMutation should get EnsembleIndividuals as input");
		
		EnsembleSpecies species = (EnsembleSpecies) inds[start].species;
		
		MersenneTwisterFast rand = state.random[thread];
		if (rand.nextDouble() < species.getOuterMutationSwapProbability()){
			int x = 0, y = 0;
			do {
				x = rand.nextInt(((EnsembleIndividual)inds[start]).getIndividualsEnsemble().length);
				y = rand.nextInt(((EnsembleIndividual)inds[start]).getIndividualsEnsemble().length);
			} while (x == y);
			Individual tmp = ((EnsembleIndividual)inds[start]).getIndividualsEnsemble()[y];
			((EnsembleIndividual)inds[start]).getIndividualsEnsemble()[y] = ((EnsembleIndividual)inds[start]).getIndividualsEnsemble()[x];
			((EnsembleIndividual)inds[start]).getIndividualsEnsemble()[x] = tmp;
		}
		
		if (rand.nextDouble() < species.getOuterMutationBoundariesChangeProbability()){
			//TODO implementacja
		}
		
		return n;
	}

}
