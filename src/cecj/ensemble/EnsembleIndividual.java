package cecj.ensemble;

import ec.EvolutionState;
import ec.Individual;
import ec.Population;
import ec.Species;
import ec.Subpopulation;
import ec.simple.SimpleEvolutionState;
import ec.util.MersenneTwisterFast;
import ec.util.Output;
import ec.util.Parameter;
import ec.util.ParameterDatabase;
import ec.vector.DoubleVectorIndividual;
import ec.vector.FloatVectorSpecies;
import games.player.EnsemblePlayer;
import games.player.EvolvedPlayer;
import games.player.LearningPlayer;

public class EnsembleIndividual extends Individual {
	
	public static final String P_ENSEMBLE_INDIVIDUAL = "ensemble-ind";

	private Individual[] individualsEnsemble;

	public Individual[] getIndividualsEnsemble() {
		return individualsEnsemble;
	}

	public void setIndividualsEnsemble(Individual[] individualsEnsemble) {
		this.individualsEnsemble = individualsEnsemble;
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
		
		//TODO parameters
		FloatVectorSpecies fvSpecies = new FloatVectorSpecies();
		fvSpecies.setup(state, base);
		this.individualsEnsemble = new Individual[3];
		for (int i = 0; i < 3; i++){
			this.individualsEnsemble[i] = new DoubleVectorIndividual();
			this.individualsEnsemble[i].species = fvSpecies;
			this.individualsEnsemble[i].setup(state, base);
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
		
		if (individualsEnsemble != null)
			clone.individualsEnsemble = individualsEnsemble.clone();
		
		return clone;
	}

	@Override
	public int hashCode() {
		return individualsEnsemble.hashCode();
	}

	public Parameter defaultBase() {
		 return EnsembleDefaults.base().push(P_ENSEMBLE_INDIVIDUAL);
	}
	
	//Debugging starts here
//	private static final String P_SEED = "seed";
//	private static final String P_VERBOSITY = "verbosity";
//	private static final String P_PLAYER = "player";
//	
//	private static EvolvedPlayer player;
//	
//	public static void main(String[] args) {
//		ParameterDatabase parameters = ec.Evolve.loadParameterDatabase(args);
//		
//		Parameter verbosityParam = new Parameter(P_VERBOSITY);
//		int verbosity = parameters.getInt(verbosityParam, null, 0);
//		if (verbosity < 0) {
//			Output.initialError("Verbosity should be an integer >= 0.\n", verbosityParam);
//		}
//		
//		Output output = new Output(true);
//		output.addLog(ec.util.Log.D_STDOUT, false);
//		output.addLog(ec.util.Log.D_STDERR, true);
//
//		int time = (int) (System.currentTimeMillis());
//		Parameter seedParam = new Parameter(P_SEED);
//		int seed = ec.Evolve.determineSeed(output, parameters, seedParam, time, 0, false);
//		MersenneTwisterFast random = new MersenneTwisterFast(seed);
//
//		EvolutionState state = new SimpleEvolutionState();
//		state.parameters = parameters;
//		state.random = new MersenneTwisterFast[] { random };
//		state.output = output;
//
//		state.generation = 0;
//		state.population = new Population();
//		state.population.subpops = new Subpopulation[1];
//		state.population.subpops[0] = new Subpopulation();
//		state.population.subpops[0].individuals = new Individual[10];
////		state.population.subpops[0].species = (Species) state.parameters.getInstanceForParameter(new Parameter("pop.subpop.0.species"), null, EnsembleSpecies.class);
//		
//		Parameter playerParam = new Parameter(P_PLAYER);
//		player = (EvolvedPlayer) state.parameters.getInstanceForParameter(playerParam, null,
//				EvolvedPlayer.class);
//		player.setup(state, playerParam);
////		player.reset();
//
//		state.population.subpops[0].species = new EnsembleSpecies();
//		state.population.subpops[0].species.setup(state, state.population.subpops[0].defaultBase());
//		
//		for (int i = 0; i < 10; i++){
//			player.setup(state, playerParam);
//			state.population.subpops[0].individuals[i] = ((EvolvedPlayer) player).createIndividual();
//			state.population.subpops[0].individuals[i].setup(state, state.population.subpops[0].individuals[i].defaultBase());
//		}
//		
////		for (int i = 0; i < 2; i++){
////			for (int j = 0; j < 3; j++){
////				System.out.print(((EnsembleIndividual)state.population.subpops[0].individuals[i]).getIndividualsEnsemble()[j].hashCode()+" ; ");
////			}
////			System.out.println();
////		}
//		
//		EnsembleOuterCrossoverPipeline xover = new EnsembleOuterCrossoverPipeline();
//		xover.setup(state, xover.defaultBase());
//
//		xover.produce(2, 2, 5, 0, state.population.subpops[0].individuals, state, 0);
//	}
	
}
