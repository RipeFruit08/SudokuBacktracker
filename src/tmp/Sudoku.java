/**
 * Main program for this project
 *
 * @author Stephen Kim
 * July 4th, 2016
 */
public class Sudoku{

    public static void usageErr(){
        System.out.println("Usage: java Sudoku (gui | ptui) board-file");
        System.exit(-1);
    }

    public static void main(String[] args){
        if ( args.length != 2)
            usageErr();

        else{
            String filename = args[1];
            System.out.println("gets here");
            
            // gui
            if ( args[0].equals("gui") )
                System.out.println("will run the gui...");
                
            // ptui
            else if ( args[0].equals("ptui") ){
                System.out.println("will run the ptui");
                SudokuPTUI ptui = new SudokuPTUI(filename);
                ptui.run();
            }

            else
                usageErr();
        }
    }
}
