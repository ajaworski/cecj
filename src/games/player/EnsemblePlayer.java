package games.player;

import cecj.ensemble.EnsembleIndividual;
import cecj.ensemble.EnsembleSystem;
import ec.EvolutionState;
import ec.Individual;
import ec.simple.SimpleFitness;
import ec.util.MersenneTwisterFast;
import ec.util.Parameter;
import ec.vector.DoubleVectorIndividual;
import games.Board;

public abstract class EnsemblePlayer implements EvolvedPlayer, IEvalCountingPlayer {
	public static final int COMBINATION_MAX = 0x0;
	public static final int COMBINATION_AVG = 0x1;
	public static final int COMBINATION_MIN = 0x2;
	
	private static final String P_ENSEMBLE_SYSTEM = "ensemble-system";
	private static final String P_SUBPLAYER = "subplayer";
	private static final String P_COMBINATION = "combination";
		
	private EvolvedPlayer[] playersEnsemble;
	protected Integer[] boundaries;
	protected Integer[] groups;
	protected EvolvedPlayer subplayer = null;
	protected int combinationMethod;
	
	protected long evalCount;
	
	public void setup(EvolutionState state, Parameter base) {
		EnsembleSystem system = new EnsembleSystem();
		system.setup(state, base.push(P_ENSEMBLE_SYSTEM));

		subplayer = (EvolvedPlayer) state.parameters.getInstanceForParameter(base.push(P_SUBPLAYER), null, EvolvedPlayer.class);
		
		String combString = state.parameters.getStringWithDefault(base.push(P_COMBINATION), null, "max");
		if (combString.equals("max"))
			this.combinationMethod = COMBINATION_MAX;
		else if (combString.equals("avg"))
			this.combinationMethod = COMBINATION_AVG;
		else if (combString.equals("min"))
			this.combinationMethod = COMBINATION_MIN;
		else
			state.output.fatal("Combination method should be one of: max, avg, min");
		
		evalCount = 0;
		
//		EnsembleIndividual ind = new EnsembleIndividual();
//		system.randomizeIndividual(state, 0, ind);
//
//		readFromIndividual(ind);		
	}

	public void reset() {
		for (EvolvedPlayer player : playersEnsemble)
			player.reset();
	}

	public void readFromIndividual(Individual ind)
			throws IllegalArgumentException {
		if (ind instanceof EnsembleIndividual) {
			EnsembleIndividual ensemble = ((EnsembleIndividual) ind);
			
			playersEnsemble = new EvolvedPlayer[ensemble.getIndividualsEnsemble().length];
			boundaries = new Integer[ensemble.getBoundaries().length];
			groups = new Integer[ensemble.getGroups().length];
			
			for (int i = 0; i < ensemble.getIndividualsEnsemble().length; i++){
				EvolvedPlayer player;
				try {
					player = (EvolvedPlayer) Class.forName(subplayer.getClass().getName()).newInstance();
				} catch (Exception e) {
					throw new IllegalArgumentException(e.getMessage());
				}
				player.readFromIndividual(ensemble.getIndividualsEnsemble()[i]);
				playersEnsemble[i] = player;
			}
			
			for (int i = 0; i < ensemble.getBoundaries().length; i++){
				boundaries[i] = new Integer(ensemble.getBoundaries()[i]);
			}
			
			for (int i = 0; i < ensemble.getGroups().length; i++){
				groups[i] = new Integer(ensemble.getGroups()[i]);
			}
			
		} else {
			throw new IllegalArgumentException("Individual should be of type EnsembleIndividual");
		}
		
	}

	public Individual createIndividual() {
		EnsembleIndividual ind = new EnsembleIndividual();
		
		Individual[] individuals = new Individual[playersEnsemble.length];
		
		for (int i = 0; i < playersEnsemble.length; i++){
			individuals[i] = playersEnsemble[i].createIndividual();
		}
		
		ind.setIndividualsEnsemble(individuals);
		ind.fitness = new SimpleFitness();
				
		return ind;
	}

	public void randomizeWeights(MersenneTwisterFast random, double range) {
		for (EvolvedPlayer player : playersEnsemble)
			player.randomizeWeights(random, range);
	}
	
	public long getEvalCount(){
		return evalCount;
	}

	public EvolvedPlayer[] getPlayersEnsemble() {
		return playersEnsemble;
	}

}
