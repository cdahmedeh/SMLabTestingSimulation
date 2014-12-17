package org.smlabtesting.simabs.action;

import org.smlabtesting.simabs.model.SMLabModel;

import absmodJ.ScheduledAction;

public class OutputAction extends ScheduledAction {

	private SMLabModel model;

	public OutputAction(SMLabModel model) {
		this.model = model;

	}
	
	private static int time = -3600*24;
	
	@Override
	protected double timeSequence() {
		return time = time + 3600*24;
	}

	@Override
	protected void actionEvent() {
		//model.printTabulatedOutputs();
	}

}
