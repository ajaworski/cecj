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
	
	public final static String P_SWAP_LIKE = "swap-likelihood";
	public final static String P_GROUP_LIKE = "group-change-likelihood";
	public final static String P_GROUP_PROB = "group-change-prob";
	public final static String P_GROUP_MAX = "max-group-change";
	public final static String P_BOUND_LIKE = "bound-change-likelihood";
	public final static String P_BOUND_PROB = "bound-change-prob";
	public final static String P_BOUND_MAX = "max-bound-change";
	
	public final static String P_PROB = "prob";
	public final static String P_LIKE = "likelihood";
	public final static String P_STDEV = "stdev";
	public final static String P_CLASS = "class";

	public static final String P_SYSTEM = "system";
	
	public static final String P_BREED_ELITE = "breed-elite";
	
	private int ensembleSize;
	private int boundariesCount;

	private float innerXoverLikelihood;
	private float innerMutationLikelihood;
	
	private String innerMutationClass;
	private String innerXoverClass;
	
	private float outerXoverLikelihood;
	private float outerXoverProbability;
	
	private float outerMutationSwapLikelihood;
	private float outerMutationBoundariesChangeLikelihood;
	private float outerMutationBoundariesChangeProbability;
	private int outerMutationMaxBoundaryChange;
	private float outerMutationGroupsChangeLikelihood;
	private float outerMutationGroupsChangeProbability;
	private int outerMutationMaxGroupChange;
	
	private EnsembleSystem ensembleSystem;
	
	private boolean breedElite;

	@Override
	public void setup(EvolutionState state, Parameter base) {
		this.innerXoverLikelihood = state.parameters.getFloatWithDefault(defaultBase().push(P_XOVER).push(P_INNER).push(P_LIKE),null,1.0);
		if (this.innerXoverLikelihood < 0 || this.innerXoverLikelihood > 1)
			state.output.error("Inner xover likelihood should be in [0;1] range");
		
		this.innerMutationLikelihood = state.parameters.getFloatWithDefault(defaultBase().push(P_MUTATION).push(P_INNER).push(P_LIKE),null,1.0);
		if (this.innerMutationLikelihood < 0 || this.innerMutationLikelihood > 1)
			state.output.error("Inner mutation likelihood should be in [0;1] range");
		
		
		if (innerMutationLikelihood > 0.0){
			this.innerMutationClass = state.parameters.getString(defaultBase().push(P_MUTATION).push(P_INNER).push(P_CLASS),null);
			if (this.innerMutationClass == null)
				state.output.fatal("Must specify class for inner mutation");
			try{
				BreedingPipeline bp = (BreedingPipeline) Class.forName(this.innerMutationClass).newInstance();
			} catch (Exception e){
				throw new IllegalArgumentException(e.getMessage());
			}
		}
		
		if (innerXoverLikelihood > 0.0){
			this.innerXoverClass = state.parameters.getString(defaultBase().push(P_XOVER).push(P_INNER).push(P_CLASS),null);
			if (this.innerXoverClass == null)
				state.output.fatal("Must specify class for inner xover");
			try{
				BreedingPipeline bp = (BreedingPipeline) Class.forName(this.innerXoverClass).newInstance();
			} catch (Exception e){
				throw new IllegalArgumentException(e.getMessage());
			}
		}
		
		this.outerXoverLikelihood = state.parameters.getFloatWithDefault(defaultBase().push(P_XOVER).push(P_OUTER).push(P_LIKE),null,1.0);
		if (this.outerXoverLikelihood < 0 || this.outerXoverLikelihood > 1)
			state.output.error("Outer xover likelihood should be in [0;1] range");
		
		this.outerXoverProbability = state.parameters.getFloatWithDefault(defaultBase().push(P_XOVER).push(P_OUTER).push(P_PROB),null,0.5);
		if (this.outerXoverLikelihood < 0 || this.outerXoverLikelihood > 1)
			state.output.error("Outer xover probability should be in [0;1] range");
		
		this.outerMutationSwapLikelihood = state.parameters.getFloatWithDefault(defaultBase().push(P_MUTATION).push(P_OUTER).push(P_SWAP_LIKE),null,0.1);
		if (this.outerMutationSwapLikelihood < 0 || this.outerMutationSwapLikelihood > 1)
			state.output.error("Outer mutation swap likelihood should be in [0;1] range");
		
		this.outerMutationBoundariesChangeLikelihood = state.parameters.getFloatWithDefault(defaultBase().push(P_MUTATION).push(P_OUTER).push(P_BOUND_LIKE),null,1.0);
		if (this.outerMutationBoundariesChangeLikelihood < 0 || this.outerMutationBoundariesChangeLikelihood > 1)
			state.output.error("Outer mutation boundaries change likelihood should be in [0;1] range");
		
		this.outerMutationBoundariesChangeProbability = state.parameters.getFloatWithDefault(defaultBase().push(P_MUTATION).push(P_OUTER).push(P_BOUND_PROB),null,0.1);
		if (this.outerMutationBoundariesChangeProbability < 0 || this.outerMutationBoundariesChangeProbability > 1)
			state.output.error("Outer mutation boundaries change probability should be in [0;1] range");
		
		this.outerMutationMaxBoundaryChange = state.parameters.getInt(defaultBase().push(P_MUTATION).push(P_OUTER).push(P_BOUND_MAX),null,1);
		this.outerMutationMaxBoundaryChange = (int) Math.max(this.outerMutationMaxBoundaryChange, 1);
		
		this.outerMutationGroupsChangeLikelihood = state.parameters.getFloatWithDefault(defaultBase().push(P_MUTATION).push(P_OUTER).push(P_GROUP_LIKE),null,1.0);
		if (this.outerMutationGroupsChangeLikelihood < 0 || this.outerMutationGroupsChangeLikelihood > 1)
			state.output.error("Outer mutation groups change likelihood should be in [0;1] range");
		
		this.outerMutationGroupsChangeProbability = state.parameters.getFloatWithDefault(defaultBase().push(P_MUTATION).push(P_OUTER).push(P_GROUP_PROB),null,0.1);
		if (this.outerMutationGroupsChangeProbability < 0 || this.outerMutationGroupsChangeProbability > 1)
			state.output.error("Outer mutation groups change probability should be in [0;1] range");
		
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
		
		this.breedElite = state.parameters.getBoolean(defaultBase().push(P_BREED_ELITE), null, false);
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

	public float getInnerXoverLikelihood() {
		return innerXoverLikelihood;
	}

	public void setInnerXoverLikelihood(float innerXoverLikelihood) {
		this.innerXoverLikelihood = innerXoverLikelihood;
	}

	public float getInnerMutationLikelihood() {
		return innerMutationLikelihood;
	}

	public void setInnerMutationLikelihood(float innerMutationLikelihood) {
		this.innerMutationLikelihood = innerMutationLikelihood;
	}

	public float getOuterXoverLikelihood() {
		return outerXoverLikelihood;
	}

	public void setOuterXoverLikelihood(float outerXoverLikelihood) {
		this.outerXoverLikelihood = outerXoverLikelihood;
	}

	public float getOuterXoverProbability() {
		return outerXoverProbability;
	}

	public void setOuterXoverProbability(float outerXoverProbability) {
		this.outerXoverProbability = outerXoverProbability;
	}

	public float getOuterMutationSwapLikelihood() {
		return outerMutationSwapLikelihood;
	}

	public void setOuterMutationSwapLikelihood(float outerMutationSwapLikelihood) {
		this.outerMutationSwapLikelihood = outerMutationSwapLikelihood;
	}

	public float getOuterMutationBoundariesChangeLikelihood() {
		return outerMutationBoundariesChangeLikelihood;
	}

	public void setOuterMutationBoundariesChangeLikelihood(
			float outerMutationBoundariesChangeLikelihood) {
		this.outerMutationBoundariesChangeLikelihood = outerMutationBoundariesChangeLikelihood;
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

	public float getOuterMutationGroupsChangeLikelihood() {
		return outerMutationGroupsChangeLikelihood;
	}

	public void setOuterMutationGroupsChangeLikelihood(
			float outerMutationGroupsChangeLikelihood) {
		this.outerMutationGroupsChangeLikelihood = outerMutationGroupsChangeLikelihood;
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

	public boolean isBreedElite() {
		return breedElite;
	}

	public void setBreedElite(boolean breedElite) {
		this.breedElite = breedElite;
	}


}
