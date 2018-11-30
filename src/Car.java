import javafx.scene.paint.*;

/** State of a car on the road */
public class Car {

    /** Position of this car on the road (i.e. how far down the road it is) in pixels */
    private double position;
    /** Current speed in pixels per second */
    private double speed;
    /** Lane that this car is on */
    private int lane;
    /** Colour of this car's display */
    private Color color;

    public Car(double position, double speed, int lane, Color color) {
        this.position = position;
        this.speed = speed;
        this.lane = lane;
        this.color = color;
    }

    /** @return a new Car object with the same state as this one */
    public Car clone() {
        return new Car(position, speed, lane, color);
    }

    /** Update this car after `elapsed' seconds have passed */
    public void tick(Environment environment, double elapsed) {
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
}
