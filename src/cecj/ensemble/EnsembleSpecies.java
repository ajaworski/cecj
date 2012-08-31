package cecj.ensemble;

import cecj.ntuple.NTupleIndividual;
import ec.EvolutionState;
import ec.Individual;
import ec.Species;
import ec.util.Parameter;

public class EnsembleSpecies extends Species {

	public static final String P_ENSEMBLE_SPECIES = "species";

	public static final String P_ENSEMBLE_SIZE = "ensemble-size";
	private int ensembleSize;
	
	public final static String P_INNER_MUTATION_PROB = "inner-mutation-prob";
	public final static String P_OUTER_MUTATION_PROB = "outer-mutation-prob";
	public final static String P_INNER_XOVER_PROB = "inner-xover-prob";
	public final static String P_OUTER_XOVER_PROB = "outer-xover-prob";

	public final static String P_MUTATION_STDEV = "mutation-stdev";

	public static final String P_SYSTEM = "system";

	public static final int M_WEIGHT_MUTATION = 0;
	public static final String V_WEIGHT_MUTATION = "weight";

	public static final int M_POSITION_MUTATION = 1;
	public static final String V_POSITION_MUTATION = "position";

	public static final String P_MUTATION_TYPE = "mutation-type";
	private int mutationType;
	
	private float innerMutationProbability;
	private float outerMutationProbability;
	private float innerXoverProbability;
	private float outerXoverProbability;

	private float mutationStdev;
	private EnsembleSystem ensembleSystem;

	@Override
	public void setup(EvolutionState state, Parameter base) {
		this.outerXoverProbability = state.parameters.getFloat(defaultBase().push(P_OUTER_XOVER_PROB),null,0.0);
		this.outerMutationProbability = state.parameters.getFloat(defaultBase().push(P_OUTER_MUTATION_PROB),null,0.0);
		
		this.ensembleSize = state.parameters.getIntWithDefault(defaultBase().push(P_ENSEMBLE_SIZE), null, 5);
		super.setup(state, base);
				
		ensembleSystem = new EnsembleSystem();
		ensembleSystem.setup(state, EnsembleDefaults.base().push("P_SYSTEM"));
	}
	
	@Override
	public Individual newIndividual(EvolutionState state, int thread) {
		EnsembleIndividual individual = (EnsembleIndividual) (super.newIndividual(state, thread));
		ensembleSystem.randomizeIndividual(state, thread, individual);
		return individual;
	}
	
	public Parameter defaultBase() {
		return EnsembleDefaults.base().push(P_ENSEMBLE_SPECIES);
	}


	public int getEnsembleSize() {
		return ensembleSize;
	}

	public void setEnsembleSize(int ensembleSize) {
		this.ensembleSize = ensembleSize;
	}

	public float getInnerMutationProbability() {
		return innerMutationProbability;
	}


	public void setInnerMutationProbability(float innerMutationProbability) {
		this.innerMutationProbability = innerMutationProbability;
	}


	public float getOuterMutationProbability() {
		return outerMutationProbability;
	}


	public void setOuterMutationProbability(float outerMutationProbability) {
		this.outerMutationProbability = outerMutationProbability;
	}


	public float getInnerXoverProbability() {
		return innerXoverProbability;
	}


	public void setInnerXoverProbability(float innerXoverProbability) {
		this.innerXoverProbability = innerXoverProbability;
	}


	public float getOuterXoverProbability() {
		return outerXoverProbability;
	}


	public void setOuterXoverProbability(float outerXoverProbability) {
		this.outerXoverProbability = outerXoverProbability;
	}

}
