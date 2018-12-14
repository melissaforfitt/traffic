import java.util.ArrayList;
import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    ArrayList<Car> carArray = new ArrayList<Car>();

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {

        final Environment environment = new Environment();
        final Display display = new Display(environment);
        environment.setDisplay(display);

        VBox box = new VBox();

        stage.setTitle("Traffic");
        stage.setScene(new Scene(box, 800, 600));

        HBox controls = new HBox();
        Button restart = new Button("Restart");
        controls.getChildren().add(restart);
        box.getChildren().add(controls);

        restart.setOnMouseClicked(e -> {
            environment.clear();
            display.reset();
            addCars(environment);
        });

        box.getChildren().add(display);

        addCars(environment);

        // TODO: Add all cars to arraylist here

        for (Car c : carArray) {
            environment.collisionCheck(c);
        }

        stage.show();
    }

    /**
     * Add the required cars to an environment.
     * 
     * @param e
     *            Environment to use.
     */
    private static void addCars(Environment e) {
        /* Add an `interesting' set of cars */
        Random r = new Random();
        e.add(new Car(0, 63, 2, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false));
        e.add(new Car(48, 79, 0, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false));
        e.add(new Car(144, 60, 0, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false));
        e.add(new Car(192, 74, 0, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false));
        e.add(new Car(240, 12, 1, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false));
        e.add(new Car(288, 77, 0, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false));
        e.add(new Car(336, 28, 1, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false));
        e.add(new Car(384, 32, 2, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false));
        e.add(new Car(432, 16, 1, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false));
    }

};
