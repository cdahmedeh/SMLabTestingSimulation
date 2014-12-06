package org.smlabtesting.simabs.entity;


/**
 * Maps to R.SampleHolder
 */
public class RSampleHolder {
    // Containers
    public ICSample sample;
    public final Integer id;
    private static Integer idSerialNum = 0;
    
    public RSampleHolder(){
    	id = idSerialNum++;
    }
}
