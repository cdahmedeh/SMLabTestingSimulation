package simModel.entity;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to Q.Racetrack
 * 
 * @author Lalit Azad
 * @author Ahmed El-Hajjar
 *
 */
public class RacetrackLine {

    // Containers
    private final Deque<SampleHolder> sampleHolders = new ArrayDeque<SampleHolder>();
    
    // Queue API
    public boolean hasNext() {
        return sampleHolders.peek() != null;
    }

    public SampleHolder removeQue() {
        return sampleHolders.pop();
    }

    public void insertQue(final SampleHolder entity) {
        sampleHolders.add(entity);
    }
}
