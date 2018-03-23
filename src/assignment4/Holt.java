package assignment4;

import assignment4.Critter.TestCritter;

public class Holt extends TestCritter {

    public int preferred_dir;

    /**
     * Instantiates a Holt object with a random "preferred" direction
     */
    public Holt() {
        // Assign a random, "preferred" direction, which our Critter will tend to
        this.preferred_dir = assignment4.Critter.getRandomInt(8);
    }

    /**
     * Get the string representation of a Holt
     *
     * @return the String H
     */
    @Override
    public String toString() {return "H";}

    /**
     * Fight method for Holts - run away from each other, fight Craigs/Algae, walk
     * from Jamisons
     *
     * @param enemy The emeny to contemplate fighting
     * @return
     */
    @Override
    public boolean fight(String enemy) {
        Critter[][] conflictGrid = getConflictGrid();
        if (enemy.equals("C") || enemy.equals("@")) {
            // Holts will only try to fight Craigs and Algae
            return true;
        } else if (enemy.equals("H")) {
            // Holts are most afraid of their own kind - run and update preferred direction
            for (int i=0; i<8; i++) {
                int[] new_coords = this.look(2, this.preferred_dir);
                int row = new_coords[0];
                int col = new_coords[1];
                if (conflictGrid[row][col] != null) {
                    this.preferred_dir = (this.preferred_dir + 1) % 8;
                }
            }
        } else if (enemy.equals("J")) {
            // Holts are only a bit afraid of Jamisons - walk
            int fleeTo = this.preferred_dir;
            for (int i=0; i<8; i++) {
                int[] new_coords = this.look(1, fleeTo);
                int row = new_coords[0];
                int col = new_coords[1];
                if (conflictGrid[row][col] != null) {
                    fleeTo = (fleeTo + 1) % 8;
                }
            }
        }
        return false;
    }

    /**
     * Run a time step for a holt - run and reproduce if possible, else walk
     * Always moves in current preferred direction
     */
    @Override
    public void doTimeStep() {
        // If possible, run and add a new Holt in this critter's current preferred direction
        if(this.getEnergy() > Params.min_reproduce_energy + Params.run_energy_cost) {
            this.run(this.preferred_dir);
            this.reproduce(new Holt(), this.preferred_dir);
        } else {
            this.walk(this.preferred_dir);
        }

    }

    /**
     * Show how many Holts exist, along with how many prefer each direction
     *
     * @param holts List of holt objects to inspect
     */
    public static void runStats(java.util.List<Critter> holts) {
        // Create array of directions to display percentages of
        int[] preferred_dirs = {0, 0, 0, 0, 0, 0, 0, 0};
        for (Critter holt : holts) {
            preferred_dirs[((Holt) holt).preferred_dir] += 1;
        }

        // Convert each count to a percentage
        for (int i=0; i<8; i++) {
            preferred_dirs[i] = preferred_dirs[i] * 100 / holts.size();
        }

        // Print out the stats by iterating through the array of percentages
        System.out.print("" + holts.size() + " total Holts    ");
        System.out.print("Preferred Direction percentages: ");
        for (int i=0; i<8; i++) {
            System.out.print(i + ":" + preferred_dirs[i] + "%,  ");
        }
        System.out.println();
    }
}
