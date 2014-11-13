package org.smlabtesting.sim.domain.entity.testingassembly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.smlabtesting.sim.domain.entity.racetrack.Racetrack;

public class TestMachineManager{

     static Map<Integer,List<TestingMachine>> groupMap = new HashMap<Integer,List<TestingMachine>>();
     static int groupIDCounter;
     
     private static Map<Integer, Integer> enterSlots = new HashMap<Integer, Integer>();
     private static Map<Integer, Integer> exitSlots = new HashMap<Integer, Integer>();

     //Load-Unload machine slot is initialized here
     private static final int loadUnloadExitSlot = Racetrack.LOAD_UNLOAD_EXIT;
     private static final int loadUnloadEnterSlot = Racetrack.LOAD_UNLOAD_ENTRANCE;
     
     static
     {
         enterSlots.put(loadUnloadEnterSlot , -1);
         exitSlots.put(loadUnloadExitSlot, -1);
     }
     
     
     public static  int assignEnterSlots(int slotNumber, int testCellNumber)
     {
         return assignSlots(slotNumber, testCellNumber, enterSlots);
         
     }
     
     public static  int assignExitSlots(int slotNumber, int testCellNumber)
     {
        return assignSlots(slotNumber, testCellNumber, exitSlots);
     }
   
     // keep incrementing the slot number, and assign it once to the map , once you get the slot number
     private static int assignSlots(int slotNumber, int testCellNumber, Map<Integer, Integer> map)
     {
         
       
         
     }
     
   
     
     private List<TestingMachine> getMachineList(Integer i)
    {
        List<TestingMachine> list = groupMap.get(i);
        return list;
        
    }
    
    public void addTestingMachineToGroup(Integer testNumber, TestingMachine machine)
    {
        List <TestingMachine> list = getMachineList(testNumber);
        if(list == null)
            list= new ArrayList<TestingMachine>();
        
        list.add(machine);
        groupMap.put(testNumber, list);
        
    }
    

    
    public static int generateGroupID()
    {
     groupIDCounter++;
     return groupIDCounter;
    }
    
  
  
    
    
}
