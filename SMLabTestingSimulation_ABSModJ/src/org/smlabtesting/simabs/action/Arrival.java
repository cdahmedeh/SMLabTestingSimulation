package org.smlabtesting.simabs.action;

import static org.smlabtesting.simabs.entity.QNewSamples.REGULAR;
import static org.smlabtesting.simabs.entity.QNewSamples.RUSH;

import org.smlabtesting.simabs.entity.ICSample;
import org.smlabtesting.simabs.model.SMLabModel;

import absmodJ.ScheduledAction;

/**
 * This action describes when a new sample arrives into one of the
 * new samples queues.
 * 
 * Participants: Q.NewSamples
 * Uses: iC.Sample (implicit)
 */
public class Arrival extends ScheduledAction {
	private SMLabModel model;

	public Arrival(SMLabModel model) {
		this.model = model;
	}
	
	@Override
	protected double timeSequence() {
		// Retrieves the next arrival time of a new untested sample
        return model.getClock() + model.rvp.uSampleArrival();
	}

	@Override
	protected void actionEvent() {
        // Create a new sample with a randomly derived sequence and priority.
        ICSample icSample = new ICSample();
        icSample.testsRemaining = model.rvp.uSequenceOfTests();
        icSample.rush = model.rvp.uSampleRush();
        icSample.startTime = model.getClock();
		
        // Move the new sample into the appropriate new samples queue based on
        // it's priority.
        if( icSample.rush ) {
        	model.qNewSamples[RUSH].insertQue(icSample);
        } else {
        	model.qNewSamples[REGULAR].insertQue(icSample);
        }
	}
}
