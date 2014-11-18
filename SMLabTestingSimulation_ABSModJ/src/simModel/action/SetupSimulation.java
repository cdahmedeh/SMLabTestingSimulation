package simModel.action;

import simModel.ModelName;
import simModel.entity.LoadUnloadMachine;
import simModel.entity.NewSamples;
import simModel.entity.Racetrack;
import simModel.entity.RacetrackLine;
import simModel.entity.SampleHolder;
import simModel.entity.TestCellBuffer;
import simModel.entity.TestingMachine;
import simModel.entity.UnloadBuffer;
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
        // Create the racetrack.
        model.racetrack = new Racetrack();

        // Create the load/unload machine.
        model.newSamples = new NewSamples();
        model.unloadBuffer = new UnloadBuffer();
        model.racetrackLine[0] = new RacetrackLine(0);
        
        // Create some sample holders, put them in the racetrack line.
        for (int i = 0; i < 70; i++) {
            SampleHolder sampleHolder = new SampleHolder();
            model.racetrackLine[0].queue(sampleHolder);
        }
        
        model.loadUnloadMachine = new LoadUnloadMachine();
                
        // Create the test cells.
        for (int stationId = 1; stationId <= 5; stationId++) {
            model.testCellBuffer[stationId] = new TestCellBuffer(stationId);
            model.racetrackLine[stationId] = new RacetrackLine(stationId);
        
            // Create testing machines in test cells.
            for (int machineId = 0; machineId < 3; machineId++) {
                model.testingMachine[stationId][machineId] = new TestingMachine(stationId, machineId);
            }
        }
	}
	

}
