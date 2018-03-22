package assignment4;
/* CRITTERS Main.java
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

import java.util.Scanner;
import java.util.List;
import java.io.*;


/*
* Usage: java <pkgname>.Main <input file> test
* input file is optional.  If input file is specified, the word 'test' is optional.
* May not use 'test' argument without specifying input file.
*/
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
    * Main method.
    * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name,
    * and the second is test (for test output, where all output to be directed to a String), or nothing.
    */
    public static void main(String[] args) throws Exception {
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                // Create a stream to hold the output
                testOutputString = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(testOutputString);
                // Save the old System.out.
                old = System.out;
                // Tell Java to use the special stream; all console output will be redirected here from now
                System.setOut(ps);
            }
        }
    } else { // if no arguments to main
        kb = new Scanner(System.in); // use keyboard and console
    }

    /* Write your code below. */
    boolean exit = false;
    while (!exit) {
        // Get and clean next user commmand
        System.out.print("critters> ");
        String cmd = kb.nextLine().trim();
        cmd = cmd.replaceAll("\\s{2,}", " ");

        // Handle Exceptions/Errors
        try {

            // Check all commands if single word or multi-word
            if (cmd.equals("quit")) {
                exit = true;
            } else if (cmd.equals("show")) {
                Critter.displayWorld();
            } else {
                // Make array of words in command and number of words
                String[] cmdWords = cmd.split("\\s");
                int numWords = cmdWords.length;

                // Manage multi-word commands
                if (cmdWords[0].equals("step") && numWords <= 2) {
                    int numSteps = numWords == 1 ? 1 : Integer.parseInt(cmdWords[1]);
                    for (int i=0; i<numSteps; i++) {
                        Critter.worldTimeStep();
                    }
                } else if (cmdWords[0].equals("seed") && numWords == 2) {
                    long seedNum = (Long.parseLong(cmdWords[1]));
                    System.out.println(seedNum);
                    Critter.setSeed(seedNum);
                } else if (cmdWords[0].equals("make") && (numWords == 2 || numWords == 3)) {
                    int toMake = numWords == 2 ? 1 : Integer.parseInt(cmdWords[2]);
                    for (int i=0; i<toMake; i++) {
                        Critter.makeCritter(cmdWords[1]);
                    }
                } else if (cmdWords[0].equals("stats") && numWords == 2) {
                    List<Critter> instances = Critter.getInstances(cmdWords[1]);
                    Critter.runStats(instances);
                } else {
                    // Command not found - print as such
                    System.out.println("invalid command: " + cmd);
                }
            }
        } catch (Exception e) {
            System.out.println("error processing: " + cmd);
        }
    }
    /* Write your code above */
    System.out.flush();

}
}
