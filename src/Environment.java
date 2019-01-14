import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

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
    boolean badBraking = false;
    boolean farLeft;
    boolean speedLimitSet = false;
    int timer;
    int speedLimit;
    boolean carAllowed;

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

                timer = timer + 1;

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

            // NOTE: UNCOMMENT THIS FOR ADDING MORE CARS VERSION (90% works)
            // Randomly generate more cars as they merge onto motorway
            // addRandomCar();

            if (i.collided == false) {
                // If car has crashed, display this
                if (collisionCheck(i) == true) {
                    i.collided = true;
                    nextCar(i).collided = true;
                    i.speed = 0;
                    nextCar(i).speed = 0;
                    carHorn(); // Make car horn noise to indicate car has crashed
                }
            }

            // Allow cars to overtake each other

            // If bad lane discipline is not activated, move cars left after overtaking
            if (badLaneDiscipline == false) {
                moveToLeftLane(i);
            }

            // Slow down speed of car if it is getting too close to car in front
            slowDown(i);

            // Speed up car if there is nothing in front of it
            if (speedLimitSet == true && i.speed < speedLimit) {
                speedUp(i);
            } else if (speedLimitSet == false && i.speed < 70) {
                speedUp(i);
            }

            // If there is no car in front, speed back up to speed limit

            while (leftCar(i) == null && i.lane > 0) {
                i.lane = i.lane - 1;
            }

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

    public Car leftCar(Car right) {
        Car left = null;
        for (Car i : cars) {
            if (i != right && i.getLane() == right.getLane() - 1 && right.getPosition() > i.position - 50
                    && right.getPosition() < i.position + 50) {
                left = i;
            }
        }
        return left;
    }

    public void setLanes(int userDefined) {

        totalLanes = userDefined;

    }

    /** @return Number of lanes */
    public int getLanes() {
        return totalLanes;
    }

    public void addRandomCar() {

        try {
            // Randomly generate more cars as they merge onto motorway
            if (timer % 250 == 0) {
                Random r = new Random();
                int newCarPosition = timer;
                int randomLane = r.nextInt(totalLanes - 1) + 0;
                for (Car car : cars) {
                    // If a car is already in that location, don't add new car
                    if (car.getLane() == totalLanes && car.getPosition() - 20 >= newCarPosition
                            && car.getPosition() + 20 >= newCarPosition) {
                        carAllowed = false;
                    } else {
                        carAllowed = true;
                    }
                }
                if (carAllowed == true) {
                    cars.add(new Car(newCarPosition, r.nextInt(80 + 1) + 10, randomLane,
                            new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false, false, false, null));
                }
                timer = timer + r.nextInt(100);
            }
        } catch (ConcurrentModificationException e) {
        }

    }

    public boolean collisionCheck(Car car) {

        // Check whether a car has collided with another one
        if (nextCar(car) != null) {
            if (car.getPosition() >= nextCar(car).getPosition() - 40
                    && car.getPosition() <= nextCar(car).getPosition()) {
                car.collided = true;
                nextCar(car).collided = true;
            }
        }

        return car.collided;

    }

    public void setOvertaking(boolean input) {

        // Allow user to change whether overtaking is allowed
        overtaking = input;

    }

    public void setUndertaking(boolean input) {

        // Allow user to change undertaking settings (on/off)
        undertaking = input;

    }

    public void setLaneDiscipline(boolean input) {

        // Allow user to change lane discipline of cars
        badLaneDiscipline = input;

    }

    public void setBrakingEfficiency(boolean input) {

        // Allow user to make cars have bad braking efficiency
        badBraking = input;

    }

    public void overtake(Car car, boolean allowOvertaking, boolean allowUndertaking) {

        // Only overtake if there is actually a car in front
        if (nextCar(car) != null) {
            car.overtaking = nextCar(car); // Car in front that is being overtaken
            // If car is getting close to car in front, overtake or undertake (if allowed)
            if (car.getPosition() >= car.overtaking.getPosition() - 100
                    && car.getPosition() <= car.overtaking.getPosition() - 20) {
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
        if (car.overtaking != null) {
            if (car.getPosition() >= car.overtaking.getPosition() + 40) {
                car.overtakeComplete = true;
                if (lane > 0) {
                    if (car.movedLeft == false) {
                        lane = lane - 1;
                        car.lane = lane;
                        car.movedLeft = true;
                        car.overtaking = null; // Reset value so car can overtake again later
                    }
                }
            }
        }
    }

    public int setSpeedLimit(int limit) {

        speedLimitSet = true;
        speedLimit = limit;

        for (Car car : cars) {
            // If car speed is bigger than limit, slow car down to the speed limit
            if (car.speed > limit) {
                car.speed = limit;
            }
        }

        return limit;

    }

    public void slowDown(Car car) {

        // If car is getting too close to car in front, slow its speed down
        if (nextCar(car) != null) {
            if (car.getPosition() >= nextCar(car).getPosition() - 80
                    && car.getPosition() <= nextCar(car).getPosition() - 50 && car.speed > 10) {
                if (badBraking == true) {
                    car.speed = car.speed - 0.5;
                } else {
                    car.speed = car.speed - 5;
                }
            }
        }
    }

    public void speedUp(Car car) {

        // If there is no car in front, speed up to maximum speed within limit
        if (speedLimitSet == true) {
            if (nextCar(car) == null || ((car.getPosition() >= nextCar(car).getPosition() - 150)
                    && (car.getPosition() <= nextCar(car).getPosition() - 90))) {
                do {
                    car.accelerate();
                } while (car.speed < speedLimit);
            }
        } else {
            // If no speed limit is set, speed cars up to national speed limit (70mph)
            if (nextCar(car) == null || ((car.getPosition() >= nextCar(car).getPosition() - 150)
                    && (car.getPosition() <= nextCar(car).getPosition() - 90))) {
                do {
                    car.accelerate();
                } while (car.speed < 70);
            }
        }
    }

    public void accelerateAllCars() {

        for (Car car : cars) {
            car.speed = car.speed + 5;
        }

    }

    private void carHorn() {

        // Find traffic sound effect file and play it
        final Media sounds = new Media(Paths.get("car-horn.wav").toUri().toString());
        MediaPlayer player = new MediaPlayer(sounds);
        player.play();

        player.setVolume(10.0);

    }

    public String speedAnalysis() {

        // Find out the speed of the cars that are currently on the motorway
        ArrayList<Double> carSpeeds = new ArrayList<Double>();

        for (Car car : cars) {
            if (collided == false) {
                carSpeeds.add(car.speed);
            }
        }

        // Sort the speeds into ascending order
        Collections.sort(carSpeeds);

        // Output current range of car speeds
        String analysis = "The speed of the cars ranges from " + carSpeeds.get(0) + "mph to "
                + carSpeeds.get(carSpeeds.size() - 1) + "mph.";

        return analysis;

    }

    public void clearCars() {

        // If reset button is clicked, remove all cars from arraylist
        cars.clear();
        timer = 0;

    }

}
