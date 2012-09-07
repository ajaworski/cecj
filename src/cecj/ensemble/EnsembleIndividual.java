package cecj.ensemble;

import ec.EvolutionState;
import ec.Individual;
import ec.util.Parameter;
import ec.vector.DoubleVectorIndividual;
import ec.vector.FloatVectorSpecies;

public class EnsembleIndividual extends Individual {
	
	public static final String P_ENSEMBLE_INDIVIDUAL = "ensemble-ind";

	private Individual[] individualsEnsemble;
	private int[] boundaries;

	public Individual[] getIndividualsEnsemble() {
		return individualsEnsemble;
	}

	public void setIndividualsEnsemble(Individual[] individualsEnsemble) {
		this.individualsEnsemble = individualsEnsemble;
	}

	public int[] getBoundaries() {
		return boundaries;
	}

	public void setBoundaries(int[] boundaries) {
		this.boundaries = boundaries;
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
		
		int ensembleSize = ((EnsembleSpecies)species).getEnsembleSize();
		
		//TODO parameters
		FloatVectorSpecies fvSpecies = new FloatVectorSpecies();
		fvSpecies.setup(state, base);
		this.individualsEnsemble = new Individual[ensembleSize];
		this.boundaries = new int[ensembleSize - 1];
		for (int i = 0; i < ensembleSize; i++){
			this.individualsEnsemble[i] = new DoubleVectorIndividual();
			this.individualsEnsemble[i].species = fvSpecies;			
			this.individualsEnsemble[i].setup(state, base);
			
			if (i < ensembleSize - 1)
				this.boundaries[i] = i;
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
	
}
