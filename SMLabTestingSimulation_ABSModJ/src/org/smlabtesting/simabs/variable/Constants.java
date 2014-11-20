package org.smlabtesting.simabs.variable;

public class Constants {
	// How much runs for station 2 in order for it to be cleaned.
	public static final int STATION_2_CLEANING_THRESHOLD = 300;
	
	// The capacity of all test cell buffer queues.
	public static final int TEST_CELL_BUFFER_CAPACITY = 3;
	
	// The capacity of the unload buffer.
	public static final int UNLOADBUFFER_CAPACITY = 5;
	
	// How many slots fit in the racetrack.
	public static final int BELT_SLOTS_COUNT = 48;
	
	// Enter/Exit points for the load/unload machines and test cells.
	public static final int[] STATION_EXITS = {0, 8, 16, 24, 32, 40}; 			// Out from the station to the racetrack.
	public static final int[] STATION_ENTRANCES = {4, 12, 20, 28, 36, 44};		// In from the racetrack to the station.
}
