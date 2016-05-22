import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.HashMap;

public class SudokuModel {
    
    // FIELDS
    public static final int EMPTY = 0;

    private int BOARD_DIM;
    private int box;
    private int[][] board;
    private int curRow;
    private int curCol;

    /**
     * Constructor for a SudokuConfig; takes in a Scanner as a parameter
     * @param f - A Scanner object that is made from a File object
     */ 
    public SudokuModel(Scanner f){
        String line = f.nextLine();

        while ( true ){
            if ( line.isEmpty()) {
                System.out.println();
                line = f.nextLine();
            }
            else{
                if ( Character.isDigit( line.charAt(0) ) )
                    break;
                else{
                    System.out.println(line);
                    line = f.nextLine();
                }
            }
        }

        this.BOARD_DIM = Integer.parseInt(line);
        box = (int)Math.sqrt(this.BOARD_DIM);
        this.board = new int[this.BOARD_DIM][this.BOARD_DIM];

        /* determining whether or not the coordinates has been initialized */
        boolean initCoord = false;
        for (int i = 0; i < BOARD_DIM; i++) {
            String[] token = f.nextLine().split(" ");
            for (int j = 0; j < BOARD_DIM; j++){
                if ( !initCoord && token[j].equals("0") ){
                    this.curRow = i;
                    this.curCol = j;
                    initCoord = true;
                }
                board[i][j] = Integer.parseInt(token[j]);
            }
        }
    }

    /**
     * isMultiple determines if an int, num, is a multiple of another int, multiple
     * @param num - the number in question
     * @param multiple - the number that is the multiple
     * @return
     */
    private boolean isMultiple(int num, int multiple){
        if ( ( num == 0 ) || ( num == 1 ) )
            return false;
        else
            return ((num % multiple) == 0);
    }

    /**
     * incrementCursor moves the cursor forward.
     *
     * this method does not handle cases when the cursor is at
     * the very bottom of the board because incrementCursor 
     * should be not called in that particular situation
     */
    public void incrementCursor(){
        if ( curCol == BOARD_DIM - 1 ){
            curCol = 0;
            curRow++;
        }

        else{
            curCol++;
        }
    }

    public void makeSuccessor(int guess){
        while ( board[curRow][curCol] != EMPTY )
            incrementCursor();
        board[curRow][curCol] = guess;
        System.out.printf("board[%d][%d] = %d\n", curRow, curCol, guess);  
    }


    public boolean solve(){
        if ( isGoal() )
            return true;
        for ( int i = 1; i < 10; i++){
            makeSuccessor(i);
            System.out.println(this); 
            if ( isValid() ){
                if ( solve() )
                    return true;
            }
            board[curRow][curCol] = 0;
        }

        return false;
    }
    
    public boolean isValid(){
        return true;
    }

    public boolean isGoal(){
        for (int i = 0; i < this.BOARD_DIM; i++) {
            for (int j = 0; j < this.BOARD_DIM; j++) {
                if ( this.board[i][j] == 0)
                    return false;
            }
        }

        return true;
    }

    public String toString() {
        int linelen = 2 * BOARD_DIM + 3 + 2 * ((BOARD_DIM/this.box)-1);
        String board = "\n";
        for (int i = 0; i < linelen; i++)
            board += "-";

        board += "\n";

        for (int i = 0; i < BOARD_DIM; i++) {
            if ( isMultiple(i, this.box) ) {
                for (int j = 0; j < linelen; j++)
                    board += "-";
                board += "\n";
            }
            board += "| ";
            for (int j = 0; j < BOARD_DIM; j++) {
                if ( isMultiple(j, this.box) )
                    board += "| ";

                // populates the board
                if ( this.board[i][j] == 0)
                    board += ". ";
                else
                    board += this.board[i][j] + " ";
            }
            board += "|\n";
        }

        for (int i = 0; i < linelen; i++)
            board += "-";

        board += "\n";

        board += "Current coord is (" + curRow + ", " + curCol + ")\n";


        return board;
    }

    public static void main(String[] args){
        System.out.println("fuck"); 
    }

}
