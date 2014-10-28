package org.smlabtesting.ui.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.Iterator;

import javax.swing.JPanel;

import org.smlabtesting.sim.domain.entity.loadunload.LoadUnloadMachine;
import org.smlabtesting.sim.domain.entity.loadunload.RacetrackLine;
import org.smlabtesting.sim.domain.entity.loadunload.UnloadBuffer;
import org.smlabtesting.sim.domain.entity.racetrack.Racetrack;
import org.smlabtesting.sim.domain.entity.sampleholder.SampleHolder;
import org.smlabtesting.sim.domain.generic.Queue;

public class StructurePanel extends JPanel {
    private static final int MAX_QUEUE_SIZE = 5;

    private static final long serialVersionUID = -4268724120570713872L;

    // Sizes
    private static final int SLOT_WIDTH = 18;

    private static final int HOLDER_WIDTH = SLOT_WIDTH - 4;
    private static final int SAMPLE_WIDTH = HOLDER_WIDTH - 4;
    
    private static final int SLOT_HOLDER_SPACING = (SLOT_WIDTH - HOLDER_WIDTH) / 2;
    private static final int SLOT_SAMPLE_SPACING = (SLOT_WIDTH - SAMPLE_WIDTH) / 2;

    private static final int MAX_DIAGONAL_OFFSET = (int) (2 + Math.sqrt(2*Math.pow(SLOT_WIDTH, 2)) - SLOT_WIDTH);
    
    private static final int RECT_ARC_WIDTH = 2;
    
    private static final int BORDER_SPACING = 5;
    
    // Colours
    private static final Color BACKGROUND_COLOR = Color.WHITE;

    private static final Color SLOT_FILL_COLOR = Color.getHSBColor(0, 0, 0.95f);
    private static final Color SLOT_BORDER_COLOR = Color.getHSBColor(0, 0, 0.75f);
    
    private static final Color HOLDER_FILL_COLOR = Color.getHSBColor(0, 0, 0.80f);
    private static final Color HOLDER_BORDER_COLOR = Color.getHSBColor(0, 0, 0.60f);
    
    private static final Color SAMPLE_FILL_COLOR = Color.getHSBColor(0, 0.5f, 0.90f);
    private static final Color SAMPLE_BORDER_COLOR = Color.getHSBColor(0, 0, 0.30f);
    
    // Relationships
    private Racetrack racetrack;
    private LoadUnloadMachine loadUnloadMachine;
    private UnloadBuffer unloadBuffer;

    private RacetrackLine racetrackLine;

    public StructurePanel(Racetrack racetrack, LoadUnloadMachine loadUnloadMachine, UnloadBuffer unloadBuffer, RacetrackLine racetrackLine) {
        super();

        this.racetrack = racetrack;
        this.loadUnloadMachine = loadUnloadMachine;
        this.unloadBuffer = unloadBuffer;
        this.racetrackLine = racetrackLine;
        
        setBackground(BACKGROUND_COLOR);
    }
   
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        enableAntiAliasing(g);
       
        Point center = new Point((getWidth() - SLOT_WIDTH)/2, (getHeight() - SLOT_WIDTH)/2);
        int trackRadius = (Math.min(getWidth(), getHeight())) / 2 - ((MAX_QUEUE_SIZE) * (SLOT_WIDTH + MAX_DIAGONAL_OFFSET)) - SLOT_WIDTH/2 - BORDER_SPACING;
        
