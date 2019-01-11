import java.nio.file.Paths;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    MediaPlayer player;
    Text overtakingText = new Text();
    Text undertakingText = new Text();
    Text badLaneDisciplineText = new Text();
    Text wetRoadsText = new Text();
    Text speedLimitText = new Text();
    Text laneNumberText = new Text();
    Text speedAnalysisText = new Text();

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

        HBox controls1 = new HBox();
        HBox controls2 = new HBox();
        HBox controlsOutput = new HBox();
        HBox speedAnalysis = new HBox();
        Button restart = new Button("Restart");
        Button accelerate = new Button("Accelerate");
        Button overtaking = new Button("Ban Overtaking");
        Button undertaking = new Button("Ban Undertaking");
        Button badLaneDiscipline = new Button("Bad Lane Discipline");
        Button wetRoads = new Button("Wet Roads");
        TextField speedLimit = new TextField();
        speedLimit.setPrefColumnCount(3);
        Button setSpeed = new Button("Set Speed Limit");
        TextField lanes = new TextField();
        lanes.setPrefColumnCount(3);
        Button setLanes = new Button("Set Lane Amount");
        Button speedAnalysisButton = new Button("Speed Analysis");
        controls1.getChildren().add(restart);
        controls1.getChildren().add(accelerate);
        controls1.getChildren().add(overtaking);
        controls1.getChildren().add(undertaking);
        controls1.getChildren().add(badLaneDiscipline);
        controls1.getChildren().add(wetRoads);
        controls2.getChildren().add(speedLimit);
        controls2.getChildren().add(setSpeed);
        controls2.getChildren().add(lanes);
        controls2.getChildren().add(setLanes);
        speedAnalysis.getChildren().add(speedAnalysisButton);
        box.getChildren().add(controls1);
        box.getChildren().add(controls2);
        box.getChildren().add(controlsOutput);
        box.getChildren().add(speedAnalysis);

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

            overtakingText.setText("Overtaking is NOT allowed" + " ");
            controlsOutput.getChildren().add(overtakingText);

        });

        // Allow user to decide if undertaking is allowed
        undertaking.setOnMouseClicked(event -> {

            environment.setUndertaking(false);

            undertakingText.setText("Undertaking is NOT allowed" + " ");
            controlsOutput.getChildren().add(undertakingText);

        });

        // Allow user to decide if cars should have bad lane discipline
        badLaneDiscipline.setOnMouseClicked(event -> {

            environment.setLaneDiscipline(true);

            badLaneDisciplineText.setText("Bad lane discipline is: ON" + " ");
            controlsOutput.getChildren().add(badLaneDisciplineText);

        });

        // Allow user to decide if cars should have worse brake efficiency
        wetRoads.setOnMouseClicked(event -> {

            environment.setBrakingEfficiency(true);

            wetRoadsText.setText("Wet road mode is: ON" + " ");
            controlsOutput.getChildren().add(wetRoadsText);

        });

        // If speed limit button is clicked, add restrictions to all car speeds
        setSpeed.setOnMouseClicked(event -> {

            int limit = Integer.parseInt(speedLimit.getText());

            environment.setSpeedLimit(limit);

            speedLimitText.setText("Speed Limit is: " + limit + " ");
            controlsOutput.getChildren().add(speedLimitText);

        });

        // Set number of lanes to amount user has inputted
        setLanes.setOnMouseClicked(event -> {

            int lanesNumber = Integer.parseInt(lanes.getText());

            environment.setLanes(lanesNumber);

            laneNumberText.setText("Number of lanes is: " + lanesNumber + " ");
            controlsOutput.getChildren().add(laneNumberText);

        });

        // Generate speed analysis of cars on motorway
        speedAnalysisButton.setOnMouseClicked(event -> {

            speedAnalysisText.setText(environment.speedAnalysis());
            speedAnalysis.getChildren().add(speedAnalysisText);

        });

    }

    /**
     * Add the required cars to an environment.
     * 
     * @param e
     *            Environment to use.
     */
    private void addCars(Environment e) {

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
