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
import assignmnet4.Polygons.*;

import java.util.List;

public class View {

	/*
	 * Returns a square or a circle, according to shapeIndex
	 */
	static Shape getIcon(CritterShape shape) {
		Shape s = null;
		double size = assignment4.Main.tileSideLength;
		
		switch(shape) {
		case SQUARE: s = new Rectangle(size, size);
			s.setFill(javafx.scene.paint.Color.RED); break;
		case CIRCLE: s = new Circle(size/2);
			s.setFill(javafx.scene.paint.Color.GREEN); break;
		case TRIANGLE: s = new Triangle(size);
			break;
		case DIAMOND: s = new Diamond(size);
			break;
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

		int curr = 0;
		for (int row=0; row<Params.world_height; row++) {
			for (int col = 0; col < Params.world_width; col++) {
				curr = (curr + 1) % 4;
				if (outputGrid[row][col] != null) {
					Critter crit = outputGrid[row][col];
					Main.grid.add(getIcon(crit.viewShape()), row, col);
				} else {
					Main.grid.add(getIcon(CritterShape.SQUARE), row, col);
				}
			}
		}
	}
}
