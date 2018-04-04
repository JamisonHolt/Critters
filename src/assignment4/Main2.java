import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;

public class Main2 extends Application {
	static GridPane grid = new GridPane();
	private static double width;
	private static double height;
	private Stage controlPane = new Stage();

	@Override
		public void start(Stage world) {
			try {
				// Show the controller component
				Parent fxml = FXMLLoader.load(getClass().getResource("Controls.fxml"));
				controlPane.setScene(new Scene(fxml));
				controlPane.setX(width - 300);
				controlPane.show();

				// Create our critter grid
				world.setScene(new Scene(grid, width-310, height));
				world.setX(0);
				world.show();

				Painter.paint();

			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	
	public static void main(String[] args) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		width = screen.getWidth();
		height = screen.getHeight();

		grid.setGridLinesVisible(true);
		launch(args);
	}
}
