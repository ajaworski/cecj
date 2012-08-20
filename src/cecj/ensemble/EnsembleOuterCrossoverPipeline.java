package cecj.ensemble;

import ec.BreedingPipeline;
import ec.EvolutionState;
import ec.Individual;
import ec.util.MersenneTwisterFast;
import ec.util.Parameter;

public class EnsembleOuterCrossoverPipeline extends BreedingPipeline {

	public static final String P_OUTER_CROSSOVER = "outer-xover";
	public static final int NUM_SOURCES = 2;

	public EnsembleOuterCrossoverPipeline() {

	}
	
	public Parameter defaultBase() {
		return EnsembleDefaults.base().push(P_OUTER_CROSSOVER);
	}

	@Override
	public int numSources() {
		return NUM_SOURCES;
	}
	
	private void swap(EnsembleIndividual a, EnsembleIndividual b, int index){
		Individual tmp = b.getIndividualsEnsemble()[index];
		b.getIndividualsEnsemble()[index] = a.getIndividualsEnsemble()[index];
		a.getIndividualsEnsemble()[index] = tmp;
	}
	
	@Override
	public int produce(int min, int max, int start, int subpopulation,
			Individual[] inds, EvolutionState state, int thread) {
		int n = sources[0].produce(min, max, start, subpopulation, inds, state, thread);
		
		if (!(sources[0] instanceof BreedingPipeline))
            for(int q=start;q<n+start;q++)
                inds[q] = (Individual)(inds[q].clone());
		
		if (!(inds[start] instanceof EnsembleIndividual))
			state.output.fatal("OuterCrossover should get EnsembleIndividuals as input");
		
		EnsembleSpecies species = (EnsembleSpecies) state.population.subpops[0].species;
		
		MersenneTwisterFast rand = state.random[thread];
		if (rand.nextDouble() < species.getOuterXoverProbability()){
			int cuttingPoint = rand.nextInt(((EnsembleIndividual)inds[start]).getIndividualsEnsemble().length - 2) + 1;
			for (int i = cuttingPoint; i < ((EnsembleIndividual)inds[start]).getIndividualsEnsemble().length; i++){
				swap((EnsembleIndividual)inds[start], (EnsembleIndividual)inds[start+1], i);
			}
		}
		
		return n;
	}

}
