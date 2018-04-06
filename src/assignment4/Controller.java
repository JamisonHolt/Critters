/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Jamison Holt
 * Jah7327
 * 15455
 * Spring 2018
 */


package assignment4;


import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import java.io.File;
import java.util.HashSet;
import java.util.Arrays;


/**
 * The main controller panel representation for our world
 */
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
    private ChoiceBox displayStatsChoiceBox;

    /**
     * Finds all critter classes that exist in the assignment folder, adds them to our choice boxes, and states that
     * animations are not running currently
     */
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
                if (! toAdd.equals("Algae")) {
                    displayStatsChoiceBox.getItems().addAll(toAdd);
                }
            }
        }

        // Add a listener to update which critter's stats are being viewed, based on what is in the box
        displayStatsChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                View.statsCritter = displayStatsChoiceBox.getItems().get((Integer) number2).toString();
            }
        });

        // set basic interface defaults
        run = false;
        makeChoiceBox.setValue(makeChoiceBox.getItems().get(0));
        displayStatsChoiceBox.setValue(displayStatsChoiceBox.getItems().get(0));
    }

    /**
     * Makes a new critter based on what's in the text box
     *
     * @throws InvalidCritterException
     */
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

    /**
     * Runs the appropriate number of time steps specified
     */
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

    /**
     * Set a new random seed for the world
     */
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

    /**
     * Use our model to display any changes in the world
     */
    public void runDisplayWorld() {
        // Make sure animation is not running
        if (run) {return;}

        // Use our old console model for debugging purposes
        Critter.displayWorld();
    }

    /**
     * Use our model to display the necessary stats of the world
     */
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

    /**
     * Start animating the world by adding an animation timer and locking all other functions
     */
    public void runStartAnimation() {
        // Make sure to avoid multiple timers
        if (run) {return;}

        run = true;
        timer = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override public void handle(long time) {
                // Only run while this has not been stoppped
                if (run) {
                    // Retrieve the current FPS
                    int FPS;
                    try {
                        FPS = Integer.parseInt(animationTextField.getCharacters().toString());
                    } catch (Exception e) {
                        // Allow user to enter new text if previous wasn't valid
                        FPS = 1;
                    }

                    // Run the appropriate number of time steps in the world based on FPS
                    for (int i=0; i<FPS; i++) {
                        Critter.worldTimeStep();
                    }

                    // Allow other functions to be run by turning off "run" blocking, and then re-enable
                    run = false;
                    runDisplayWorld();
                    runDisplayStats();
                    run = true;
                }
            }
        };
        // Start our timer
        timer.start();
    }

    /**
     * Stops animation by disabling the timer and unblocking other fxns
     */
    public void runStopAnimation() {
        // Make sure there exists a timer to begin with
        if (timer == null) {return;}

        // Allow other functions to work again and stop current timer
        run = false;
        timer.stop();
    }

    /**
     * Exits the simulation for good
     */
    public void runExitSimulation() {
        // Make sure animation is not running
        if (run) {return;}

        System.exit(0);
    }
}