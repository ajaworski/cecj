package cecj.ensemble;

import cecj.ntuple.NTupleDefaults;
import cecj.ntuple.NTupleSystem;
import ec.Species;
import ec.util.Parameter;

public class EnsembleSpecies extends Species {

	public static final String P_ENSEMBLE_SPECIES = "species";

	public final static String P_MUTATION_PROB = "mutation-prob";
	public final static String P_CROSSOVER_PROB = "crossover-prob";

	public final static String P_MUTATION_STDEV = "mutation-stdev";

	public static final String P_SYSTEM = "system";

	public static final int M_WEIGHT_MUTATION = 0;
	public static final String V_WEIGHT_MUTATION = "weight";

	public static final int M_POSITION_MUTATION = 1;
	public static final String V_POSITION_MUTATION = "position";

	public static final String P_MUTATION_TYPE = "mutation-type";
	
	private int mutationType;
	
	private float mutationProbability;
	private float crossoverProbability;

	private float mutationStdev;
	private EnsembleSystem ensembleSystem;

	
	public Parameter defaultBase() {
		return NTupleDefaults.base().push(P_ENSEMBLE_SPECIES);
	}

}
