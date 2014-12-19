package org.smlabtesting.simabs.model;

import static org.smlabtesting.simabs.entity.QNewSamples.REGULAR;
import static org.smlabtesting.simabs.entity.QNewSamples.RUSH;
import static org.smlabtesting.simabs.variable.Constants.C1;
import static org.smlabtesting.simabs.variable.Constants.C2;
import static org.smlabtesting.simabs.variable.Constants.C5;
import static org.smlabtesting.simabs.variable.Constants.LU;
import static org.smlabtesting.simabs.variable.Constants.NUM_SAMPLE_HOLDERS;
import static org.smlabtesting.simabs.variable.Constants.STATION_COUNT;

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
		model.qRacetrackLine = new QRacetrackLine[STATION_COUNT];
		model.qTestCellBuffer = new QTestCellBuffer[STATION_COUNT];
		
		// Determine how large the testing machine arrays need to be.
		int maxMachines = 0;
		for(int i = C1; i < STATION_COUNT; i++){
			if(model.parameters.numCellMachines[i] > maxMachines)
				maxMachines = model.parameters.numCellMachines[i];
		}
		
		// Create the 2D array for storing the testing machines. Five test
		// cells but with machineId = 0 not set as usual.
		model.rcTestingMachine = new RCTestingMachine[STATION_COUNT][maxMachines];
		
        // Create the racetrack.
        model.rqRacetrack = new RQRacetrack();

        // Create the load/unload machine with its queues.
        model.qNewSamples = new QNewSamples[2];
        model.qNewSamples[REGULAR] = new QNewSamples();
        model.qNewSamples[RUSH] = new QNewSamples();
        model.qUnloadBuffer = new QUnloadBuffer();
        model.qRacetrackLine[LU] = new QRacetrackLine();
        model.rLoadUnloadMachine = new RLoadUnloadMachine();
        
        // Create some sample holders, put them in the racetrack line of 
        // load/unload machine.
        model.sampleHolders = new RSampleHolder[NUM_SAMPLE_HOLDERS];
        for (int i = 0; i < NUM_SAMPLE_HOLDERS; i++) {
            model.sampleHolders[i] = new RSampleHolder(i);
            model.qRacetrackLine[LU].insertQue(i);
        }
                
        // Create the test cells.
        for (int stationId = C1; stationId <= C5; stationId++) {
            model.qTestCellBuffer[stationId] = new QTestCellBuffer();
            model.qRacetrackLine[stationId] = new QRacetrackLine();
        
            // Create testing machines in test cells. Using the number of 
            // test machines per cell from the parameter numCellMachines.
            for (int machineId = 0; machineId < model.parameters.numCellMachines[stationId]; machineId++) {
                model.rcTestingMachine[stationId][machineId] = new RCTestingMachine();
                
                // For machines that are not number C2, setup a failure time.
                if (stationId != C2) {
                	model.rcTestingMachine[stationId][machineId].timeUntilFailure
                		= model.rvp.uFailureDuration(stationId);
                }
            }
        }
	}
	

}
