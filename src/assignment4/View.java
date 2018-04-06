/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Jamison Holt
 * Jah7327
 * 15455
 * Spring 2018
 */

package assignment4;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.paint.Color;
import assignmnet4.Polygons.*;
import javafx.scene.text.Text;
import javafx.scene.Group;
import javafx.scene.Scene;

import java.util.List;
import java.util.ArrayList;

public class View {
	public static String statsCritter;

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

	public static void paintStats() throws Exception {
		List<Critter> population = Critter.TestCritter.getPopulation();
		StringBuilder allStats = new StringBuilder();
		allStats.append(Critter.runStats(population));
		Class<?> toUse = Class.forName("assignment4." + statsCritter);
		toUse.getClass();
		List<Critter> thisCritsType = new ArrayList<Critter>();
		for (Critter crit : population) {
			if (toUse.isInstance(crit)) {
				thisCritsType.add(crit);
			}
		}
		allStats.append(toUse.getMethod("runStats", List.class).invoke(null, thisCritsType));
		Main.statsPane.setScene(new Scene(new Group(new Text("\n\n" + allStats.toString()))));
	}
}
