package cecj.ensemble;

import ec.BreedingPipeline;
import ec.EvolutionState;
import ec.Fitness;
import ec.Individual;
import ec.Species;
import ec.util.Parameter;

public class EnsembleSpecies extends Species {

	public static final String P_ENSEMBLE_SPECIES = "species";

	public static final String P_ENSEMBLE_SIZE = "ensemble-size";
	public static final String P_BOUNDARIES_COUNT = "boundaries-count";
	
	public final static String P_MUTATION = "mutation";
	public final static String P_XOVER = "xover";
	
	public final static String P_INNER = "inner";
	public final static String P_OUTER = "outer";
	
	public final static String P_SWAP_PROB = "swap-prob";
	public final static String P_GROUP_PROB = "group-change-prob";
	public final static String P_GROUP_MAX = "max-group-change";
	public final static String P_BOUND_PROB = "bound-change-prob";
	public final static String P_BOUND_MAX = "max-bound-change";
	
	public final static String P_PROB = "prob";
	public final static String P_STDEV = "stdev";
	public final static String P_CLASS = "class";

	public static final String P_SYSTEM = "system";
	
	private int ensembleSize;
	private int boundariesCount;

	private float innerXoverProbability;
	private float innerMutationProbability;
	
	private String innerMutationClass;
	private String innerXoverClass;
	
	private float outerXoverProbability;
	
	private float outerMutationSwapProbability;
	private float outerMutationBoundariesChangeProbability;
	private int outerMutationMaxBoundaryChange;
	private float outerMutationGroupsChangeProbability;
	private int outerMutationMaxGroupChange;
	
	private EnsembleSystem ensembleSystem;

	@Override
	public void setup(EvolutionState state, Parameter base) {
		this.innerXoverProbability = state.parameters.getFloatWithMax(defaultBase().push(P_XOVER).push(P_INNER).push(P_PROB),null,0.0,1.0);
		this.innerXoverProbability = (float) Math.max(this.innerXoverProbability, 0.0);
		this.innerMutationProbability = state.parameters.getFloatWithMax(defaultBase().push(P_MUTATION).push(P_INNER).push(P_PROB),null,0.0,1.0);
		this.innerMutationProbability = (float) Math.max(this.innerMutationProbability, 0.0);
		
		if (innerMutationProbability > 0.0){
			this.innerMutationClass = state.parameters.getString(defaultBase().push(P_MUTATION).push(P_INNER).push(P_CLASS),null);
			if (this.innerMutationClass == null)
				state.output.fatal("Must specify class for inner mutation");
			try{
				BreedingPipeline bp = (BreedingPipeline) Class.forName(this.innerMutationClass).newInstance();
			} catch (Exception e){
				throw new IllegalArgumentException(e.getMessage());
			}
		}
		
		if (innerXoverProbability > 0.0){
			this.innerXoverClass = state.parameters.getString(defaultBase().push(P_XOVER).push(P_INNER).push(P_CLASS),null);
			if (this.innerXoverClass == null)
				state.output.fatal("Must specify class for inner xover");
			try{
				BreedingPipeline bp = (BreedingPipeline) Class.forName(this.innerXoverClass).newInstance();
			} catch (Exception e){
				throw new IllegalArgumentException(e.getMessage());
			}
		}
		
		this.outerXoverProbability = state.parameters.getFloatWithMax(defaultBase().push(P_XOVER).push(P_OUTER).push(P_PROB),null,0.0,1.0);
		this.outerXoverProbability = (float) Math.max(this.outerXoverProbability, 0.0);
		this.outerMutationSwapProbability = state.parameters.getFloatWithMax(defaultBase().push(P_MUTATION).push(P_OUTER).push(P_SWAP_PROB),null,0.0,1.0);
		this.outerMutationSwapProbability = (float) Math.max(this.outerMutationSwapProbability, 0.0);
		this.outerMutationBoundariesChangeProbability = state.parameters.getFloatWithMax(defaultBase().push(P_MUTATION).push(P_OUTER).push(P_BOUND_PROB),null,0.0,1.0);
		this.outerMutationBoundariesChangeProbability = (float) Math.max(this.outerMutationBoundariesChangeProbability, 0.0);
		this.outerMutationMaxBoundaryChange = state.parameters.getInt(defaultBase().push(P_MUTATION).push(P_OUTER).push(P_BOUND_MAX),null,1);
		this.outerMutationMaxBoundaryChange = (int) Math.max(this.outerMutationMaxBoundaryChange, 1);
		this.outerMutationGroupsChangeProbability = state.parameters.getFloatWithMax(defaultBase().push(P_MUTATION).push(P_OUTER).push(P_GROUP_PROB),null,0.0,1.0);
		this.outerMutationGroupsChangeProbability = (float) Math.max(this.outerMutationGroupsChangeProbability, 0.0);
		this.outerMutationMaxGroupChange = state.parameters.getInt(defaultBase().push(P_MUTATION).push(P_OUTER).push(P_GROUP_MAX),null,1);
		this.outerMutationMaxGroupChange = (int) Math.max(this.outerMutationMaxGroupChange, 1);
		
		this.ensembleSize = state.parameters.getIntWithDefault(defaultBase().push(P_ENSEMBLE_SIZE), null, 2);
		if (this.ensembleSize < 2){
			state.output.error("Must have at least 2 individuals in an ensemble");
		}
		
		this.boundariesCount = state.parameters.getIntWithDefault(defaultBase().push(P_BOUNDARIES_COUNT), null, (this.ensembleSize - 1));
		this.boundariesCount = (int) Math.max(this.boundariesCount, 1);
		this.boundariesCount = (int) Math.min(this.boundariesCount, this.ensembleSize - 1);
		super.setup(state, base);
				
		ensembleSystem = new EnsembleSystem();
		ensembleSystem.setup(state, EnsembleDefaults.base().push("P_SYSTEM"));
	}
	
