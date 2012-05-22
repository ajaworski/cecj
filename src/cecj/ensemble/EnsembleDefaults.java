package cecj.ensemble;

import ec.DefaultsForm;
import ec.util.Parameter;

public class EnsembleDefaults implements DefaultsForm {

	public static final String P_ENSEMBLE = "ensemble";

	public static final Parameter base() {
		return new Parameter(P_ENSEMBLE);
	}
}
