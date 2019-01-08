import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
        controls.getChildren().add(restart);
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

        // TODO: CLICK ON A CAR TO ANALYSE ITS SPEED
        // speedAnalysis(Car clicked)

        // TODO: FIX NULL POINTER EXCEPTION FOR CARS CRASHING
        /*
         * for (Car c : carArray) { if (environment.collisionCheck(c) == true) {
         * System.out.println("Car crashed"); } }
         */
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

        carArray.add(new Car(0, 63, 2, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false));
        e.add(carArray.get(0));

        carArray.add(new Car(48, 79, 0, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false));
        e.add(carArray.get(1));

        carArray.add(new Car(144, 60, 0, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false));
        e.add(carArray.get(2));

        carArray.add(new Car(192, 74, 0, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false));
        e.add(carArray.get(3));

        carArray.add(new Car(240, 12, 1, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false));
        e.add(carArray.get(4));

        carArray.add(new Car(288, 77, 0, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false));
        e.add(carArray.get(5));

        carArray.add(new Car(336, 28, 1, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false));
        e.add(carArray.get(6));

        carArray.add(new Car(384, 32, 2, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false));
        e.add(carArray.get(7));

        carArray.add(new Car(432, 16, 1, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0), false));
        e.add(carArray.get(8));

    }

    private void trafficSounds() {

        // Find traffic sound effect file and play it
        final Media sounds = new Media(Paths.get("traffic-sounds.wav").toUri().toString());
        player = new MediaPlayer(sounds);
        player.play();

        player.setVolume(10.0);

    }

};
