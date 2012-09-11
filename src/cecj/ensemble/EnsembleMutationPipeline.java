package cecj.ensemble;

import ec.BreedingPipeline;
import ec.BreedingSource;
import ec.EvolutionState;
import ec.Individual;
import ec.util.MersenneTwisterFast;
import ec.util.Parameter;

public class EnsembleMutationPipeline extends BreedingPipeline {
	private BreedingPipeline innerMutationPipeline = null;
	private EnsembleBreedingSource ensembleBreedingSource = null;
	private Individual[] innerMutatedInds = null;
	
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
		
		if (!(state.population.subpops[subpopulation].species instanceof EnsembleSpecies))
			state.output.fatal("OuterCrossover should work on EnsembleSpecies");
		
		EnsembleSpecies species = (EnsembleSpecies) state.population.subpops[subpopulation].species;
		
		MersenneTwisterFast rand = state.random[thread];
		if (rand.nextBoolean(species.getOuterMutationSwapProbability())){
			int x = 0, y = 0;
			do {
				x = rand.nextInt(((EnsembleIndividual)inds[start]).getIndividualsEnsemble().length);
				y = rand.nextInt(((EnsembleIndividual)inds[start]).getIndividualsEnsemble().length);
			} while (x == y);
			Individual tmp = ((EnsembleIndividual)inds[start]).getIndividualsEnsemble()[y];
			((EnsembleIndividual)inds[start]).getIndividualsEnsemble()[y] = ((EnsembleIndividual)inds[start]).getIndividualsEnsemble()[x];
			((EnsembleIndividual)inds[start]).getIndividualsEnsemble()[x] = tmp;
		}
		
		if (rand.nextBoolean(species.getOuterMutationBoundariesChangeProbability())){
			boolean out;
			int index;
			int ammount;
			boolean increase;
			do{
				out = true;
				index = rand.nextInt(((EnsembleIndividual)inds[start]).getBoundaries().length);
				increase = rand.nextBoolean();
				ammount = rand.nextInt(species.getOuterMutationMaxBoundaryChange()) + 1;
				//increase == true means increasing the value, decreasing otherwise
				if (increase){
					if (index == 0){ //XXX hardcoded!!!
						if (((EnsembleIndividual)inds[start]).getBoundaries()[index] >= 62)
							out = false;
					} else if (((EnsembleIndividual)inds[start]).getBoundaries()[index] + ammount >= ((EnsembleIndividual)inds[start]).getBoundaries()[index - 1]){
						out = false;
					}
				} else {
					if (index == ((EnsembleIndividual)inds[start]).getBoundaries().length - 1){ //XXX hardcoded!!!
						if (((EnsembleIndividual)inds[start]).getBoundaries()[index] <= ammount)
							out = false;
					} else if (((EnsembleIndividual)inds[start]).getBoundaries()[index] - ammount <= ((EnsembleIndividual)inds[start]).getBoundaries()[index + 1]){
						out = false;
					}										
				}
			} while (!out);
			if (increase){
				((EnsembleIndividual)inds[start]).getBoundaries()[index] += ammount;
			} else {
				((EnsembleIndividual)inds[start]).getBoundaries()[index] -= ammount;
			}
		}
		
		//Inner mutation
		if (species.getInnerMutationProbability() > 0.0){
			if (ensembleBreedingSource == null)
				ensembleBreedingSource = new EnsembleBreedingSource();
			
			if (innerMutationPipeline == null){
				try{
					innerMutationPipeline = (BreedingPipeline) Class.forName(species.getInnerMutationClass()).newInstance();
					innerMutationPipeline.likelihood = (float) 1.0;
					innerMutationPipeline.sources = new BreedingSource[1];
					innerMutationPipeline.sources[0] = ensembleBreedingSource;
				} catch (Exception e){
					throw new IllegalArgumentException(e.getMessage());
				}
			}
			
			if (innerMutatedInds == null){
				innerMutatedInds = new Individual[1];
			}
			
			ensembleBreedingSource.setEnsembleIndividual((EnsembleIndividual)inds[start]);
			for (int i = 0; i < ((EnsembleIndividual)inds[start]).getIndividualsEnsemble().length; i++){
				if (rand.nextBoolean(species.getInnerMutationProbability())){
					innerMutationPipeline.produce(1, 1, 0, subpopulation, innerMutatedInds, state, thread);
					((EnsembleIndividual)inds[start]).getIndividualsEnsemble()[i] = (Individual) innerMutatedInds[0].clone();
				}
			}
		}
		inds[start].evaluated = false;		
		return n;
	}

}
