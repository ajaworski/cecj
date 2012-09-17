package cecj.ensemble;

import ec.EvolutionState;
import ec.Individual;
import ec.Species;
import ec.util.Code;
import ec.util.Parameter;
import ec.vector.DoubleVectorIndividual;
import ec.vector.FloatVectorSpecies;
import games.player.EvolvedPlayer;

public class EnsembleIndividual extends Individual {
	
	public static final String P_ENSEMBLE_INDIVIDUAL = "ensemble-ind";
	
	public static final String P_SUBSPECIES = "subspecies";

	private Individual[] individualsEnsemble;
	private Integer[] boundaries;
	private Integer[] groups;

	public Individual[] getIndividualsEnsemble() {
		return individualsEnsemble;
	}

	public void setIndividualsEnsemble(Individual[] individualsEnsemble) {
		this.individualsEnsemble = individualsEnsemble;
	}

	public Integer[] getBoundaries() {
		return boundaries;
	}

	public void setBoundaries(Integer[] boundaries) {
		this.boundaries = boundaries;
	}

	public Integer[] getGroups() {
		return groups;
	}

	public void setGroups(Integer[] groups) {
		this.groups = groups;
	}

	/**
	 * This method is called only once - on a prototype individual stored in the species class.
	 */
	@Override
	public void setup(EvolutionState state, Parameter base) {
		super.setup(state, base);

		if (!(species instanceof EnsembleSpecies)) {
			state.output.fatal("EnsembleIndividual requires a EnsembleSpecies", base, defaultBase());
		}
		
		Species subspecies = (Species) state.parameters.getInstanceForParameter(base.pop().push(P_SUBSPECIES), null, Species.class);
		subspecies.setup(state, base.pop().push(P_SUBSPECIES));
		
		int ensembleSize = ((EnsembleSpecies)species).getEnsembleSize();
		int boundariesCount = ((EnsembleSpecies)species).getBoundariesCount();
		
		this.individualsEnsemble = new Individual[ensembleSize];
		this.boundaries = new Integer[boundariesCount];
		this.groups = new Integer[boundariesCount];
		for (int i = 0; i < ensembleSize; i++){
			this.individualsEnsemble[i] = new DoubleVectorIndividual();
			this.individualsEnsemble[i].species = subspecies;			
			this.individualsEnsemble[i].setup(state, base);
			
			if (i < boundariesCount){
				this.boundaries[i] = i;
				this.groups[i] = i;
			}
		}
		
	}
	
	@Override
	public boolean equals(Object ind) {
		if (!(ind instanceof EnsembleIndividual)) {
			return false;
		}
		
		EnsembleIndividual ensemble = (EnsembleIndividual) ind;
		
		if (ensemble.individualsEnsemble.length != individualsEnsemble.length)
			return false;
		
		for (int i = 0; i < individualsEnsemble.length; i++){
			if (!ensemble.individualsEnsemble[i].equals(individualsEnsemble[i]))
				return false;
		}
		
		return true;
	}
	
	@Override
	public Object clone() {
		EnsembleIndividual clone = (EnsembleIndividual) (super.clone());
		
		if (individualsEnsemble != null){
			clone.individualsEnsemble = new Individual[individualsEnsemble.length];
			for (int i = 0; i < individualsEnsemble.length; i++)
				clone.individualsEnsemble[i] = (Individual) individualsEnsemble[i].clone();
			clone.boundaries = new Integer[boundaries.length];
			for (int i = 0; i < boundaries.length; i++)
				clone.boundaries[i] = new Integer(boundaries[i]);
			clone.groups = new Integer[groups.length];
			for (int i = 0; i < groups.length; i++)
				clone.groups[i] = new Integer(groups[i]);
		}
		
		return clone;
	}

	@Override
	public int hashCode() {
		return individualsEnsemble.hashCode();
	}

	public Parameter defaultBase() {
		 return EnsembleDefaults.base().push(P_ENSEMBLE_INDIVIDUAL);
	}
	
	@Override
	public void printIndividual(EvolutionState state, int log) {
		state.output.println(EVALUATED_PREAMBLE + Code.encode(evaluated), log);
        fitness.printFitness(state,log);
        for (Individual ind : individualsEnsemble){
        	state.output.println( ind.genotypeToString(), log );
        }
	}
	
}
