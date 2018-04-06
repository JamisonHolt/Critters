package assignment4;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import java.io.File;
import java.util.HashSet;
import java.util.Arrays;


public class Controller {
    private boolean run;
    private AnimationTimer timer;

    @FXML
    private TextField makeTextField;

    @FXML
    private ChoiceBox makeChoiceBox;

    @FXML
    private TextField timeStepTextField;

    @FXML
    private TextField setSeedTextField;

    @FXML
    private TextField animationTextField;

    @FXML
    public void initialize() {
        // Find all new Critter classes
        HashSet<String> ignore = new HashSet<String>(Arrays.asList(
                "Controller.java", "Controls.fxml", "Critter.java",
                "CritterShape.java", "Header.java", "InvalidCritterException.java",
                "Main.java", "Params.java", "Polygons.java", "View.java"
        ));
        File folder = new File("./src/assignment4/");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (!(ignore.contains(file.getName().toString()))) {
                String toAdd = file.getName();
                toAdd = toAdd.substring(0, toAdd.length()-5);
                makeChoiceBox.getItems().addAll(toAdd);
            }
        }

        run = false;
        makeChoiceBox.setValue(makeChoiceBox.getItems().get(0));
    }

    public void runMake() throws InvalidCritterException {
        // Make sure animation is not running
        if (run) {return;}

        int numToMake;
        // Make sure that a valid number is entered
        try {
            numToMake = Integer.parseInt(makeTextField.getCharacters().toString());
        } catch (Exception e) {
            // Allow user to enter new text if previous wasn't valid
            makeTextField.setText("");
            makeTextField.setPromptText("Error");
            numToMake = 0;
        }

        // Create the required number of critters
        for (int i=0; i<numToMake; i++) {
            Critter.makeCritter(makeChoiceBox.getValue().toString());
        }
    }

    public void runTimeStep() {
        // Make sure animation is not running
        if (run) {return;}

        int numSteps;
        // Make sure that a valid number is entered
        try {
            numSteps = Integer.parseInt(timeStepTextField.getCharacters().toString());
        } catch (Exception e) {
            // Allow user to enter new text if previous wasn't valid
            timeStepTextField.setText("");
            timeStepTextField.setPromptText("Error");
            numSteps = 0;
        }

        // Step the required number of times
        for (int i=0; i<numSteps; i++) {
            Critter.worldTimeStep();
        }
    }

    public void runSetSeed() {
        // Make sure animation is not running
        if (run) {return;}

        long seedNum;
        // Make sure a valid seed number is entered
        try {
            seedNum = Long.parseLong(setSeedTextField.getCharacters().toString());
        } catch (Exception e) {
            // Allow user to enter new text if previous wasn't valid
            setSeedTextField.setText("");
            setSeedTextField.setPromptText("Error");
            seedNum = 0;
        }

        // Set our new seed number
        Critter.setSeed(seedNum);
    }

    public void runDisplayWorld() {
        // Make sure animation is not running
        if (run) {return;}

        // Use our old console model for debugging purposes
        Critter.displayWorld();
    }

    public void runDisplayStats() {
        // Make sure animation is not running
        if (run) {return;}
        try {
            View.paintStats();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public void runStartAnimation() {
        // Make sure to avoid multiple timers
        if (run) {return;}

        run = true;
        timer = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override public void handle(long time) {
                if (run) {
                    int FPS;
                    try {
                        FPS = Integer.parseInt(animationTextField.getCharacters().toString());
                    } catch (Exception e) {
                        // Allow user to enter new text if previous wasn't valid
                        FPS = 1;
                    }
                    for (int i=0; i<FPS; i++) {
                        Critter.worldTimeStep();
                    }
                    run = false;
                    runDisplayWorld();
                    runDisplayStats();
                    run = true;
                }
            }
        };
        timer.start();
    }

    public void runStopAnimation() {
        // Make sure there exists a timer to begin with
        if (timer == null) {return;}

        // Allow other functions to work again and stop current timer
        run = false;
        timer.stop();
    }

    public void runExitSimulation() {
        // Make sure animation is not running
        if (run) {return;}

        System.exit(0);
    }
}