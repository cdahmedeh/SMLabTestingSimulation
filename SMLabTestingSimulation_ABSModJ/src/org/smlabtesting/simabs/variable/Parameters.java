package org.smlabtesting.simabs.variable;

/**
 * An object that contains an entry for every parameter in the model. 
 * 
 * NOTE:The values set in here are just placeholder defaults that will be changed
 * for the experimentation step.  
 */
public class Parameters {
	// Maximum number of empty holders in the load/unload buffer.
	public int maxEmptyHolders = 5;
	
	// Number of testing machines for each test cell. (Value 0 is ignored by
	// loops because it machineId = 0 belongs to load/unload machine.
	public int[] numCellMachines = {-1, 1, 1, 1, 1, 1};
	
	public Parameters(int maxEmpty, int[] numMachines){
		maxEmptyHolders = maxEmpty;
		numCellMachines = numMachines;
	}
	public Parameters(){
		
	}
}
