package org.smlabtesting.simabs.entity;


/**
 * Maps to RC.TestingMachine
 */
public class RCTestingMachine {
	// Possible testing machine statuses.
    public enum Status {
        Idle, Testing, NeedsCleaning, NeedsRepair, InRepair, InCleaning;
    }
    
    // Containers
    public ICSampleHolder sampleHolder = null;
    
    // Attributes
    public Status status = Status.Idle;
    public boolean testSuccess = false;
    public int timeUntilFailure = 10; //TODO: Bad value
    public int runTime = 0;
    public int completedTests = 0;
}
