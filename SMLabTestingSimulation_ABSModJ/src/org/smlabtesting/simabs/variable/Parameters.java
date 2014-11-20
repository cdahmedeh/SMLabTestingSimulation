package org.smlabtesting.simabs.variable;

public class Parameters {
	// Maximum number of empty holders in the load/unload buffer.
	public int maxEmptyHolders = 3;
	
	// Number of testing machines for each test cell. (Value 0 is ignored by
	// loops because it machineId = 0 belongs to load/unload machine.
	public int[] numCellMachines = {-1, 3, 2, 1, 5, 2};
	
	// Total number of sample holders in the system.
	public int sampleHoldersCount = 70;
}
