package main;
// TODO GUI portion of this
//import javafx.application.Application;


import main.java.ptui.SudokuPTUI;

/**
 * Main program for this project
 *
 * @author Stephen Kim
 * July 4th, 2016
 */
public class Sudoku{

    public static void usageErr(){
        System.out.println("Usage: java main.Sudoku (gui | ptui) board-file");
        System.exit(1);
    }

    public static void main(String[] args){
        String current = System.getProperty("user.dir");
        System.out.println("Current dir: " + current);
        if ( args.length != 2)
            usageErr();

        else{
            String filename = args[1];
            System.out.println("gets here");
            
            // gui
            if ( args[0].equals("gui") ){
                System.out.println("will run the gui...");
                //Application.launch(SudokuGUI.class, filename);
            }
                
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
