package org.smlabtesting.sim.domain.entity.testingassembly;

import java.util.UUID;

import org.smlabtesting.sim.domain.generic.Entity;

public class UtilityClass {

   
    
    public String getUUID(Entity entity)
    {
        String id="";

        if(entity.getClass() == TestingMachine.class)
        {
            id= Code.TESTMACHINE.getPrefix()+":"+UUID.randomUUID().toString(); 
            return id;
        }
        return null;
    }
    
    
    public static enum Code
    {
        TESTMACHINE("0TESTM");

        private  String prefix;
        Code(String code)
        {
            this.prefix = code;
        }
        public String getPrefix() {
            return prefix;
        }
        
       
    };
}
