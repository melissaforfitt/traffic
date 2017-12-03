import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        final Environment environment = new Environment();
        final Display display = new Display(environment);
        environment.setDisplay(display);

        frame.setLayout(new BorderLayout());

        JPanel controls = new JPanel();
        frame.add(controls, BorderLayout.PAGE_START);

        JButton restart = new JButton("Restart");
        controls.add(restart);
        restart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                environment.clear();
                display.reset();
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
        /* Add some random cars */
        Random r = new Random();
        for (int i = 0; i < 10; ++i) {
            e.add(new Car(e.carLength() * 1.2 * i, r.nextInt(20) / 4.0, r.nextInt(3), new Color(r.nextInt(0xffffff))));
        }
    }
};
