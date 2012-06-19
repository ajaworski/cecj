package cecj.ensemble;

import ec.Species;
import ec.util.Parameter;

public class EnsembleSpecies extends Species {

	public static final String P_ENSEMBLE_SPECIES = "species";

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

	
	public Parameter defaultBase() {
		return EnsembleDefaults.base().push(P_ENSEMBLE_SPECIES);
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
