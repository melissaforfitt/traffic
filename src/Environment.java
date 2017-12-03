import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.*;

/** A class which represents the environment that we are working in.  In other words,
 *  this class describes the road and the cars that are on the road.
 */
public class Environment implements Cloneable {

    /** All the cars that are on our road */
    private ArrayList<Car> cars = new ArrayList<Car>();
    /** The Display object that we are working with */
    private Display display;
    /** Number of lanes to have on the road */
    private int lanes = 4;

    /** Set the Display object that we are working with.  This must be called
     *  before anything will happen.
     */
    public void setDisplay(Display display) {
        this.display = display;

        /* Start a timer to update everything every 40ms */
        new Timer(40, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
                double furthest = 0;
                for (Car i: cars) {
                    if (i.getPosition() > furthest) {
                        furthest = i.getPosition();
                    }
                }
                display.setEnd((int) furthest);
                display.repaint();
            }
        }).start();
    }

    /** Return a copy of this environment */
    public Environment clone() {
        Environment c = new Environment();
        for (Car i: cars) {
            c.cars.add(i.clone());
        }
        return c;
    }

    /** Draw the current state of the environment on our display */
    public void draw() {
        for (Car i: cars) {
            display.car((int) i.getPosition(), i.getLane(), i.getColor());
        }
    }

    /** Add a car to the environment.
     *  @param car Car to add.
     */
    public void add(Car car) {
        cars.add(car);
    }

    public void clear() {
        cars.clear();
    }

    /** @return length of each car (in pixels) */
    public double carLength() {
        return 40;
    }

    /** Update the state of the environment after some short time has passed */
    private void tick() {
        Environment before = Environment.this.clone();
        for (Car i: cars) {
            i.tick(before);
        }
    }

    /** @param behind A car.
     *  @return The next car in front of @ref behind in the same lane, or null if there is nothing in front on the same lane.
     */
    public Car nextCar(Car behind) {
        Car closest = null;
        for (Car i: cars) {
            if (i != behind && i.getLane() == behind.getLane() && i.getPosition() > behind.getPosition() && (closest == null || i.getPosition() < closest.getPosition())) {
                closest = i;
            }
        }
        return closest;
    }

    /** @return Number of lanes */
    public int getLanes() {
        return lanes;
    }
}
