import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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
    private int totalLanes = 4;
    private long last;
    boolean collided = false;
    boolean overtaking = true;
    boolean undertaking = true;
    boolean badLaneDiscipline = false;
    boolean farLeft;
    Car beingOvertaken;

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

            if (i.collided == false) {
                // If car has crashed, display this
                if (collisionCheck(i) == true) {
                    i.collided = true;
                    nextCar(i).collided = true;
                    i.speed = 0;
                    nextCar(i).speed = 0;
                    carHorn();
                }
            }

            // Allow cars to overtake each other
            overtake(i, overtaking, undertaking);

            // If bad lane discipline is not activated, move cars left after overtaking
            if (badLaneDiscipline == false) {
                moveToLeftLane(i);
            }

            // Slow down speed of car if it is getting too close to car in front
            if (i.slowedDown == false) {
                slowDown(i);
            }
            i.slowedDown = true; // Reset boolean so cars can slow down again in future

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

        totalLanes = userDefined;

    }

    /** @return Number of lanes */
    public int getLanes() {
        return totalLanes;
    }

    public boolean collisionCheck(Car car) {

        if (nextCar(car) != null) {
            if (car.getPosition() >= nextCar(car).getPosition() - 40
                    && car.getPosition() <= nextCar(car).getPosition()) {
                car.collided = true;
            }
        }

        return car.collided;

    }

    public void setOvertaking(boolean input) {

        overtaking = input;

    }

    public void setUndertaking(boolean input) {

        undertaking = input;

    }

    public void setLaneDiscipline(boolean input) {

        badLaneDiscipline = input;

    }

    public void overtake(Car car, boolean allowOvertaking, boolean allowUndertaking) {

        // Only overtake if there is actually a car in front
        if (nextCar(car) != null) {
            beingOvertaken = nextCar(car); // Car in front that is being overtaken
            // If car is getting close to car in front, overtake or undertake (if allowed)
            if (car.getPosition() >= beingOvertaken.getPosition() - 80
                    && car.getPosition() <= beingOvertaken.getPosition() - 40) {
                int lane = car.getLane();
                if (allowOvertaking == true && allowUndertaking == true) {
                    // Overtake
                    if (lane < totalLanes) {
                        lane = lane + 1;
                        car.lane = lane;
                    } else {
                        // Undertake
                        if (lane == totalLanes) {
                            lane = lane - 1;
                            car.lane = lane;
                        }
                    }
                } else if (allowOvertaking == false && allowUndertaking == true) {
                    // Undertake
                    if (lane == totalLanes) {
                        lane = lane - 1;
                        car.lane = lane;
                    }
                } else if (allowOvertaking == true && allowUndertaking == false) {
                    // Overtake
                    if (lane < totalLanes) {
                        lane = lane + 1;
                        car.lane = lane;
                    }
                }
            }
        }

    }

    public void moveToLeftLane(Car car) {

        int lane = car.getLane();

        // If car is now further ahead, mark overtake as complete
        if (car.getPosition() >= beingOvertaken.getPosition() + 40) {
            car.overtakeComplete = true;
            if (lane > 1) {
                if (car.movedLeft == false) {
                    lane = lane - 1;
                    System.out.println("Left lane is: " + lane);
                    car.lane = lane;
                    car.movedLeft = true;
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

    public void slowDown(Car car) {

        // If car is getting too close to car in front, slow its speed down
        if (nextCar(car) != null) {
            if (car.getPosition() >= nextCar(car).getPosition() - 150
                    && car.getPosition() <= nextCar(car).getPosition() - 80) {
                car.speed = car.speed - 2;
                car.slowedDown = true;
            }
        }

    }

    private void carHorn() {

        // Find traffic sound effect file and play it
        final Media sounds = new Media(Paths.get("car-horn.wav").toUri().toString());
        MediaPlayer player = new MediaPlayer(sounds);
        player.play();

        player.setVolume(10.0);

    }

}
