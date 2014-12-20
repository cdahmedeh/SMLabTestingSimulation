package org.smlabtesting.simabs.model;

import static org.smlabtesting.simabs.entity.QNewSamples.REGULAR;
import static org.smlabtesting.simabs.entity.QNewSamples.RUSH;

import org.smlabtesting.simabs.entity.RSampleHolder;

/**
 * Helper methods to handle printing of the model onto the console. Printing
 * code would need templating to be more readable.
 */
public class ModelPrinter {
	public static void showBasicModelInfo(SMLabModel model) {
		System.out.println("-------- Model Information -------- \n");
		System.out
				.print(String.format(
						"Clock: %f, \n"
						+ "Q.NewSamples[REGULAR].n: %d \n"
						+ "Q.NewSamples[RUSH].n: %d \n"
						+ "\n"
						+ "Station(L/U): \n"
						+ "   Q.UnloadBuffer.n: %d, Q.UnloadBuffer.nEmpty: %d, \n"
						+ "      numFailedStationEntries[0]: %d \n"
						+ "   R.LoadUnloadMachine.busy: %b \n"
						+ "   Q.RacetrackLine[UL].n:  %d \n",
						model.getClock(),
						model.qNewSamples[REGULAR].n(),
						model.qNewSamples[RUSH].n(),

						model.qUnloadBuffer.n(), 
						model.qUnloadBuffer.nEmpty,
						model.output.totalFailedStationEntries[0],
						model.rLoadUnloadMachine.busy,
						model.qRacetrackLine[0].n()));
		for (int stationId = 1; stationId < 6; stationId++) {
			System.out.print(String.format(
					"Station(%d): \n"
					+ "   Q.TestCellBuffer.n: %d, totalFailedStationEntries: %d\n"
					,
					stationId,
					model.qTestCellBuffer[stationId].n(),
					model.output.totalFailedStationEntries[stationId])
			);

			for (int machineId = 0; machineId < model.parameters.numCellMachines[stationId]; machineId++) {
				System.out.print(String.format(
					"   RC.TestingMachine[%d][%d].status: %s \n"
					,
					stationId, 
					machineId,
					model.rcTestingMachine[stationId][machineId].status)
				);
			}

			System.out.println(String.format(
				"   Q.RacetrackLine[%d].n: %d"
				, 
				stationId,
				model.qRacetrackLine[stationId].n())
			);
		}
	} 
	
	/**
	 * Prints a visual representation of the 48 racetrack slots, for logging purposes.
	 */
	public static void printRacetrackView(SMLabModel model) {
		//Print the 'top' row, so belt indexes 15-36
		System.out.println(" ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___");
		for(int i = 15; i < 37; i++){
			String sHolder = getSlotRepresentation(model, i);
			System.out.print("|"+sHolder);
		}
		System.out.println("|");
		System.out.println("|___|Ex4|___|___|___|In4|___|___|___|Ex3|___|___|___|In3|___|___|___|Ex2|___|___|___|In2|");
		//Print the left and right sides of the belt, so right belt indexes = 37, 38, and left belt indexes = 14, 13
		String innerBeltSpace = "                                                                               ";
		System.out.println("|"+getSlotRepresentation(model, 14)+"|"+innerBeltSpace+"|"+getSlotRepresentation(model, 37)+"|");
		System.out.println("|___|"+innerBeltSpace+"|___|");
		System.out.println("|"+getSlotRepresentation(model, 13)+"|"+innerBeltSpace+"|"+getSlotRepresentation(model, 38)+"|");

		//Print the bottom side of the belt, so belt indexes 39-47 and 0-12
		String bottom = "";
		System.out.println("|___|___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___|___|");
		for(int i = 39; i < (47 + 13 + 1); ++i){
			String sHolder = getSlotRepresentation(model, i%48);
			bottom =sHolder +"|" + bottom;
		}
		System.out.println("|"+bottom);
		System.out.println("|In5|___|___|___|Ex5|___|___|___|In0|___|___|___|Ex0|___|___|___|In1|___|___|___|Ex1|___|");
		System.out.println();
	}
	
	/**
	 * Determines whether a slot on the racetrack (at index slotId) has nothing ("   "), an empty sample holder ("[ ]"), or a sample holder with a sample ("[x]")
	 * @param slotId index of the slot
	 * @return the string representing the slot content (or lack thereof)
	 */
	private static String getSlotRepresentation(SMLabModel model, int slotId){
		String sHolder = "   ";
		RSampleHolder sampleHolder = model.udp.getSampleHolder(model.rqRacetrack.slots.get(slotId));
		if(sampleHolder != null && sampleHolder.sample != null){
			sHolder = "[x]";
		} else if(sampleHolder != null && sampleHolder.sample == null){
			sHolder = "[ ]";
		}
		return sHolder;
	}
	
	public static void printOutputs(SMLabModel model) {
		model.output.percentageLateRegularSamples();
		model.output.percentageLateRushSamples();
		System.out.println("Regular percentage late: "+model.output.percentageLateRegularSamples);
		System.out.println("Rush percentage late: "+model.output.percentageLateRushSamples);
		
		//Print failed station entries
		System.out.print("Total Failed Station Entries: < ");
		System.out.print(model.output.totalFailedStationEntries[0]);
		for(int i = 1; i < 6; i++){
			System.out.print(", "+ model.output.totalFailedStationEntries[i]);
		}
		System.out.print(" > ");
		System.out.println();
	}
}
