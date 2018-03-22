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
 * Fall 2016
 */


import java.util.*;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private	static HashSet<Critter> population = new HashSet<Critter>();
	private static List<Critter> babies = new ArrayList<Critter>();
	private static java.util.Random rand = new Random();

	private int energy = 0;
	private int x_coord;
	private int y_coord;
	private boolean moved = false;

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}

	/**
	 *
	 * @param max
	 * @return
	 */
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}

	/**
	 *
	 * @param new_seed
	 */
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}

	/**
	 *
	 * @return
	 */
	public String toString() {return "";}

	/**
	 *
	 * @return
	 */
	protected int getEnergy() {return energy;}

    /**
     *
     * @param numSteps
     * @param direction
     * @return
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
        int new_y_coord = rowChange * numSteps;
        int new_x_coord = colChange * numSteps;

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
     *
     * @param numSteps
     * @param energyCost
     * @param direction
     */
	protected final void move(int numSteps, int energyCost, int direction) {
        // TODO: Figure out what it means by "look functions added later"
        // Check if Critter has moved previously and update energy if so
        this.energy -= energyCost;
        if (this.moved) {
            return;
        }
        this.moved = true;

        // Find new coordinate and move critter to location
        int[] new_coords = look(numSteps, direction);
        this.y_coord = new_coords[0];
        this.x_coord = new_coords[1];
	}

    /**
     *
     * @param direction
     */
	protected final void walk(int direction) {
	    move(1, assignment4.Params.walk_energy_cost, direction);
	}

	/**
	 *
	 * @param direction
	 */
	protected final void run(int direction) {
		move(2, assignment4.Params.run_energy_cost, direction);
	}

	/**
	 *
	 * @param offspring
	 * @param direction
	 */
	protected final void reproduce(Critter offspring, int direction) {
	    babies.add(offspring);
	    offspring.walk(direction);
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws Exception {
		// Create new Critter based on class name provided - throw error if invalid
		Critter added = new assignment4.Algae();
		try {
			added = (Critter) Class.forName("assignment4." + critter_class_name).newInstance();
		} catch (ClassNotFoundException e) {
			throw new assignment4.InvalidCritterException("Invalid Critter class name!");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

		// Move Critter to random position in the world
        added.energy = assignment4.Params.start_energy;
		population.add(added);
		added.y_coord = getRandomInt(Params.world_height);
		added.x_coord = getRandomInt(Params.world_width);
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		String critString = "";
		if (critter_class_name.equals("Craig")) {critString = "C";}
		else if (critter_class_name.equals("Algae")) {critString = "@";}
		else if (critter_class_name.equals("Jamison")) {critString = "J";}
		else if (critter_class_name.equals("Holt")) {critString = "H";}
		for (Critter crit : population) {
			if (crit.toString().equals(critString)) {
				result.add(crit);
			}
		}
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
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
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return (List) new ArrayList<Critter>(population);
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		// Complete this method.
	}

	private static Critter resolve(Critter contender, Critter defender) {
		// TODO: Add resolution logic here
		Critter winner = contender;
		return winner;
	}

	public static void worldTimeStep() {
	    // TODO: Make sure critters don't move twice within same timestep
		// Run a time step for each critter, making sure to say it hasn't moved
		for (Critter crit : Critter.population) {
		    crit.moved = false;
			crit.doTimeStep();
		}

		// Resolve conflicts between critters in same grid positions
		Critter[][] grid = new Critter[Params.world_height][Params.world_width];
		for (Critter crit : population) {
			int col = crit.x_coord;
			int row = crit.y_coord;
			if (grid[row][col] != null) {
				grid[row][col] = resolve(crit, grid[row][col]);
			} else {
				grid[row][col] = crit;
			}
		}

		// TODO: make sure reproduce method is correctly finished
		// Add babies and then remove dead critters
		for (Critter baby : babies) {
			population.add(baby);
		}
		List<Critter> morgue = new ArrayList<Critter>();
		for (Critter crit : population) {
		    crit.energy -= assignment4.Params.rest_energy_cost;
			if (crit.energy <= 0) {
				morgue.add(crit);
			}
		}
		for (Critter dead : morgue) {
			population.remove(dead);
		}
	}
	
	public static void displayWorld() {
		// Create array to store Critters
		Critter[][] grid = new Critter[Params.world_height][Params.world_width];
		for (Critter crit : population) {
			grid[crit.y_coord][crit.x_coord] = crit;
		}
		for (int row=-1; row<=Params.world_height; row++) {
			for (int col=-1; col<=Params.world_width; col++) {
				if ((row==-1 || row==Params.world_height) && (col==-1 || col==Params.world_width)) {
					System.out.print("+");
				} else if (row==-1 || row==Params.world_height) {
					System.out.print("-");
				} else if (col==-1 || col==Params.world_width) {
					System.out.print("|");
				} else if (grid[row][col] != null) {
					System.out.print(grid[row][col].toString());
				} else {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}
}
