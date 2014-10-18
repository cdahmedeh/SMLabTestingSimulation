package org.smlabtesting.sim.domain.entity;

import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.State;

/**
 * The sample is the test tube holding a specimen of the patient.
 *  
 * @author Ahmed El-Hajjar
 */
public class Sample extends Entity {
	protected enum SampleState implements State {
		Unloaded,
		Moving,
		Tested
	}

	@Override
	public void process() {
		//TODO: Implement me.
	}
}
