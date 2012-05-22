package cecj.ensemble;

import ec.EvolutionState;
import ec.Individual;
import ec.util.Parameter;

public class EnsembleIndividual extends Individual {
	
	public static final String P_ENSEMBLE_INDIVIDUAL = "ensemble-ind";

	private Individual[] individualsEnsemble;
	private double[] boundaries;
	private int[] order;
	

	public Individual[] getIndividualsEnsemble() {
		return individualsEnsemble;
	}

	public void setIndividualsEnsemble(Individual[] individualsEnsemble) {
		this.individualsEnsemble = individualsEnsemble;
	}

	public double[] getBoundaries() {
		return boundaries;
	}

	public void setBoundaries(double[] boundaries) {
		this.boundaries = boundaries;
	}

	public int[] getOrder() {
		return order;
	}

	public void setOrder(int[] order) {
		this.order = order;
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
		
		for (int i = 0; i < order.length; i++){
			if (ensemble.order[i] != order[i])
				return false;
		}
		
		for (int i = 0; i < boundaries.length; i++){
			if (ensemble.boundaries[i] != boundaries[i])
				return false;
		}
		
		return true;
	}
	
	@Override
	public Object clone() {
		EnsembleIndividual clone = (EnsembleIndividual) (super.clone());
		
		if (individualsEnsemble != null)
			clone.individualsEnsemble = individualsEnsemble.clone();
		
		if (order != null)
			clone.order = order.clone();
		
		if (boundaries != null)
			clone.boundaries = boundaries.clone();		

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
