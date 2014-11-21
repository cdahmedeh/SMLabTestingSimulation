package org.smlabtesting.simabs.variable;

import org.smlabtesting.simabs.model.SMLabModel;

/**
 * UDP.shiftRacetrack() 						is implemented in: RQRacetrack.shiftRacetrack(int)
 * UDP.testsRemainingNext(sample, stationId)	is implemented in: EnterTestCellBuffer.precondition(...) (see comments there)
 * UDP.testsCompleted(sample) 					is implemented in: EnterUnloadBuffer.precondition(...) (see comments there)
 */
public class UDPs {
	@SuppressWarnings("unused")
	private SMLabModel model;
	public UDPs(SMLabModel model) {
		this.model = model; 
	}
}
