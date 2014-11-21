package org.smlabtesting.simabs.variable;

import java.util.stream.DoubleStream;

import org.smlabtesting.simabs.model.SMLabModel;

public class DVPs {
	@SuppressWarnings("unused")
	private SMLabModel model; 
	
	public DVPs(SMLabModel model) {
		this.model = model; 
	}
	
	// Constants
    private static final double[] TESTING_CYCLE_TIMES = DoubleStream
    		.of(0, 0.77, 0.85 , 1.04, 1.24 , 1.7).map(i -> i * 60).toArray();
    
    /**
     * @param stationId The identifier of the the test cell normally from 1 to 5.
     * @return A testing cycle time for a test cell in seconds. 
     */
    public int uTestingTime(int stationId) {
        return (int) TESTING_CYCLE_TIMES[stationId];
    }
}
