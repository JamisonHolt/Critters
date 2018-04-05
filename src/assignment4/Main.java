package assignment4;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {
	static GridPane grid = new GridPane();
	private static double screenWidth;
	private static double screenHeight;
	private static double stageSideLength;
	private static double tileSideLength;
	private Stage controlPane = new Stage();

    @Override
    public void start(Stage world) {
        try {
            // Show the controller component
            Parent fxml = FXMLLoader.load(getClass().getResource("../Controls.fxml"));
            controlPane.setScene(new Scene(fxml));
            controlPane.setX(screenWidth - 300);
            controlPane.show();

            // Create our critter grid
            world.setScene(new Scene(grid, screenWidth-310, screenHeight));
            world.setX(0);
            world.setY(0);
            world.show();

            Painter.paint();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

	public static void main(String[] args) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screen.getWidth();
		screenHeight = screen.getHeight();
		stageSideLength = screenWidth < screenHeight ? screenWidth : screenHeight;



		grid.setGridLinesVisible(true);
		launch(args);
	}
}
