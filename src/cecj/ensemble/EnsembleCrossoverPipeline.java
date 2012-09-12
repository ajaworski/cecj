package cecj.ensemble;

import java.util.Arrays;

import ec.BreedingPipeline;
import ec.BreedingSource;
import ec.EvolutionState;
import ec.Individual;
import ec.util.MersenneTwisterFast;
import ec.util.Parameter;

public class EnsembleCrossoverPipeline extends BreedingPipeline {

	public static final String P_OUTER_CROSSOVER = "outer-xover";
	public static final int NUM_SOURCES = 2;

	private EnsembleIndividual[] parents;
	private EnsembleBreedingSource[] ensembleBreedingSources = null;
	private BreedingPipeline innerXoverPipeline = null;
	private Individual[] innerXoveredInds = null;
	
	public EnsembleCrossoverPipeline() {
		parents = new EnsembleIndividual[2];
	}
	
	public Parameter defaultBase() {
		return EnsembleDefaults.base().push(P_OUTER_CROSSOVER);
	}

	@Override
	public int numSources() {
		return NUM_SOURCES;
	}
	
	private void crossover(EnsembleIndividual a, EnsembleIndividual b, int cuttingPoint){
		for (int i = cuttingPoint; i < a.getIndividualsEnsemble().length; i++){
			Individual tmp = b.getIndividualsEnsemble()[i];
			b.getIndividualsEnsemble()[i] = a.getIndividualsEnsemble()[i];
			a.getIndividualsEnsemble()[i] = tmp;
		}
	}
	
	@Override
	public int produce(int min, int max, int start, int subpopulation,
			Individual[] inds, EvolutionState state, int thread) {
		int n = typicalIndsProduced();
		if (n < min)
			n = min;
		if (n > max)
			n = max;
		
		for (int i = start; i < start + n; i += 2){
			sources[0].produce(1, 1, 0, subpopulation, parents, state, thread);
			sources[1].produce(1, 1, 1, subpopulation, parents, state, thread);
			
			if (!(sources[0] instanceof BreedingPipeline)){
	                parents[0] = (EnsembleIndividual)(parents[0].clone());
			}
			if (!(sources[1] instanceof BreedingPipeline)){
	            parents[1] = (EnsembleIndividual)(parents[1].clone());
			}
			
			if (!(parents[0] instanceof EnsembleIndividual) || !(parents[1] instanceof EnsembleIndividual))
				state.output.fatal("OuterCrossover should get EnsembleIndividuals as input");
			
			if (!(state.population.subpops[subpopulation].species instanceof EnsembleSpecies))
				state.output.fatal("OuterCrossover should work on EnsembleSpecies");
			
			EnsembleSpecies species = (EnsembleSpecies) state.population.subpops[0].species;
			
			MersenneTwisterFast rand = state.random[thread];
			if (rand.nextBoolean(species.getOuterXoverProbability())){
				int cuttingPoint = rand.nextInt(((EnsembleIndividual)parents[0]).getIndividualsEnsemble().length - 2) + 1;
				crossover(parents[0], parents[1], cuttingPoint);
			}
			
			//Inner crossover
			if (species.getInnerXoverProbability() > 0.0){
				//Proxy, done only once
				if (ensembleBreedingSources == null){
					ensembleBreedingSources = new EnsembleBreedingSource[2];
					ensembleBreedingSources[0] = new EnsembleBreedingSource();
					ensembleBreedingSources[1] = new EnsembleBreedingSource();
					
					try{
						innerXoverPipeline = (BreedingPipeline) Class.forName(species.getInnerXoverClass()).newInstance();
						innerXoverPipeline.likelihood = (float) 1.0;
						innerXoverPipeline.sources = ensembleBreedingSources;
					} catch (Exception e){
						throw new IllegalArgumentException(e.getMessage());
					}
					
					innerXoveredInds = new Individual[2];
				}
				
				ensembleBreedingSources[0].setEnsembleIndividual(parents[0]);
				ensembleBreedingSources[1].setEnsembleIndividual(parents[1]);
				for (int j = 0; j < ((EnsembleIndividual)parents[0]).getIndividualsEnsemble().length; j++){
					if (rand.nextBoolean(species.getInnerXoverProbability())){
						innerXoverPipeline.produce(2, 2, 0, subpopulation, innerXoveredInds, state, thread);
						parents[0].getIndividualsEnsemble()[j] = (Individual) innerXoveredInds[0];
						parents[1].getIndividualsEnsemble()[j] = (Individual) innerXoveredInds[1];
					}
				}
			}
	
			parents[0].evaluated = false;
			parents[1].evaluated = false;
			
			inds[i] = parents[0];
			if (i + 1 < n + start){
				inds[i + 1] = parents[1];
			}
		}
		
		return n;
	}

}
