/**
 * @author Stephen Kim
 * Created: January 12th, 2016
 *
 * SudokuSolver.java is the main program that solves a sudoku puzzle
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SudokuSolver {

    /**
     * solve is the backtracking algorithm for a SudokuConfig
     * @param config - A SudokuConfig object
     * @param debug - A boolean flag for debugging
     * @return solution, a SudokuConfig that is completed and correct
     */
    public static SudokuConfig solve(SudokuConfig config, boolean debug){

        SudokuConfig solution = null;

        // probably need to get rid of this block
        if ( debug ) {
            System.out.println("Solve was called");

            System.out.println("--------------------------------");
            System.out.println("Printing successors");

            for (SudokuConfig successor : config.getSuccessors()) {
                System.out.println(successor);
            }

            System.out.println("--------------------------------");
        }

        if ( config.isGoal() ){
            return config;
        }

        else{
            if ( debug )
                System.out.println("Current Board:\n" + config);

            for ( SudokuConfig succesor: config.getSuccessors()){

                if ( succesor.isValid()) {
                    //System.out.println("Valid successor" + succesor);
                    solution = solve(succesor, debug);

                    if (solution != null)
                        return solution;
                }
            }
        }

        return solution;
    }

    /**
     * Note: Main program MUST be run in one of two configurations
     *     1. Program run with 0 arguments
     *     2. Prgroam run with 2 arguments.
     *         -Argument 1 is the filename
     *         -Argument 2 is true/false (debug flag)
     * @param args - A string array containing arguments
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException{
        String filename;
        boolean debug = false;

        // get filename from user input if 0 arguments are passed in
        if (args.length == 0){
            Scanner input = new Scanner(System.in);
            System.out.print("Enter filename of the board: ");
            filename = input.next();
        }

        // grabs the filename and debug from args
        else if ( args.length == 2){
            filename = args[0];
            debug = Boolean.valueOf(args[1]);
        }

        else{
            System.out.println("Usage: [boardconfig debugmode]");
            return;
        }

        System.out.println("Filename: " + filename);
        File file = new File(filename);
        Scanner f = new Scanner(file);

        SudokuConfig initBoard = new SudokuConfig(f);

        System.out.println("Initial Configuration:");
        System.out.println(initBoard);

        SudokuConfig solution = solve(initBoard, debug);

        if(solution != null){
            System.out.println("Solution:");
            System.out.println(solution);
        }

    }
}
