import javafx.scene.layout.*;
import javafx.scene.*;
import javafx.scene.control.*;
import java.util.*;
import javafx.scene.paint.*;
import javafx.application.*;
import javafx.stage.*;

public class Main extends Application {
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

        stage.show();
    }

    /** Add the required cars to an environment.
     *  @param e Environment to use.
     */
    private static void addCars(Environment e) {
        /* Add an `interesting' set of cars */
        Random r = new Random();
        e.add(new Car(  0, 63, 2, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0)));
        e.add(new Car( 48, 79, 0, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0)));
        e.add(new Car(144, 60, 0, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0)));
        e.add(new Car(192, 74, 0, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0)));
        e.add(new Car(240, 12, 1, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0)));
        e.add(new Car(288, 77, 0, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0)));
        e.add(new Car(336, 28, 1, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0)));
        e.add(new Car(384, 32, 2, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0)));
        e.add(new Car(432, 16, 1, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0)));
    }
};
