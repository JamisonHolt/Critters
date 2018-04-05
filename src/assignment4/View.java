package assignment4;

/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2015
 */

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.List;

public class View {

	/*
	 * Returns a square or a circle, according to shapeIndex
	 */
	static Shape getIcon(int shapeIndex) {
		Shape s = null;
		double size = assignment4.Main.tileSideLength;
		
		switch(shapeIndex) {
		case 0: s = new Rectangle(size, size); 
			s.setFill(javafx.scene.paint.Color.RED); break;
		case 1: s = new Circle(size/2); 
			s.setFill(javafx.scene.paint.Color.GREEN); break;
		}
		// set the outline of the shape
		s.setStroke(javafx.scene.paint.Color.BLUE); // outline
		return s;
	}
	
	/*
	 * Paints the shape on a grid.
	 */
	public static void paintWorld() {
		Main.grid.getChildren().clear(); // clean up grid.

		// Find all currently living critters and add to a grid for output
		List<Critter> population = assignment4.Critter.TestCritter.getPopulation();
		Critter[][] outputGrid = new Critter[Params.world_height][Params.world_width];
		for (Critter crit : population) {
			outputGrid[crit.getY_coord()][crit.getX_coord()] = crit;
		}

		for (int row=0; row<Params.world_height; row++) {
			for (int col = 0; col < Params.world_width; col++) {
				if (outputGrid[row][col] != null) {
					Main.grid.add(getIcon(1), row, col);
				} else {
					Main.grid.add(getIcon(0), row, col);
				}
			}
		}
	}
}
