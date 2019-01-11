import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    ArrayList<Car> carArray = new ArrayList<Car>();
    MediaPlayer player;

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
        Button accelerate = new Button("Accelerate");
        Button overtaking = new Button("Ban Overtaking");
        Button undertaking = new Button("Ban Undertaking");
        Button badLaneDiscipline = new Button("Bad Lane Discipline");
        Button badBraking = new Button("Bad Braking");
        TextField speedLimit = new TextField();
        speedLimit.setPrefColumnCount(3);
        Button setSpeed = new Button("Set Speed Limit");
        TextField lanes = new TextField();
        lanes.setPrefColumnCount(3);
        Button setLanes = new Button("Set Lane Amount");
        controls.getChildren().add(restart);
        controls.getChildren().add(accelerate);
        controls.getChildren().add(overtaking);
        controls.getChildren().add(undertaking);
        controls.getChildren().add(badLaneDiscipline);
        controls.getChildren().add(badBraking);
        controls.getChildren().add(speedLimit);
        controls.getChildren().add(setSpeed);
        controls.getChildren().add(lanes);
        controls.getChildren().add(setLanes);
        box.getChildren().add(controls);

        restart.setOnMouseClicked(e -> {
            environment.clear();
            display.reset();
            addCars(environment);
        });

        box.getChildren().add(display);

        addCars(environment);

        trafficSounds();

        stage.show();

        // User can control when cars accelerate
        accelerate.setOnMouseClicked(event -> {

            environment.accelerate();

        });

        // Allow user to decide if overtaking is allowed
        overtaking.setOnMouseClicked(event -> {

            environment.setOvertaking(false);

        });

        // Allow user to decide if undertaking is allowed
        undertaking.setOnMouseClicked(event -> {

            environment.setUndertaking(false);

        });

        // Allow user to decide if cars should have bad lane discipline
        badLaneDiscipline.setOnMouseClicked(event -> {

            environment.setLaneDiscipline(true);

        });

        // Allow user to decide if cars should have worse brake efficiency
        badBraking.setOnMouseClicked(event -> {

            environment.setBrakingEfficiency(true);

        });

        // If speed limit button is clicked, add restrictions to all car speeds
        setSpeed.setOnMouseClicked(event -> {

            int limit = Integer.parseInt(speedLimit.getText());

            environment.setSpeedLimit(limit);

        });

        // Set number of lanes to amount user has inputted
        setLanes.setOnMouseClicked(event -> {

            int lanesNumber = Integer.parseInt(lanes.getText());

            environment.setLanes(lanesNumber);

        });

        // TODO: CLICK ON A CAR TO ANALYSE ITS SPEED
        // speedAnalysis(Car clicked)

    }

    /**
     * Add the required cars to an environment.
     * 
     * @param e
     *            Environment to use.
     */
    private void addCars(Environment e) {

        // TODO: ADD RANDOM CARS LATER ON JOINING ONTO MOTORWAY
        /* Add an `interesting' set of cars */
        Random r = new Random();

        e.add(new Car(0, 63, 2, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false, false, false, false,
                null));

        e.add(new Car(48, 79, 0, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false, false, false,
                false, null));

        e.add(new Car(148, 60, 2, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false, false, false,
                false, null));

        e.add(new Car(192, 74, 0, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false, false, false,
                false, null));

        e.add(new Car(240, 12, 1, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false, false, false,
                false, null));

        e.add(new Car(288, 77, 0, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false, false, false,
                false, null));

        e.add(new Car(336, 28, 1, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false, false, false,
                false, null));

        e.add(new Car(384, 32, 2, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false, false, false,
                false, null));

        e.add(new Car(432, 16, 1, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false, false, false,
                false, null));

    }

    private void trafficSounds() {

        // Find traffic sound effect file and play it
        final Media sounds = new Media(Paths.get("traffic-sounds.wav").toUri().toString());
        player = new MediaPlayer(sounds);
        player.play();

        player.setVolume(10.0);

    }

};
