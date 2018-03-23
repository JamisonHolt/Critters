package assignment4;
/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * Jamison Holt
 * Jah7327
 * 15455
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2016
 */


import java.util.*;

public abstract class Critter {
	private static String myPackage;
	private	static HashSet<Critter> population = new HashSet<Critter>();
    private static Critter[][] conflictGrid = new Critter[Params.world_height][Params.world_width];
    private static List<Critter> babies = new ArrayList<Critter>();
	private static java.util.Random rand = new Random();

	private int energy = 0;
	private int x_coord;
	private int y_coord;
	private boolean moved = false;
	private boolean fighting = false;

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
	    myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}

	public static int getRandomInt(int max) {return rand.nextInt(max);}

	public static void setSeed(long new_seed) {rand = new java.util.Random(new_seed);}

	public String toString() {return "";}

	protected int getEnergy() {return energy;}

    public abstract void doTimeStep();

    public abstract boolean fight(String oponent);

    /**
     * Return cleaned coordinates a certain number of steps in a direction
     *
     * @param numSteps number of steps to move in a direction
     * @param direction representation of the direction to move
     * @return int[] with two coordinates representing y and x coords respectively
     */
	protected final int[] look(int numSteps, int direction) {
        // Create a map for making sure directions get updated correctly
        HashMap<String, List<Integer>> dirMap= new HashMap<>();
        dirMap.put("Up", Arrays.asList(1, 2, 3));
        dirMap.put("Down", Arrays.asList(5, 6, 7));
        dirMap.put("Right", Arrays.asList(0, 1, 7));
        dirMap.put("Left", Arrays.asList(3, 4, 5));

        // Decide how the critter will move based on the direction map
        int colChange = 0;
        int rowChange = 0;
        rowChange = dirMap.get("Up").contains(direction) ? 1 : rowChange;
        rowChange = dirMap.get("Down").contains(direction) ? -1 : rowChange;
        colChange = dirMap.get("Right").contains(direction) ? 1 : colChange;
        colChange = dirMap.get("Left").contains(direction) ? -1 : colChange;
        int new_y_coord = this.y_coord + rowChange * numSteps;
        int new_x_coord = this.x_coord + colChange * numSteps;

        // Make sure new position is valid through bounds checking
        if (new_x_coord < 0) {
            new_x_coord = assignment4.Params.world_width + new_x_coord;
        } else if (new_x_coord >= assignment4.Params.world_width) {
            new_x_coord = new_x_coord - assignment4.Params.world_width;
        }
        if (new_y_coord < 0) {
            new_y_coord = assignment4.Params.world_height + new_y_coord;
        } else if (new_y_coord >= assignment4.Params.world_height) {
            new_y_coord = new_y_coord - assignment4.Params.world_height;
        }
        return new int[]{new_y_coord, new_x_coord};
    }

    /**
     * Helper function to allow code sharing between run and walk functions
     * Moves a critter a desired number of steps in a direction, if possible.
     * Subtracts energy regardless of whether a movement is possible
     *
     * @param numSteps Number of steps to move in a direction
     * @param energyCost Cost of moving that number of steps
     * @param direction Number representation of the direction to move
     */
	protected final void move(int numSteps, int energyCost, int direction) {
        // Check if Critter has moved previously and update energy if so
        this.energy -= energyCost;
        if (this.moved) {
            return;
        }
        this.moved = true;

        // Find new coordinate and move critter to location, if not fighting and unoccupied
        int[] new_coords = look(numSteps, direction);
        if (this.fighting) {
            for (Critter crit : Critter.population) {
                if (crit.y_coord == new_coords[0] && crit.x_coord == new_coords[1] && crit.energy > 0) {
                    return;
                }
            }
            // If fighting and change of coordinates, update our conflict-tracking grid
            conflictGrid[new_coords[0]][new_coords[1]] = this;
        }
        this.y_coord = new_coords[0];
        this.x_coord = new_coords[1];
	}

    /**
     * Moves a critter one space in a given direction, if possible
     * Subtracts the walk energy cost no matter what
     *
     * @param direction
     */
	protected final void walk(int direction) {
	    move(1, assignment4.Params.walk_energy_cost, direction);
	}

    /**
     * Moves a critter two spaces in a given direction, if possible
     * Subtracts the walk energy cost no matter what
     *
     * @param direction
     */
	protected final void run(int direction) {
		move(2, assignment4.Params.run_energy_cost, direction);
	}

    /**
     * Allows a critter object to produce an offspring in the given direction
     * adjacent to it. Gives half of the parent's energy to the child
     *
     * @param offspring The offspring to be added to the population
     * @param direction The direction from the parent the baby will be put
     */
	protected final void reproduce(Critter offspring, int direction) {
	    // Confirm that "parent" critter has enough energy to reproduce
        if (this.energy <= assignment4.Params.min_reproduce_energy) {return;}

        // Assign child energy to half, rounded down. Parent loses half, round ups
        offspring.energy = this.energy / 2;
        this.energy -= offspring.energy;

        // Update child's location using adjacent square to parent
        offspring.x_coord = this.x_coord;
        offspring.y_coord = this.y_coord;
        int[] new_coords = offspring.look(1, direction);
        offspring.y_coord = new_coords[0];
        offspring.x_coord = new_coords[1];

        // Add to babies collection, to be added to pop at end of cycle
	    babies.add(offspring);
	}


	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws Exception
	 */
	public static void makeCritter(String critter_class_name) throws assignment4.InvalidCritterException {
		// Create new Critter based on class name provided - throw error if invalid
		Critter added = new assignment4.Algae();
		try {
			added = (Critter) Class.forName("assignment4." + critter_class_name).newInstance();
		} catch (ClassNotFoundException e) {
			throw new assignment4.InvalidCritterException("Invalid Critter class name!");
		} catch (IllegalAccessException e) {
            throw new assignment4.InvalidCritterException("Invalid Critter class name!");
		} catch (InstantiationException e) {
            throw new assignment4.InvalidCritterException("Invalid Critter class name!");
		}

		// Move Critter to random position in the world
        added.energy = assignment4.Params.start_energy;
		added.y_coord = getRandomInt(Params.world_height);
		added.x_coord = getRandomInt(Params.world_width);
        population.add(added);
	}

	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
        // Check if critter is requested class type, and add to result if possible
		List<Critter> result = new java.util.ArrayList<Critter>();
		for (Critter crit : population) {
		    boolean found = false;
		    try {
                found = Class.forName(Critter.myPackage + "." + critter_class_name) == crit.getClass();
            } catch (Exception e) {
		        e.printStackTrace();
            }
		    if (found) {
		        result.add(crit);
            }
		}
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 *
     * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}

	/* the TestCritter class allows some critters to "cheat". If you want to
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here.
	 *
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {

		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}

		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}

		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}

		protected int getX_coord() {
			return super.x_coord;
		}

		protected int getY_coord() {
			return super.y_coord;
		}

		protected static List<Critter> getBabies() { return babies; }

		protected static List<Critter> getPopulation() {
			return (List) new ArrayList<Critter>(population);
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		population = new HashSet<Critter>();
	}

    /**
     * Resolves a conflict between two critters, and returns the "winner,"
     * or critter that will keep the spot, if one exists
     *
     * @param contender One of the critters to fight
     * @param defender The other critter to fight
     * @return Critter representing the winner of the fight, if one exists
     */
	private static Critter resolve(Critter contender, Critter defender) {
	    // Make sure current "residing" critter is alive
        if (defender.energy <= 0) {
            return contender;
        } else if (contender.energy <= 0) {
            return defender;
        }

        // Check if each critter decides to fight or not
        int oldX = contender.x_coord;
        int oldY = contender.y_coord;
        boolean contenderFight = contender.fight(defender.toString());
        boolean defenderFight = defender.fight(contender.toString());

        // Check if Critter's no longer fighting
        boolean sameY = contender.y_coord == defender.y_coord;
        boolean sameX = contender.x_coord == defender.x_coord;

        // If one creature moved, add correct critter to this grid spot. Else, add nothing
        if (!(sameY && sameX)) {
            if (contender.x_coord == oldX && contender.y_coord == oldY) {
                return contender;
            } else if (defender.x_coord == oldX && defender.y_coord == oldY) {
                return defender;
            } else {
                return null;
            }
        }

        // Check surrender condition
        if ((contenderFight != defenderFight) && sameY && sameX) {
            Critter winner = contenderFight ? contender : defender;
            Critter loser = contenderFight ? defender : contender;
            winner.energy += loser.energy / 2;
            return winner;
        }

        // Check if critter died trying to run away - if so, fight resolved
		if (contender.energy <= 0 && defender.energy <= 0) {
		    return null;
        } else if (contender.energy <= 0) {
		    return defender;
        } else if (defender.energy <= 0) {
		    return contender;
        }

        // Roll off between contender and defender to decide winner
        int contRoll = getRandomInt(contender.energy);
        int defRoll = getRandomInt(defender.energy);
        if (contRoll >= defRoll) {
            contender.energy += defender.energy / 2;
            defender.energy = 0;
            return contender;
        } else {
            defender.energy += contender.energy / 2;
            contender.energy = 0;
            return defender;
        }
	}

    /**
     * Runs a time step for every creature, resolves conflicts between
     * critters on the same space, updates rest energy, spawns algae,
     * adds babies and removes dead critters all in the same turn
     */
	public static void worldTimeStep() {
	    // TODO: Make sure critters don't move twice within same timestep
		// 2) Run a time step for each critter, making sure to reset situational properties
		for (Critter crit : Critter.population) {
		    crit.moved = false;
		    crit.fighting = false;
			crit.doTimeStep();
		}

		// 3) Resolve conflicts between critters in same grid positions
        conflictGrid = new Critter[Params.world_height][Params.world_width];
		for (Critter crit : population) {
			int col = crit.x_coord;
			int row = crit.y_coord;
			if (conflictGrid[row][col] != null) {
			    conflictGrid[row][col] = resolve(crit, conflictGrid[row][col]);
			} else {
			    conflictGrid[row][col] = crit;
			}
		}

		// 4) Update Rest Energy
		for (Critter crit : population) {
            crit.energy -= assignment4.Params.rest_energy_cost;
        }

		// 5) Add new Algae to the world
        for (int i=0; i< assignment4.Params.refresh_algae_count; i++) {
            try {
                makeCritter("Algae");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

		// 6) Add babies and then remove dead critters
		population.addAll(babies);
		babies.clear();
		List<Critter> morgue = new ArrayList<Critter>();
		for (Critter crit : population) {
			if (crit.energy <= 0) {
				morgue.add(crit);
			}
		}
		for (Critter dead : morgue) {
			population.remove(dead);
		}
		morgue.clear();
	}

    /**
     * Displays the current world using ascii characters
     */
	public static void displayWorld() {
		// Create array to store Critters
		Critter[][] outputGrid = new Critter[Params.world_height][Params.world_width];
		for (Critter crit : population) {
			outputGrid[crit.y_coord][crit.x_coord] = crit;
		}
		// Iterate through borders, rows, and cols
		for (int row=-1; row<=Params.world_height; row++) {
			for (int col=-1; col<=Params.world_width; col++) {
				if ((row==-1 || row==Params.world_height) && (col==-1 || col==Params.world_width)) {
					System.out.print("+");
				} else if (row==-1 || row==Params.world_height) {
					System.out.print("-");
				} else if (col==-1 || col==Params.world_width) {
					System.out.print("|");
				} else if (outputGrid[row][col] != null) {
					System.out.print(outputGrid[row][col].toString());
				} else {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}
}
