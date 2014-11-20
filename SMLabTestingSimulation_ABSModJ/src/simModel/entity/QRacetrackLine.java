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
public class QRacetrackLine {

    // Containers
    private final Deque<ICSampleHolder> icSampleHolders = new ArrayDeque<ICSampleHolder>();
    
    // Queue API
    public boolean hasNext() {
        return icSampleHolders.peek() != null;
    }

    public ICSampleHolder removeQue() {
        return icSampleHolders.pop();
    }

    public void insertQue(final ICSampleHolder entity) {
        icSampleHolders.add(entity);
    }
}