        drawRaceTrack(g, center, trackRadius);
        drawQueue(g, unloadBuffer, Racetrack.LOAD_UNLOAD_ENTRANCE, Racetrack.BELT_SLOTS_COUNT, trackRadius, center, true);
        drawQueue(g, racetrackLine, Racetrack.LOAD_UNLOAD_EXIT, Racetrack.BELT_SLOTS_COUNT, trackRadius, center, false);
        drawMachine(g, loadUnloadMachine, (Racetrack.LOAD_UNLOAD_ENTRANCE - Racetrack.LOAD_UNLOAD_EXIT)/2, Racetrack.BELT_SLOTS_COUNT, trackRadius, center);
    }

    private void drawRaceTrack(Graphics g, Point center, int trackRadius) {
        for (int i = 0; i < Racetrack.BELT_SLOTS_COUNT; i++) {
            Point point = getCirclePoint(i, Racetrack.BELT_SLOTS_COUNT, trackRadius, center);

            drawSlot(g, point.x, point.y);

            drawHolder(g, racetrack.peek(i), point.x, point.y);
        }
    }

    private void drawSlot(Graphics g, int x, int y) {
        g.setColor(SLOT_FILL_COLOR);
        g.fillRoundRect(x, y, SLOT_WIDTH, SLOT_WIDTH, RECT_ARC_WIDTH, RECT_ARC_WIDTH);
        
        g.setColor(SLOT_BORDER_COLOR);
        g.drawRoundRect(x, y, SLOT_WIDTH, SLOT_WIDTH, RECT_ARC_WIDTH, RECT_ARC_WIDTH);
        
        g.setColor(Color.BLACK);
    }


    private void drawHolder(Graphics g, SampleHolder holder, int x, int y) {
        if (holder != null) {
            g.setColor(HOLDER_FILL_COLOR);
            g.fillRoundRect(x + SLOT_HOLDER_SPACING, y + SLOT_HOLDER_SPACING, HOLDER_WIDTH, HOLDER_WIDTH, RECT_ARC_WIDTH, RECT_ARC_WIDTH);
            
            g.setColor(HOLDER_BORDER_COLOR);
            g.drawRoundRect(x + SLOT_HOLDER_SPACING, y + SLOT_HOLDER_SPACING, HOLDER_WIDTH, HOLDER_WIDTH, RECT_ARC_WIDTH, RECT_ARC_WIDTH);            
            
            if (holder.hasSample()) {
                g.setColor(SAMPLE_FILL_COLOR);
                g.fillOval(x + SLOT_SAMPLE_SPACING, y + SLOT_SAMPLE_SPACING, SAMPLE_WIDTH, SAMPLE_WIDTH);
                
                g.setColor(SAMPLE_BORDER_COLOR);
                g.drawOval(x + SLOT_SAMPLE_SPACING, y + SLOT_SAMPLE_SPACING, SAMPLE_WIDTH, SAMPLE_WIDTH);
            }
        }
        
        g.setColor(Color.BLACK);
    }
    
    private Point getCirclePoint(int position, int total, int radius, Point center) {
        return new Point(
                (int) ( radius * Math.cos(2 * Math.PI * position / total) ) + center.x,
                (int) ( radius * Math.sin(2 * Math.PI * position / total) ) + center.y
        );
    }
    
    private void drawQueue(Graphics g, Queue<SampleHolder> queue, int position, int total, int trackRadius, Point center, boolean reverse) {
        //reverse means out of the racetrack
        
        Math.abs( Math.sin( ( 90*Math.PI/180 ) * 2 ) );
        
        double angle = 2 * Math.PI * position / total;
        double xStep = Math.cos(angle) * (SLOT_WIDTH + MAX_DIAGONAL_OFFSET);
        double yStep = Math.sin(angle) * (SLOT_WIDTH + MAX_DIAGONAL_OFFSET); 
        
        //queue slots
        int queueSize = MAX_QUEUE_SIZE;
        
        Point start = getCirclePoint(position, total, trackRadius, center);
            
        Iterator<SampleHolder> iterator = queue.iterator();
        
        for (int i = 0; i < queueSize; i++) {
            int j = reverse ? (queueSize - 1 - i) : i;
            
            int x = (int) (start.x + (j + 1) * xStep);
            int y = (int) (start.y + (j + 1) * yStep);
            
            drawSlot(g, x, y);
            
            if (iterator.hasNext()) {
                SampleHolder sampleHolder = iterator.next();;
                drawHolder(g, sampleHolder, x, y);    
            }
        }
    }
    
    private void drawMachine(Graphics g, LoadUnloadMachine loadUnloadMachine, int position, int total, int trackRadius, Point center) {
        Point start = getCirclePoint(position, total, trackRadius, center);
        double angle = 2 * Math.PI * position / total;
        int xStep = (int) (Math.cos(angle) * (SLOT_WIDTH + MAX_DIAGONAL_OFFSET));
        int yStep = (int) (Math.sin(angle) * (SLOT_WIDTH + MAX_DIAGONAL_OFFSET)); 
        
        drawSlot(g, start.x + xStep, start.y + yStep);
        drawHolder(g, loadUnloadMachine.getSampleHolder(), start.x + xStep, start.y + yStep);
    }

    private void enableAntiAliasing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
}
