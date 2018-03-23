package assignment4;

import assignment4.Critter.TestCritter;

/**
 * Jamison Critters attempt to bulk up, but are otherwise pretty boring/passive
 * This Critter keeps track of the critter with the most energy from last turn,
 * and all critters above that amount can reproduce next turn
 */
public class Jamison extends TestCritter {

    public static int maxEnergy;

    @Override
    public String toString() {return "J";}

    @Override
    public boolean fight(String opponent) {
        // If fighting other creatures, run
        if (opponent.equals("C") || opponent.equals("H")) {
            walk(assignment4.Critter.getRandomInt(8));
            return false;
        }
        // Same or Algae means fight
        return true;
    }

    /**
     * Reproduce if "bulkiest" Jamison and has 2x necessary energy.
     * Also walk/run/stay randomly
     */
    @Override
    public void doTimeStep() {
        if (this.getEnergy() >= maxEnergy && maxEnergy > Params.min_reproduce_energy * 2) {
            this.reproduce(new Jamison(), Critter.getRandomInt(8));
        }
        int random = assignment4.Critter.getRandomInt(9);
        if (random < 4) {
            walk(getRandomInt(8));
        } else if (random == 8) {
            run(getRandomInt(8));
        }
        if (this.getEnergy() > maxEnergy) {
            maxEnergy = this.getEnergy();
        }
    }

    public static void runStats(java.util.List<Critter> jamisons) {
        System.out.print("" + jamisons.size() + " total Jamisons    ");
        System.out.print("Max Jamison Energy: " + maxEnergy);
        System.out.println();
    }
}
