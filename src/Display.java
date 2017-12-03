import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** Class to display our environment (i.e. the road and the cars) */
public class Display extends JPanel
{
    /** Last known size of our display, or null if not yet known */
    private Dimension size;
    /** Height of each lane in pixels */
    private int laneHeight = 16;
    /** Height of car in pixels */
    private int carHeight = 14;
    /** Distance to move the display off the left of the panel */
    private int xOffset = 0;
    /** Distance to move the display off the top of the panel */
    private int yOffset = 16;
    /** Our last known graphics context */
    private Graphics2D context;
    /** The Environment that we are displaying */
    private Environment environment;

    /* Dashed and solid strokes to draw the roads with */
    private final static float dash[] = { 10.0f };
    private final static BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
    private final static BasicStroke solid = new BasicStroke(2.0f);

    public Display(Environment environment) {
        this.environment = environment;
    }

    /** Set where the end of our road is, in pixels */
    public void setEnd(int end) {
        if (size == null) {
            return;
        }

        /* Scroll the road leftwards so that we can see the end of the road at the right of the display */
        if ((end + xOffset) + (size.width / 4) > size.width) {
            xOffset -= size.width / 4;
        }
    }

    /** Draw a car.
     *  @param position Car position.
     *  @param lane Car lane.
     *  @param color Car colour.
     */
    public void car(int position, int lane, Color color) {
        int pad = laneHeight - carHeight;
        context.setColor(Color.BLACK);
        context.drawRect(xOffset + position, yOffset + lane * laneHeight + pad / 2, (int) environment.carLength(), laneHeight - pad);
        context.setColor(color);
        context.fillRect(xOffset + position, yOffset + lane * laneHeight + pad / 2, (int) environment.carLength(), laneHeight - pad);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        size = getSize();
        context = (Graphics2D) g;

        /* Draw the dashed road lines */
        context.setStroke(dashed);
        for (int i = 1; i < environment.getLanes(); ++i) {
            context.drawLine(0, yOffset + i * laneHeight, size.width, yOffset + i * laneHeight);
        }

        /* Draw the main road lines */
        context.setStroke(solid);
        context.setColor(Color.BLACK);
        context.drawLine(0, yOffset, size.width, yOffset);
        context.drawLine(0, yOffset + environment.getLanes() * laneHeight, size.width, yOffset + environment.getLanes() * laneHeight);

        /* Draw the cars */
        environment.draw();

	/* Ensure that the display is up to date */
        Toolkit.getDefaultToolkit().sync();
    }

    public void setCarHeight(int carHeight) {
        this.carHeight = carHeight;
    }

    public void setLaneHeight(int laneHeight) {
        this.laneHeight = laneHeight;
    }
}
