package org.smlabtesting.simabs.entity;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maps to iC.Sample
 */
public class ICSample {
    // Attributes (they are set by Arrival)
    public Deque<Integer> testsRemaining = new ArrayDeque<>();
    public double startTime = 0.0;
    public boolean rush = false;
}
