package org.smlabtesting.sim.domain.entity.testingassembly;

import java.util.ArrayDeque;
import java.util.Deque;

import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;
import org.smlabtesting.sim.domain.generic.Entity;
import org.smlabtesting.sim.domain.generic.Handler;
import org.smlabtesting.sim.domain.generic.Queue;

// Make the BoundedQueue an entity, and keep it outside the TestMachine
public  class BoundedQueue <E extends Entity > implements  Queue<E>
{

    // infinite bounded size is set to 100
    private  int boundedQueueSize;
    public int getBoundedQueueSize() {
        return boundedQueueSize;
    }

    public void setBoundedQueueSize(int boundedQueueSize) {
        this.boundedQueueSize = boundedQueueSize;
    }
    
   public BoundedQueue(){
        super();
        this.setBoundedQueueSize(100);
    }
    
   public  BoundedQueue(int n)
    {
        super();
        this.setBoundedQueueSize(n);
    }

    private Deque<E> queue = new ArrayDeque<E>() ;
    
    @Override
    public boolean hasNext() {
       return queue.peek()!= null;
    }

    @Override
    public E next() {
        // TODO Auto-generated method stub
        if(hasNext())
            return queue.poll();
        return null;
    }

    @Override
    public boolean hasVacancy() {
        return queue.size()< boundedQueueSize;
    }

    @Override
    public void queue(E entity) 
    {
        if(hasVacancy())
            queue.offer(entity);
        else 
            throw new RuntimeException("Retrying in queue, Bounded Queue full");
        
    }
    
    
    public int getCurrentQueueSize()
    {
        return queue.size();
    }

   
}

