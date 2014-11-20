package simModel.action;

import simModel.ModelName;
import simModel.entity.RCLoadUnloadMachine;
import simModel.entity.QNewSamples;
import simModel.entity.RQRacetrack;
import simModel.entity.QRacetrackLine;
import simModel.entity.ICSampleHolder;
import simModel.entity.QTestCellBuffer;
import simModel.entity.RCTestingMachine;
import simModel.entity.QUnloadBuffer;
import absmodJ.ScheduledAction;


public class SetupSimulation extends ScheduledAction
{
	ModelName model;
	
	// Constructor
	public SetupSimulation(ModelName model) { this.model = model; }

	double [] ts = { 0.0, -1.0 }; // -1.0 ends scheduling
	int tsix = 0;  // set index to first entry.
	public double timeSequence() 
	{
		return ts[tsix++];  // only invoked at t=0
	}

	public void actionEvent() 
	{
		// TODO: Why......?
		model.qRacetrackLine = new QRacetrackLine[6];
		model.qTestCellBuffer = new QTestCellBuffer[6];
		model.rcTestingMachine = new RCTestingMachine[6][3];
		
        // Create the rqRacetrack.
        model.rqRacetrack = new RQRacetrack();

        // Create the load/unload machine.
        model.qNewSamples = new QNewSamples();
        model.qUnloadBuffer = new QUnloadBuffer();
        model.qRacetrackLine[0] = new QRacetrackLine();
        
        // Create some icSample holders, put them in the rqRacetrack line.
        for (int i = 0; i < 70; i++) {
            ICSampleHolder icSampleHolder = new ICSampleHolder();
            model.qRacetrackLine[0].insertQue(icSampleHolder);
        }
        
        model.rcLoadUnloadMachine = new RCLoadUnloadMachine();
                
        // Create the test cells.
        for (int stationId = 1; stationId <= 5; stationId++) {
            model.qTestCellBuffer[stationId] = new QTestCellBuffer();
            model.qRacetrackLine[stationId] = new QRacetrackLine();
        
            // Create testing machines in test cells.
            for (int machineId = 0; machineId < 3; machineId++) {
                model.rcTestingMachine[stationId][machineId] = new RCTestingMachine();
            }
        }
	}
	

}
