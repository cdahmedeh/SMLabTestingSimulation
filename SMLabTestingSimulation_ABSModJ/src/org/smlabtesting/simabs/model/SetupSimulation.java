package org.smlabtesting.simabs.model;

import static org.smlabtesting.simabs.variable.Constants.NUM_SAMPLE_HOLDERS;


import org.smlabtesting.simabs.entity.QNewSamples;
import org.smlabtesting.simabs.entity.QRacetrackLine;
import org.smlabtesting.simabs.entity.QTestCellBuffer;
import org.smlabtesting.simabs.entity.QUnloadBuffer;
import org.smlabtesting.simabs.entity.RCTestingMachine;
import org.smlabtesting.simabs.entity.RLoadUnloadMachine;
import org.smlabtesting.simabs.entity.RQRacetrack;
import org.smlabtesting.simabs.entity.RSampleHolder;

import absmodJ.ScheduledAction;


public class SetupSimulation extends ScheduledAction
{
	SMLabModel model;
	
	public SetupSimulation(SMLabModel model) { 
		this.model = model; 
	}

	// BEGIN - From template project
	double [] ts = { 0.0, -1.0 }; // -1.0 ends scheduling
	int tsix = 0;  // set index to first entry.
	public double timeSequence() {
		return ts[tsix++];  // only invoked at t=0
	}
	// END - From template project
	
	public void actionEvent() {
		// Create the arrays to store the racetrack lines and test cell buffers.
		// For the test cell buffer, entry 0 is never set because machineId = 0
		// belong to the load/unload machine.
		model.qRacetrackLine = new QRacetrackLine[6];
		model.qTestCellBuffer = new QTestCellBuffer[6];
		
		// Determine how large the testing machine arrays need to be.
		int maxMachines = 0;
		for(int i = 1; i < 6; i++){
			if(model.parameters.numCellMachines[i] > maxMachines)
				maxMachines = model.parameters.numCellMachines[i];
		}
		
		// Create the 2D array for storing the testing machines. Five test
		// cells but with machineId = 0 not set as usual.
		model.rcTestingMachine = new RCTestingMachine[6][maxMachines];
		
        // Create the racetrack.
        model.rqRacetrack = new RQRacetrack();

        // Create the load/unload machine with its queues.
        model.qNewSamples = new QNewSamples();
        model.qUnloadBuffer = new QUnloadBuffer();
        model.qRacetrackLine[0] = new QRacetrackLine();
        model.rLoadUnloadMachine = new RLoadUnloadMachine();
        
        // Create some sample holders, put them in the racetrack line of 
        // load/unload machine.
        for (int i = 0; i < NUM_SAMPLE_HOLDERS; i++) {
            RSampleHolder sampleHolder = new RSampleHolder();
            model.qRacetrackLine[0].insertQue(sampleHolder);
        }
                
        // Create the test cells.
        for (int stationId = 1; stationId <= 5; stationId++) {
            model.qTestCellBuffer[stationId] = new QTestCellBuffer();
            model.qRacetrackLine[stationId] = new QRacetrackLine();
        
            // Create testing machines in test cells. Using the number of 
            // test machines per cell from the parameter numCellMachines.
            for (int machineId = 0; machineId < model.parameters.numCellMachines[stationId]; machineId++) {
                model.rcTestingMachine[stationId][machineId] = new RCTestingMachine();
                
                // For machines that are not number 2, setup a failure time.
                if (stationId != 2) {
                	model.rcTestingMachine[stationId][machineId].timeUntilFailure
                		= model.rvp.uFailureDuration(stationId);
                }
            }
        }
	}
	

}
