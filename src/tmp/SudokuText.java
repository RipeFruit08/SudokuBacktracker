import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SudokuText{

    public static void main(String[] args) throws FileNotFoundException{
        if ( args.length != 1 )
            System.out.println("Usage: java SudokuText filename"); 
        
        else{
            String filename = args[0];
            //File file = new File(filename);
            //Scanner f = new Scanner(file);

            SudokuModel m = new SudokuModel(filename);
            System.out.println("initial board");
            System.out.println(m); 
            if( m.solve() )
                System.out.println("Solution:"); 

            else
                System.out.println("No solution"); 

            System.out.println(m); 
        }

    }

}
