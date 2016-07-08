import java.util.Scanner;

public class SudokuPTUI{
    
    private SudokuModel model;
    private Scanner inputParser;
    private String usrcmd;

    /**
     * Constructor for the Plain Text User Interface.
     *
     * @param filename - string representing the configuration file. This is
     *                   used to generate the initial sudoku board.
     */
    public SudokuPTUI(String filename){
        model = new SudokuModel(filename);
        inputParser = new Scanner(System.in);
        usrcmd = "";
    }

    /**
     * determines if the three parameters are integers.
     *
     * @param row - a string that may be an integer
     * @param col - a string that may be an integer
     * @param val - a string that may be an integer
     *
     * @return true if row AND col AND val are all integers, false otherwise
     */
    private boolean validateArgs(String row, String col, String val){
        return SudokuModel.isNumeric(row) && SudokuModel.isNumeric(col)
            && SudokuModel.isNumeric(val);
    }

    private void displayHelp(){
        System.out.println("Enter one of the following commands:");
        System.out.println("    (q)uit to quit sudoku");
        System.out.println("    (s)olve to solve the puzzle");
        System.out.println("    (v)alidate to validate the current board");
        System.out.println("    (a)dd val row col to add val to the board at");
        System.out.println("\tcoordinates (row, col)");
        System.out.println("    (d)elete row col to delete the value from the");
        System.out.println("\tboard at coordinate (row, col)"); 
        System.out.println("    (h)elp to display this message again"); 
    }

    /** 
     * processes the commands fed in by the user.
     *
     * valid commands include quitting, adding to board, deleting from board,
     * solving board, and validating the board
     */
    private boolean processCmds(String cmd){
        // splits by whitespace
        String[] cmdlst = cmd.split("\\s");
        if ( cmdlst.length == 0 )
            System.out.println("please enter a command");

        // quit | q
        else if ( cmdlst[0].equals("quit") || cmdlst[0].equals("q") )
            System.out.println("Quitting the puzzle"); 

        // help | q
        else if ( cmdlst[0].equals("help") || cmdlst[0].equals("h") )
            displayHelp();

        // solve | s
        else if (cmdlst[0].equals("solve") || cmdlst[0].equals("s") ){
            /*
            long sTime = System.currentTimeMillis();
            System.out.println("The puzzle will be solved");
            boolean solved = model.solve();
            long eTime = System.currentTimeMillis();
            if( solved )
                System.out.println("Solution:"); 

            else
                System.out.println("No solution"); 

            System.out.println(model);
            double time_exec = (eTime - sTime) / 1000.0;
            System.out.printf("Puzzle was solved in %f seconds\n", time_exec);
            */
            model.show_solution();
            System.out.println("Solution: ");
            System.out.println(model);
            System.out.printf("The puzzle was solved in %f seconds\n",
                model.get_solve_time() );  
            return true;
        }

        // validate | v
        else if (cmdlst[0].equals("validate") || cmdlst[0].equals("v") ){
            System.out.print("Validating board... ");
            if ( !model.validate_board() )
                System.out.println("The current board is not valid"); 
        
            else
                System.out.println("The current board is valid"); 
        }

        // add value row col | a value row col 
        else if (cmdlst[0].equals("add") || cmdlst[0].equals("a") ){
            //System.out.println("adding an element");
            if ( cmdlst.length == 4 && validateArgs(cmdlst[1], cmdlst[2],
                cmdlst[3] ) ){
                
                int val = Integer.parseInt(cmdlst[1]);
                int row = Integer.parseInt(cmdlst[2]);
                int col = Integer.parseInt(cmdlst[3]);
                //System.out.printf("Placing %s at (%s, %s)\n", 
                //    cmdlst[3], cmdlst[1], cmdlst[2]);  
                model.addToBoard(row, col, val);
            }

            else
                System.out.println("Usage: (a)dd val row col"); 
        }

        // del row col | d row col 
        else if (cmdlst[0].equals("del") || cmdlst[0].equals("d") ){
            //System.out.println("deleting an element");
            if ( cmdlst.length == 3 && SudokuModel.isNumeric(cmdlst[1]) &&
                SudokuModel.isNumeric(cmdlst[2])){
                
                int row = Integer.parseInt(cmdlst[1]);
                int col = Integer.parseInt(cmdlst[2]);
                //System.out.printf("Removing element from (%s, %s)\n", 
                //    cmdlst[1], cmdlst[2]);  
                model.deleteElement(row, col);
            }

            else
                System.out.println("Usage: (d)el row col"); 
        }

        // undo | u
        else if (cmdlst[0].equals("undo") || cmdlst[0].equals("u") ){
            model.undo();
        }

        // invalid comments 
        else{
            System.out.print("Invalid Command; "); 
            displayHelp();
        }
        return false;
    }

    public void run(){
        boolean solved = false;
        displayHelp();
        long sTime = System.currentTimeMillis();
        while( !usrcmd.equals("q") && !usrcmd.equals("quit") ){
            System.out.println(model); 
            System.out.print("> "); 
            usrcmd = inputParser.nextLine();
            solved = processCmds(usrcmd);
            if (solved)
                break;
        }
        long eTime = System.currentTimeMillis();
        double exec_time = (eTime - sTime) / 1000.0;
        if (!solved)
            System.out.println("Program ran for " + exec_time + " seconds"); 
    }

}
