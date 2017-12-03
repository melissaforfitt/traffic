import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        final Environment environment = new Environment();
        Display display = new Display(environment);
        environment.setDisplay(display);

        frame.setLayout(new BorderLayout());

        JPanel controls = new JPanel();
        frame.add(controls, BorderLayout.PAGE_START);

        JButton restart = new JButton("Restart");
        controls.add(restart);
        restart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                environment.clear();
                addCars(environment);
            }
        });
        
        frame.add(display, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setVisible(true);

        addCars(environment);
    }

    /** Add the required cars to an environment.
     *  @param e Environment to use.
     */
    private static void addCars(Environment e) {
        /* Add a couple of cars */
        e.add(new Car(0, 2, 0, Color.RED));
        e.add(new Car(100, 3, 0, Color.BLUE));
    }
};
