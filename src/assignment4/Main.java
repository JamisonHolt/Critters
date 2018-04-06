package assignment4;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.Group;

import java.awt.*;

public class Main extends Application {
	static public GridPane grid = new GridPane();
    static public Stage statsPane = new Stage();
    public Stage controlPane = new Stage();

	public static double stageWidth;
	public static double stageHeight;
	public static double tileSideLength;

    @Override
    public void start(Stage world) {
        try {
            // Create our critter grid
            world.setScene(new Scene(grid, stageWidth, stageHeight));
            world.setX(0);
            world.setY(0);
            world.show();

            // Check for grid size changes
            world.widthProperty().addListener((obs) -> {
                setStageProps(world.getHeight(), world.getWidth());
                View.paintWorld();
            });
            world.heightProperty().addListener((obs) -> {
                setStageProps(world.getHeight(), world.getWidth());
                View.paintWorld();
            });

            // Show the controller component
            Parent fxml = FXMLLoader.load(getClass().getResource("../Controls.fxml"));
            controlPane.setScene(new Scene(fxml));
            controlPane.setX(stageWidth);
            controlPane.show();
            double controlPaneHeight = controlPane.getHeight();

            // Show the stats component
            statsPane.setX(stageWidth);
            statsPane.setY(controlPaneHeight + 60);
            statsPane.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

	public static void main(String[] args) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setStageProps(screen.getHeight(), screen.getWidth());

		grid.setGridLinesVisible(true);
		launch(args);
	}

    public static void setStageProps(double newHeight, double newWidth) {
        double numHigh = newHeight / Params.world_height;
        double numWide = newWidth / Params.world_width;
        tileSideLength = numHigh < numWide ? numHigh : numWide;
        // Adjust tile Size to deal with outline sizes causing overflow
        double larger = Params.world_height > Params.world_width ? Params.world_height : Params.world_width;
        tileSideLength -= (5 - (larger - 10) / 30);
        stageWidth = tileSideLength * Params.world_width;
        stageHeight = tileSideLength * Params.world_height;
    }
}
