import javafx.scene.paint.Color;

/** State of a car on the road */
public class Car {

    /**
     * Position of this car on the road (i.e. how far down the road it is) in pixels
     */
    public double position;
    /** Current speed in pixels per second */
    public double speed;
    /** Lane that this car is on */
    public int lane;
    /** Colour of this car's display */
    public Color color;
    /** Whether the car has collided or not */
    public boolean collided;
    /** If car has completed an overtake manouevre */
    public boolean overtakeComplete = false;
    /** Whether car has moved back to left lane after overtaking */
    public boolean movedLeft;
    /** Check which car this car is overtaking */
    public Car overtaking;

    public Car(double position, double speed, int lane, Color color, boolean collided, boolean overtakeComplete,
            boolean movedLeft, Car overtaking) {
        this.position = position;
        this.speed = speed;
        this.lane = lane;
        this.color = color;
        this.collided = collided;
        this.overtakeComplete = overtakeComplete;
        this.movedLeft = movedLeft;
    }

    /** @return a new Car object with the same state as this one */
    public Car clone() {
        return new Car(position, speed, lane, color, collided, overtakeComplete, movedLeft, overtaking);
    }

    /** Update this car after `elapsed' seconds have passed */
    public void tick(Environment environment, double elapsed, Car car) {

        position += speed * elapsed;

    }

    public double getPosition() {
        return position;
    }

    public int getLane() {
        return lane;
    }

    public Color getColor() {
        return color;
    }

    public boolean getCollided(Environment environment, Car car) {

        // Check and output whether a car has collided or not
        if (environment.collisionCheck(car) == true) {
            collided = true;
        } else {
            collided = false;
        }

        return collided;
    }

    public void accelerate() {

        // Increase the speed of the car
        speed = speed + 5;

    }

}
