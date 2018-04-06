package assignment4;

/*
 * Do not change or submit this file.
 */
import assignment4.Critter.TestCritter;

import java.awt.*;
import javafx.scene.shape.Shape;
import javafx.scene.paint.Color;

public class Algae extends TestCritter {

	@Override
	public CritterShape viewShape() {
		return CritterShape.CIRCLE;
	}

	@Override
	public Color viewOutlineColor() {
		return Color.BLACK;
	}

	@Override
	public Color viewFillColor() {
		return Color.GREEN;
	}

	public String toString() { return "@"; }

	public boolean fight(String not_used) { return false; }
	
	public void doTimeStep() {
		setEnergy(getEnergy() + Params.photosynthesis_energy_amount);
	}
}
