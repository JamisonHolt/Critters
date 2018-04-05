package assignment4;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;


public class Controller {
    private boolean run;

    @FXML
    private TextField makeTextField;

    @FXML
    private ChoiceBox makeChoiceBox;

    @FXML
    private Button makeButton;

    @FXML
    private TextField timeStepTextField;

    @FXML
    private Button timeStepButton;

    @FXML
    private TextField setSeedTextField;

    @FXML
    private Button setSeedButton;

    @FXML
    private Button displayWorldButton;

    @FXML
    private Button displayStatsButton;

    @FXML
    private Button startAnimationButton;

    @FXML
    private TextField animationTextField;

    @FXML
    private Button stopAnimationButton;

    @FXML
    private Button exitSimulationButton;

    @FXML
    public void initialize() {
        run = false;
        makeChoiceBox.getItems().addAll(
                "Algae",
                "Craig",
                "Critter1",
                "Critter2"
        );
        makeChoiceBox.setValue("Algae");
    }

    public void runMake() throws InvalidCritterException {
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
        // Use our old console model for debugging purposes
        Critter.displayWorld();
    }

    public void runDisplayStats() {
//        Critter.runStats();
        return;
    }

    public void runStartAnimation() {
        return;
    }

    public void runStopAnimation() {
        return;
    }

    public void runExitSimulation() {
        System.exit(0);
    }
}