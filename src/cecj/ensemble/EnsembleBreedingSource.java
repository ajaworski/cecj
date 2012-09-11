package cecj.ensemble;

import ec.BreedingSource;
import ec.EvolutionState;
import ec.Individual;
import ec.Population;
import ec.util.Parameter;

public class EnsembleBreedingSource extends BreedingSource{

	public static final String P_ENS_BSOURCE = "ensemble-breeding-source";
	
	private EnsembleIndividual ensembleIndividual = null;
	private int currentIndex;
	
	public EnsembleBreedingSource() {
		currentIndex = 0;
	}
	
	@Override
	public int typicalIndsProduced() {
		return 1;
	}

	@Override
	public boolean produces(EvolutionState state, Population newpop,
			int subpopulation, int thread) {
		// TODO Check if that's OK
		return true;
	}

	@Override
	public void prepareToProduce(EvolutionState state, int subpopulation,
			int thread) {
		currentIndex = 0;
		
	}

	@Override
	public void finishProducing(EvolutionState s, int subpopulation, int thread) {

		
	}

	@Override
	public int produce(int min, int max, int start, int subpopulation,
			Individual[] inds, EvolutionState state, int thread) {
		if (ensembleIndividual == null)
			state.output.fatal("No EnsembleIndividual set for EnsembleBreedingSource");
		
		int n = typicalIndsProduced();
		if (n < min)
			n = min;
		if (n > max)
			n = max;
		
		for (int i = start; i < start + n; i++){
			inds[i] = ensembleIndividual.getIndividualsEnsemble()[currentIndex];
			currentIndex = (currentIndex + 1) % ensembleIndividual.getIndividualsEnsemble().length;
		}
		
		return n;
	}

	public Parameter defaultBase() {
		return EnsembleDefaults.base().push(P_ENS_BSOURCE);
	}

	public EnsembleIndividual getEnsembleIndividual() {
		return ensembleIndividual;
	}

	public void setEnsembleIndividual(EnsembleIndividual ensembleIndividual) {
		this.currentIndex = 0;
		this.ensembleIndividual = ensembleIndividual;
	}

}