	@Override
	public Individual newIndividual(EvolutionState state, int thread) {
		EnsembleIndividual individual = (EnsembleIndividual) (super.newIndividual(state, thread));
		for (Individual i : individual.getIndividualsEnsemble()){
			i.fitness = (Fitness) (f_prototype.clone());
		}
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

	public int getBoundariesCount() {
		return boundariesCount;
	}

	public void setBoundariesCount(int boundariesCount) {
		this.boundariesCount = boundariesCount;
	}

	public float getInnerXoverProbability() {
		return innerXoverProbability;
	}

	public void setInnerXoverProbability(float innerXoverProbability) {
		this.innerXoverProbability = innerXoverProbability;
	}

	public float getInnerMutationProbability() {
		return innerMutationProbability;
	}

	public void setInnerMutationProbability(float innerMutationProbability) {
		this.innerMutationProbability = innerMutationProbability;
	}

	public float getOuterXoverProbability() {
		return outerXoverProbability;
	}

	public void setOuterXoverProbability(float outerXoverProbability) {
		this.outerXoverProbability = outerXoverProbability;
	}

	public float getOuterMutationSwapProbability() {
		return outerMutationSwapProbability;
	}

	public void setOuterMutationSwapProbability(float outerMutationSwapProbability) {
		this.outerMutationSwapProbability = outerMutationSwapProbability;
	}

	public float getOuterMutationBoundariesChangeProbability() {
		return outerMutationBoundariesChangeProbability;
	}

	public void setOuterMutationBoundariesChangeProbability(
			float outerMutationBoundariesChangeProbability) {
		this.outerMutationBoundariesChangeProbability = outerMutationBoundariesChangeProbability;
	}

	public int getOuterMutationMaxBoundaryChange() {
		return outerMutationMaxBoundaryChange;
	}

	public void setOuterMutationMaxBoundaryChange(
			int outerMutationMaxBoundaryChange) {
		this.outerMutationMaxBoundaryChange = outerMutationMaxBoundaryChange;
	}

	public float getOuterMutationGroupsChangeProbability() {
		return outerMutationGroupsChangeProbability;
	}

	public void setOuterMutationGroupsChangeProbability(
			float outerMutationGroupsChangeProbability) {
		this.outerMutationGroupsChangeProbability = outerMutationGroupsChangeProbability;
	}

	public int getOuterMutationMaxGroupChange() {
		return outerMutationMaxGroupChange;
	}

	public void setOuterMutationMaxGroupChange(int outerMutationMaxGroupChange) {
		this.outerMutationMaxGroupChange = outerMutationMaxGroupChange;
	}

	public String getInnerMutationClass() {
		return innerMutationClass;
	}

	public void setInnerMutationClass(String innerMutationClass) {
		this.innerMutationClass = innerMutationClass;
	}

	public String getInnerXoverClass() {
		return innerXoverClass;
	}

	public void setInnerXoverClass(String innerXoverClass) {
		this.innerXoverClass = innerXoverClass;
	}


}
