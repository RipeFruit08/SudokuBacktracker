import java.util.Scanner;

public class SudokuPTUI{

    private Scanner inputParser;
    private String usrcmd;

    public SudokuPTUI(String filename){
        inputParser = new Scanner(System.in);
        usrcmd = "";
    }

    public void run(){
        while(!usrcmd.equals("q") && !usrcmd.equals("quit") ){
            System.out.print("> "); 
            usrcmd = inputParser.nextLine();
        }
    }

}
