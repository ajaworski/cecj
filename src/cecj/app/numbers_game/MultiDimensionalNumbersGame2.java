package cecj.app.numbers_game;

import java.math.BigInteger;
import java.util.List;


public class MultiDimensionalNumbersGame2 extends MultiDimensionalNumbersGame {

	@Override
	protected int compareDimensionsVectors(
			List<BigInteger> candidateVector, List<BigInteger> testVector) {

		int comparisonDimension = 0;
		BigInteger minDiffOnDimension = candidateVector.get(0).subtract(testVector.get(0)).abs();
		for (int dim = 1; dim < candidateVector.size(); dim++) {
			BigInteger diff = candidateVector.get(dim).subtract(testVector.get(dim)).abs();
			if (diff.compareTo(minDiffOnDimension) < 0) {
				minDiffOnDimension = diff;
				comparisonDimension = dim;
			}
		}

		if (candidateVector.get(comparisonDimension).compareTo(testVector.get(comparisonDimension)) > 0) {
			return 1;
		} else {
			return -1;
		}
	}
}
