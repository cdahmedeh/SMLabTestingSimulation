package org.smlabtesting.simabs.entity;


/**
 * Maps to RC.TestingMachine
 * 
 * @author Lalit Azad
 * @author Ahmed El-Hajjar
 */
public class RCTestingMachine {
    // States
    public enum TestingMachineState {
        Idle, Testing, NeedsCleaning, NeedsRepair, InRepair, Cleaning;
    }
    
    public static final int STATION_2_CLEANING_THRESHOLD = 300;
    
    // Containers
    public ICSampleHolder sampleHolder = null;
    
    // Attributes
    public TestingMachineState status = TestingMachineState.Idle;
    public boolean testSuccess = false;
    public int timeUntilFailure = 10; //TODO: Bad value
    public int runTime = 0;
    public int completedTests = 0;
    

    
}
