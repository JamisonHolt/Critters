package assignment4;

import assignment4.Critter.TestCritter;

/**
 * Critter with a preferred direction and way of tracking whether it has moved
 * Moves randomly, and will not run from a fight if it has moved
 */
public class Holt extends TestCritter {

    public int preferred_dir;
    private boolean hasMoved;

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
        if (enemy.equals("C") || enemy.equals("@")) {
            // Holts will only try to fight Craigs and Algae
            return true;
        } else if (enemy.equals("H") && !(this.hasMoved)) {
            // Holts are most afraid of their own kind - run and update preferred direction
            for (int i=0; i<8; i++) {
                int[] new_coords = this.look(2, this.preferred_dir);
                int row = new_coords[0];
                int col = new_coords[1];
                return false;
            }
        } else if (enemy.equals("J") && !(this.hasMoved)) {
            // Holts are only a bit afraid of Jamisons - walk
            int fleeTo = this.preferred_dir;
            for (int i=0; i<8; i++) {
                int[] new_coords = this.look(1, fleeTo);
                int row = new_coords[0];
                int col = new_coords[1];
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
        // If possible, run and add a new Holt in a different preferred direction
        if(this.getEnergy() > Params.min_reproduce_energy + Params.run_energy_cost) {
            this.hasMoved = true;
            this.run(this.preferred_dir);
            Holt baby = new Holt();
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
     * @param holts List of holt objects to inspect
     */
    public static void runStats(java.util.List<Critter> holts) {
        // Create array of directions to display percentages of
        int[] preferred_dirs = {0, 0, 0, 0, 0, 0, 0, 0};
        for (Critter holt : holts) {
            preferred_dirs[((Holt) holt).preferred_dir] += 1;
        }

        // Convert each count to a percentage
        if (holts.size() > 0) {
            for (int i=0; i<8; i++) {
                preferred_dirs[i] = preferred_dirs[i] * 100 / holts.size();
            }
        }

        // Print out the stats by iterating through the array of percentages
        System.out.print("" + holts.size() + " total Holts    ");
        System.out.print("Preferred Direction percentages: ");
        if (holts.size() > 0) {
            for (int i=0; i<8; i++) {
                System.out.print(i + ":" + preferred_dirs[i] + "%,  ");
            }
        } else {
            System.out.print("None - all are dead :(");
        }
        System.out.println();
    }
}
