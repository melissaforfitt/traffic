import java.awt.*;
import javax.swing.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Environment environment = new Environment();
        Display display = new Display(environment);
        environment.setDisplay(display);
        frame.add(display);
        frame.setSize(800, 600);
        frame.setVisible(true);

        /* Add a couple of cars */
        environment.add(new Car(0, 2, 0, Color.RED));
        environment.add(new Car(100, 3, 0, Color.BLUE));
    }
};
