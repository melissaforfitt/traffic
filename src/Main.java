import java.nio.file.Paths;
import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    boolean overtakingOn = true;
    boolean undertakingOn = true;
    boolean badLaneDisciplineOn = false;
    boolean wetRoadsOn = false;

    // Combo boxes for user selection
    public ComboBox<String> overtakingBox = new ComboBox<String>();
    public ComboBox<String> undertakingBox = new ComboBox<String>();
    public ComboBox<String> laneDisciplineBox = new ComboBox<String>();
    public ComboBox<String> wetRoadsBox = new ComboBox<String>();

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

        // Set up all of the GUI interface
        HBox controls1 = new HBox();
        HBox controls2 = new HBox();
        HBox controlsOutput = new HBox();
        HBox speedAnalysis = new HBox();
        Button restart = new Button("Restart");
        Button accelerate = new Button("Accelerate");
        TextField speedLimit = new TextField();
        speedLimit.setPrefColumnCount(3);
        Button setSpeed = new Button("Set Speed Limit");
        TextField lanes = new TextField();
        lanes.setPrefColumnCount(3);
        Button setLanes = new Button("Set Lane Amount");
        Button speedAnalysisButton = new Button("Speed Analysis");
        controls1.getChildren().add(restart);
        controls1.getChildren().add(accelerate);
        controls1.getChildren().add(overtakingBox);
        controls1.getChildren().add(undertakingBox);
        controls1.getChildren().add(laneDisciplineBox);
        controls1.getChildren().add(wetRoadsBox);
        controls2.getChildren().add(speedLimit);
        controls2.getChildren().add(setSpeed);
        controls2.getChildren().add(lanes);
        controls2.getChildren().add(setLanes);
        speedAnalysis.getChildren().add(speedAnalysisButton);
        box.getChildren().add(controls1);
        box.getChildren().add(controls2);
        box.getChildren().add(controlsOutput);
        box.getChildren().add(speedAnalysis);
        controlsOutput.getChildren().add(overtakingText);
        controlsOutput.getChildren().add(speedLimitText);
        controlsOutput.getChildren().add(laneNumberText);
        speedAnalysis.getChildren().add(speedAnalysisText);

        // When restart button is clicked, refresh motorway to starting positions
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

            environment.accelerateAllCars();

        });

        // Set up drop down list for user to select if overtaking is on/off
        overtakingBox.setPromptText("Overtaking");
        overtakingBox.getItems().addAll("On", "Off");

        overtakingBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {

            if (newValue == "On") {
                environment.setOvertaking(true);
            } else if (newValue == "Off") {
                environment.setOvertaking(false);
            }

        });

        // Set up drop down list for user to select if undertaking is on/off
        undertakingBox.setPromptText("Undertaking");
        undertakingBox.getItems().addAll("On", "Off");

        undertakingBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {

            if (newValue == "On") {
                environment.setUndertaking(true);
            } else if (newValue == "Off") {
                environment.setUndertaking(false);
            }

        });

        // Set up drop down list for user to select if bad lane discipline is on/off
        laneDisciplineBox.setPromptText("Bad Lane Discipline");
        laneDisciplineBox.getItems().addAll("On", "Off");

        laneDisciplineBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {

            if (newValue == "On") {
                environment.setLaneDiscipline(true);
            } else if (newValue == "Off") {
                environment.setLaneDiscipline(false);
            }

        });

        // Set up drop down list for user to select if wet road is on/off
        wetRoadsBox.setPromptText("Wet Roads");
        wetRoadsBox.getItems().addAll("On", "Off");

        wetRoadsBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {

            if (newValue == "On") {
                environment.setBrakingEfficiency(true);
            } else if (newValue == "Off") {
                environment.setBrakingEfficiency(false);
            }

        });

        // If speed limit button is clicked, add restrictions to all car speeds
        setSpeed.setOnMouseClicked(event -> {

            int limit = Integer.parseInt(speedLimit.getText());

            environment.setSpeedLimit(limit);

            speedLimitText.setText("Speed Limit is: " + limit + " ");

        });

        // Set number of lanes to amount user has inputted
        setLanes.setOnMouseClicked(event -> {

            int lanesNumber = Integer.parseInt(lanes.getText());

            environment.setLanes(lanesNumber);

            laneNumberText.setText("Number of lanes is: " + lanesNumber + " ");

        });

        // Generate speed analysis of cars on motorway
        speedAnalysisButton.setOnMouseClicked(event -> {

            speedAnalysisText.setText(environment.speedAnalysis());

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

        e.add(new Car(0, 63, 2, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false, false, false,
                null));

        e.add(new Car(80, 79, 0, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false, false, false,
                null));

        e.add(new Car(148, 60, 3, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false, false, false,
                null));

        e.add(new Car(192, 74, 0, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false, false, false,
                null));

        e.add(new Car(240, 12, 1, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false, false, false,
                null));

        e.add(new Car(288, 77, 2, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false, false, false,
                null));

        e.add(new Car(396, 28, 1, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false, false, false,
                null));

    }

    private void trafficSounds() {

        // Find traffic sound effect file and play it
        final Media sounds = new Media(Paths.get("traffic-sounds.wav").toUri().toString());
        player = new MediaPlayer(sounds);
        player.play();

        player.setVolume(10.0);

    }

};
