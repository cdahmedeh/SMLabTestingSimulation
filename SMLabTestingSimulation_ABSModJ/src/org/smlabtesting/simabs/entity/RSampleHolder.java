package org.smlabtesting.simabs.entity;


/**
 * Maps to R.SampleHolder
 */
public class RSampleHolder {
    // Containers
    public ICSample sample;
    public final Integer id;
    private static int serialId;
    
    public RSampleHolder(){
    	id = serialId++;
    }
}
