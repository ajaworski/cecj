package games.player;

import java.util.Arrays;

import ec.EvolutionState;
import ec.Individual;
import ec.simple.SimpleFitness;
import ec.util.MersenneTwisterFast;
import ec.util.Parameter;
import ec.vector.DoubleVectorIndividual;
import ec.vector.VectorIndividual;
import games.Board;

public class WPCPlayer implements EvolvedPlayer, IEvalCountingPlayer {

	private int boardSize;

	private double[] wpc;

	private double[][] traces;
	
	private long evalCount;
	
	public WPCPlayer() {
		
	}
	
	public WPCPlayer(int boardSize) {
		this.boardSize = boardSize;
		this.wpc = new double[boardSize * boardSize];
		this.evalCount = 0;
	}

	public WPCPlayer(double[] wpc) {
		this.boardSize = (int) Math.sqrt(wpc.length);
		this.wpc = wpc;
		this.evalCount = 0;
	}

	public static WPCPlayer readFromString(String string) {		
		String[] weights = string.trim().split("\\s+");
		WPCPlayer result = new WPCPlayer((int) Math.sqrt(weights.length));
		
		for (int i = 0; i < weights.length; i++) {
			result.wpc[i] = Double.parseDouble(weights[i]);
		}
		return result;
	}
	
	public void setup(EvolutionState state, Parameter base) {
		this.boardSize = 8;
		this.wpc = new double[64];
		this.evalCount = 0;
	}

	public double getValue(int row, int col) {
		return wpc[(row - 1) * boardSize + (col - 1)];
	}

	public double[] getWPC() {
		return wpc;
	}

	public void setValue(int row, int col, double value) {
		wpc[(row - 1) * boardSize + (col - 1)] = value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				builder.append(wpc[i * boardSize + j] + "\t");
			}
			builder.append("\n");
		}
		String res = builder.toString();
		return res;
	}

	public void randomizeWeights(MersenneTwisterFast random, double range) {
		for (int i = 0; i < wpc.length; i++) {
			wpc[i] = random.nextDouble() * range;
			if (random.nextBoolean()) {
				wpc[i] *= -1;
			}
		}
	}
	
	public void initializeEligibilityTraces() {
		traces = new double[boardSize + 1][boardSize + 1];
	}

	public void TDLUpdate(Board previous, double delta, double lambda) {
		double evalBefore = Math.tanh(evaluate(previous));
		double derivative = (1 - (evalBefore * evalBefore));

		for (int row = 1; row <= boardSize; row++) {
			for (int col = 1; col <= boardSize; col++) {
				traces[row][col] = traces[row][col] * lambda
						+ (derivative * previous.getValueAt(row, col));
				setValue(row, col, getValue(row, col) + (delta * traces[row][col]));
			}
		}
	}

	public void TDLUpdate(Board previous, double delta) {
		for (int row = 1; row <= boardSize; row++) {
			for (int col = 1; col <= boardSize; col++) {
				setValue(row, col, getValue(row, col) + (delta * previous.getValueAt(row, col)));
			}
		}
	}

	public double evaluate(Board board) {
		double result = 0;
		for (int row = 1; row <= board.getSize(); row++) {
			for (int col = 1; col <= board.getSize(); col++) {
				result += board.getValueAt(row, col) * getValue(row, col);
			}
		}
		evalCount++;
		return result;
	}

	public void readFromIndividual(Individual ind) {
		if (ind instanceof DoubleVectorIndividual) {
			wpc = ((DoubleVectorIndividual) ind).genome;
			boardSize = (int)Math.sqrt(wpc.length);
		} else {
			throw new IllegalArgumentException(
					"Individual should be of type DoubleVectorIndividual");
		}
	}

	public Individual createIndividual() {
		VectorIndividual ind = new DoubleVectorIndividual();
		ind.setGenome(wpc);
		ind.fitness = new SimpleFitness();
		return ind;
	}

	public EvolvedPlayer createEmptyCopy() {
		return new WPCPlayer(boardSize);
	}

	public void reset() {
		Arrays.fill(wpc, 0.0);
	}

	public long getEvalCount() {
		return this.evalCount;
	}
	
	public void addSingleEval(){
		this.evalCount++;
	}
}
