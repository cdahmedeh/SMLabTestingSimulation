package org.smlabtesting.simabs.variable;

import org.smlabtesting.simabs.model.SMLabModel;

public class DVPs {
	@SuppressWarnings("unused")
	private SMLabModel model; 
	
	public DVPs(SMLabModel model) {
		this.model = model; 
	}
	
	// Constants
    private static final double[] TESTING_CYCLE_TIMES = new double [] {0 * 60, 0.77 * 60, 0.85 * 60 , 1.04 * 60, 1.24 * 60, 1.7 * 60};
    
    /**
     * @param stationId The identifier of the the test cell normally from 1 to 5.
     * @return A testing cycle time for a test cell in seconds. 
     */
    public int uTestingTime(int stationId) {
        return (int) TESTING_CYCLE_TIMES[stationId];
    }
}
