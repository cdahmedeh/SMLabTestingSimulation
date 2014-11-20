package org.smlabtesting.simabs.entity;

import static org.smlabtesting.simabs.entity.RCTestingMachine.Status.Idle;


/**
 * Maps to RC.TestingMachine
 * 
 * TODO: Document init process in CM.
 */
public class RCTestingMachine {
	// Possible testing machine statuses.
    public enum Status {
        Idle, Testing, NeedsCleaning, NeedsRepair, InRepair, InCleaning;
    }
    
    // Containers
    public ICSampleHolder sampleHolder = null;
    
    // Attributes
    public Status status = Idle;
    public boolean testSuccess = false;
    public int timeUntilFailure = Integer.MAX_VALUE; // Set in Setup Simulation 
    public int runTime = 0;
    public int completedTests = 0;
}
