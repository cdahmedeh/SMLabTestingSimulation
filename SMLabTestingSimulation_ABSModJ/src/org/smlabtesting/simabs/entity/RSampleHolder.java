package org.smlabtesting.simabs.entity;


/**
 * Maps to R.SampleHolder
 */
public class RSampleHolder {
	// IDs
    public final Integer id;
	
    // Containers
    public ICSample sample = null;
    
    public RSampleHolder(int id){
    	this.id = id;
    }
}
