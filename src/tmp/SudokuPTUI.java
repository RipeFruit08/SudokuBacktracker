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

        // solve
        else if (cmdlst[0].equals("solve") ){
            System.out.println("The puzzle will be solved");
            if( model.solve() )
                System.out.println("Solution:"); 

            else
                System.out.println("No solution"); 

            System.out.println(model);
            return true;
        }

        // validate
        else if (cmdlst[0].equals("validate") ){
            System.out.println("The puzzle will be validated");
            if ( !model.validate_board() )
                System.out.println("The current board is not valid"); 
        
            else
                System.out.println("The current board is valid"); 
        }

        // add row col value
        else if (cmdlst[0].equals("add") ){
            System.out.println("adding an element");
            if ( cmdlst.length == 4 && validateArgs(cmdlst[1], cmdlst[2],
                cmdlst[3] ) ){
                
                int row = Integer.parseInt(cmdlst[1]);
                int col = Integer.parseInt(cmdlst[2]);
                int val = Integer.parseInt(cmdlst[3]);
                System.out.printf("Placing %s at (%s, %s)\n", 
                    cmdlst[3], cmdlst[1], cmdlst[2]);  
                model.addToBoard(row, col, val);
            }
        }

        // del row col
        else if (cmdlst[0].equals("del") ){
            System.out.println("deleting an element");
            if ( cmdlst.length == 3 && SudokuModel.isNumeric(cmdlst[1]) &&
                SudokuModel.isNumeric(cmdlst[2])){
                
                int row = Integer.parseInt(cmdlst[1]);
                int col = Integer.parseInt(cmdlst[2]);
                System.out.printf("Removing element from (%s, %s)\n", 
                    cmdlst[1], cmdlst[2]);  
                model.deleteElement(row, col);
            }
        }

        else
            System.out.println("not a valid cmd"); 

        return false;
    }

    public void run(){
        boolean solved = false;
        while( !usrcmd.equals("q") && !usrcmd.equals("quit") ){
            System.out.println(model); 
            System.out.print("> "); 
            usrcmd = inputParser.nextLine();
            solved = processCmds(usrcmd);
            if (solved)
                break;
        }
    }

}
