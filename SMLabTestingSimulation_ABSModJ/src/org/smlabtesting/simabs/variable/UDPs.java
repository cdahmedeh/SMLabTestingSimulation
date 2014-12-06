package org.smlabtesting.simabs.variable;

import org.smlabtesting.simabs.entity.ICSample;
import org.smlabtesting.simabs.entity.RQRacetrack;
import org.smlabtesting.simabs.entity.RSampleHolder;
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
	
	public boolean testsCompleted(ICSample sample) {
		return sample.testsRemaining.isEmpty();
	}

	public void completeNextText(ICSample sample) {
		sample.testsRemaining.pop();
	}

	public boolean testsRemainingNext(ICSample sample, int stationId) {
		return !sample.testsRemaining.isEmpty() && sample.testsRemaining.peek() == stationId;
	}
	
	public void shiftRacetrack(RQRacetrack rqRacetrack) {
		rqRacetrack.offset = (rqRacetrack.offset + 1) % rqRacetrack.n();
	}
	
    public RSampleHolder slots(RQRacetrack rqRacetrack, final int position) {
        return rqRacetrack.slots[position];
    }
    
}
