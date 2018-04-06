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
import javafx.scene.paint.Color;
import assignmnet4.Polygons.*;

import java.util.List;

public class View {

	/*
	 * Returns a square or a circle, according to shapeIndex
	 */
	static Shape getIcon(CritterShape shape, Color outlineColor, Color fillColor) {
		Shape s = null;
		double size = assignment4.Main.tileSideLength;
		
		switch(shape) {
		case SQUARE:
			s = new Rectangle(size, size);
			break;
		case CIRCLE:
			s = new Circle(size/2);
			break;
		case TRIANGLE:
			s = new Triangle(size);
			break;
		case DIAMOND:
			s = new Diamond(size);
			break;
		}

		// set the outline of the shape
		s.setStroke(outlineColor);
		s.setFill(fillColor);
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
					Main.grid.add(getIcon(crit.viewShape(), crit.viewOutlineColor(), crit.viewFillColor()), row, col);
				}
			}
		}
	}
}
