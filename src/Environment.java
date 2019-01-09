import java.util.ArrayList;

import javafx.animation.AnimationTimer;

/**
 * A class which represents the environment that we are working in. In other
 * words, this class describes the road and the cars that are on the road.
 */
public class Environment implements Cloneable {

    /** All the cars that are on our road */
    private ArrayList<Car> cars = new ArrayList<Car>();
    /** The Display object that we are working with */
    private Display display;
    /** Number of lanes to have on the road */
    private int lanes = 4;
    private long last;
    boolean collided = false;
    boolean overtaking = true;
    boolean undertaking = true;

    /**
     * Set the Display object that we are working with. This must be called before
     * anything will happen.
     */
    public void setDisplay(Display display) {
        this.display = display;

        /* Start a timer to update things */
        new AnimationTimer() {
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                }

                /* Update the model */
                tick((now - last) * 1e-9);

                /* Update the view */
                double furthest = 0;
                for (Car i : cars) {
                    if (i.getPosition() > furthest) {
                        furthest = i.getPosition();
                    }
                }
                display.setEnd((int) furthest);
                display.draw();
                last = now;
            }
        }.start();
    }

    /** Return a copy of this environment */
    public Environment clone() {
        Environment c = new Environment();
        for (Car i : cars) {
            c.cars.add(i.clone());
        }
        return c;
    }

    /** Draw the current state of the environment on our display */
    public void draw() {

        for (Car i : cars) {
            display.car((int) i.getPosition(), i.getLane(), i.getColor());

            // If car has crashed, display this
            if (collisionCheck(i) == true) {
                System.out.println("Car crashed");
            }

            // Allow cars to overtake each other
            overtake(i, overtaking, undertaking);

        }

    }

    /**
     * Add a car to the environment.
     * 
     * @param car
     *            Car to add.
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
    private void tick(double elapsed) {
        Environment before = Environment.this.clone();
        for (Car i : cars) {
            i.tick(before, elapsed, i);
        }
    }

    /**
     * @param behind
     *            A car.
     * @return The next car in front of @ref behind in the same lane, or null if
     *         there is nothing in front on the same lane.
     */
    public Car nextCar(Car behind) {
        Car closest = null;
        for (Car i : cars) {
            if (i != behind && i.getLane() == behind.getLane() && i.getPosition() > behind.getPosition()
                    && (closest == null || i.getPosition() < closest.getPosition())) {
                closest = i;
            }
        }
        return closest;
    }

    public void setLanes(int userDefined) {

        lanes = userDefined;

    }

    /** @return Number of lanes */
    public int getLanes() {
        return lanes;
    }

    public boolean collisionCheck(Car car) {

        if (nextCar(car) != null) {
            if (car.getPosition() >= nextCar(car).getPosition() - 40
                    && car.getPosition() <= nextCar(car).getPosition()) {
                collided = true;
            }
        }

        return collided;

    }

    public void setOvertaking(boolean input) {

        overtaking = input;

    }

    public void setUndertaking(boolean input) {

        undertaking = input;

    }

    public void overtake(Car car, boolean allowOvertaking, boolean allowUndertaking) {

        int lane;

        // Only overtake if there is actually a car in front
        if (nextCar(car) != null) {
            // If car is getting close to car in front, overtake or undertake (if allowed)
            if (car.getPosition() >= nextCar(car).getPosition() - 80
                    && car.getPosition() <= nextCar(car).getPosition() - 40) {
                lane = car.getLane();
                if (allowOvertaking == true && allowUndertaking == true) {
                    // Overtake
                    if (lane < 4) {
                        lane = lane + 1;
                        car.lane = lane;
                    } else {
                        // Undertake
                        if (lane > 1) {
                            lane = lane - 1;
                            car.lane = lane;
                        }
                    }
                } else if (allowOvertaking == false && allowUndertaking == true) {
                    // Undertake
                    if (lane > 1) {
                        lane = lane - 1;
                        car.lane = lane;
                    }
                } else if (allowOvertaking == true && allowUndertaking == false) {
                    // Overtake
                    if (lane < 4) {
                        lane = lane + 1;
                        car.lane = lane;
                    }
                }
            }
        }

    }

    public void setSpeedLimit(Car car, int limit) {

        // If car speed is bigger than limit, slow car down to the speed limit
        if (car.speed > limit) {
            car.speed = limit;
        }

        if (car.speed < limit) {

        }

    }

}
