package cecj.statistics;

import ec.simple.SimpleEvolutionState;

public class SimpleEvolutionStateWithEvalCount extends SimpleEvolutionState{
	protected long evalCount = 0;
	
	public void addEvalCount(long evalCount){
		this.evalCount += evalCount;
	}
	
	public long getEvalCount(){
		return this.evalCount;
	}
}
