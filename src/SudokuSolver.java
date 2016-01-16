/**
 * Created by Stephen on 1/12/2016.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SudokuSolver {

    public static SudokuConfig solve(SudokuConfig config){

        SudokuConfig solution = null;
        boolean debug = false;

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
                    System.out.println("Valid successor" + succesor);
                    solution = solve(succesor);

                    if (solution != null)
                        return solution;
                }
            }
        }

        return solution;
    }

    public static void main(String[] args) throws FileNotFoundException{
        String filename;

        if (args.length == 0){
            Scanner input = new Scanner(System.in);
            System.out.print("Enter filename of the board: ");
            filename = input.next();
        }

        else
            filename = args[0];

        System.out.println("Filename: " + filename);
        File file = new File(filename);
        Scanner f = new Scanner(file);

        SudokuConfig initBoard = new SudokuConfig(f);

        System.out.println("Initial Configuration:");
        System.out.println(initBoard);

        SudokuConfig solution = solve(initBoard);

        if(solution != null){
            System.out.println("Solution:");
            System.out.println(solution);
        }

    }
}
