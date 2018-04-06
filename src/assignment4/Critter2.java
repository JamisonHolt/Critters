/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Jamison Holt
 * Jah7327
 * 15455
 * Spring 2018
 */

package assignment4;

import assignment4.Critter.TestCritter;

import javafx.scene.paint.Color;

/**
 * Critter with a preferred direction and way of tracking whether it has moved
 * Moves randomly, and will not run from a fight if it has moved
 */
public class Critter2 extends TestCritter {

    public int preferred_dir;
    private boolean hasMoved;

    /**
     * Instantiates a Critter2 object with a random "preferred" direction
     */
    public Critter2() {
        // Assign a random, "preferred" direction, which our Critter will tend to
        this.preferred_dir = assignment4.Critter.getRandomInt(8);
    }

    @Override
    public CritterShape viewShape() {
        return CritterShape.DIAMOND;
    }

    @Override
    public Color viewOutlineColor() {
        return Color.BISQUE;
    }

    @Override
    public Color viewFillColor() {
        return Color.SLATEBLUE;
    }

    /**
     * Get the string representation of a Critter2
     *
     * @return the String H
     */
    @Override
    public String toString() {return "2";}

    /**
     * Fight method for Holts - run away from each other, fight Craigs/Algae, walk
     * from Jamisons
     *
     * @param enemy The emeny to contemplate fighting
     * @return whether the critter will fight or not
     */
    @Override
    public boolean fight(String enemy) {
        if (enemy.equals("C") || enemy.equals("@")) {
            // Holts will only try to fight Craigs and Algae
            return true;
        } else if (enemy.equals("2") && !(this.hasMoved)) {
            // Holts are most afraid of their own kind - run and update preferred direction
            if (this.look(this.preferred_dir, true) == null) {
                this.run(this.preferred_dir);
            }
            return false;
        } else if (enemy.equals("1") && !(this.hasMoved)) {
            // Holts are only a bit afraid of Jamisons - walk
            if (this.look(this.preferred_dir, false) == null) {
                this.walk(this.preferred_dir);
            }
            return false;
        }
        return true;
    }

    /**
     * Run a time step for a holt - run and reproduce if possible, else walk
     * Always moves in current preferred direction
     * Babies will always have the next clockwise direction
     */
    @Override
    public void doTimeStep() {
        this.hasMoved = false;
        // If possible, run and add a new Critter2 in a different preferred direction
        if(this.getEnergy() > Params.min_reproduce_energy + Params.run_energy_cost) {
            this.hasMoved = true;
            this.run(this.preferred_dir);
            Critter2 baby = new Critter2();
            baby.preferred_dir = (this.preferred_dir + 1) % 8;
            this.reproduce(baby, this.preferred_dir);
        } else {
            // Only walk 50% of time
            if (assignment4.Critter.getRandomInt(2) == 1) {
                this.hasMoved = true;
                this.walk(this.preferred_dir);
            }
        }

    }

    /**
     * Show how many Holts exist, along with how many prefer each direction
     *
     * @param critter2s List of holt objects to inspect
     * @return the string of this critters stats
     */
    public static String runStats(java.util.List<Critter> critter2s) {
        // Create array of directions to display percentages of
        int[] preferred_dirs = {0, 0, 0, 0, 0, 0, 0, 0};
        for (Critter holt : critter2s) {
            preferred_dirs[((Critter2) holt).preferred_dir] += 1;
        }

        // Convert each count to a percentage
        if (critter2s.size() > 0) {
            for (int i=0; i<8; i++) {
                preferred_dirs[i] = preferred_dirs[i] * 100 / critter2s.size();
            }
        }

        StringBuilder stats = new StringBuilder();
        // Print out the stats by iterating through the array of percentages
        stats.append("" + critter2s.size() + " total Critter2s    ");
        stats.append("Preferred Direction percentages: ");
        if (critter2s.size() > 0) {
            for (int i=0; i<8; i++) {
                stats.append(i + ":" + preferred_dirs[i] + "%,  ");
            }
        } else {
            stats.append("None - all are dead :(");
        }
        stats.append("\n");
        return stats.toString();
    }
}
