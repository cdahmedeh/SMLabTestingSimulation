package org.smlabtesting.simabs.variable;

public class Constants {
	// The capacity of the unload buffer.
	public static final int UNLOADBUFFER_CAPACITY = 5;

	// The capacity of all test cell buffer queues.
	public static final int TEST_CELL_BUFFER_CAPACITY = 3;

	// Total number of sample holders in the system.
	public static final int NUM_SAMPLE_HOLDERS = 70;
	
	// How many slots fit in the racetrack.
	public static final int BELT_SLOTS_COUNT = 48;
	
	// Enter/Exit points for the load/unload machines and test cells.
	public static final int[] STATION_EXITS = {0, 8, 16, 24, 32, 40}; 			// Out from the station to the racetrack.
	public static final int[] STATION_ENTRANCES = {4, 12, 20, 28, 36, 44};		// In from the racetrack to the station.
	
	// Monthly costs of each station save for the load/unload machine 
	public static final int[] MONTHLY_MACHINE_COST = {-1, 10000, 12400, 8500, 9800, 11200};
	public static final int MONTHTLY_SAMPLEHOLDER_COST = 387;
	
	//A regular sample is on time if it is below this max time (1 hour)
	public static final int REGULAR_SAMPLE_MAX_TIME = 60 * 60;
	
	//A rush sample is on time if it is below this max time (30 min)
	public static final int RUSH_SAMPLE_MAX_TIME = 30 * 60;
}
