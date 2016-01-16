/**
 * Created by Stephen on 1/11/2016.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class SudokuConfig {

    // FIELDS
    public static final int EMPTY = 0;

    private int BOARD_DIM;
    private int box;
    private int[][] board;

    public SudokuConfig(Scanner f){
        this.BOARD_DIM = Integer.parseInt(f.nextLine());
        box = (int)Math.sqrt(this.BOARD_DIM);
        this.board = new int[this.BOARD_DIM][this.BOARD_DIM];


        for (int i = 0; i < BOARD_DIM; i++) {
            String[] token = f.nextLine().split(" ");
            for (int j = 0; j < BOARD_DIM; j++)
                board[i][j] = Integer.parseInt(token[j]);
        }
    }

    public SudokuConfig(SudokuConfig copy){
        this.BOARD_DIM = copy.BOARD_DIM;
        this.box = copy.box;
        this.board = new int[this.BOARD_DIM][this.BOARD_DIM];

        for (int i = 0; i < this.BOARD_DIM; i++) {
            for (int j = 0; j < this.BOARD_DIM; j++)
                this.board[i][j] = copy.board[i][j];
        }
    }

    public boolean isValid(){
        for (int i = 0; i < this.BOARD_DIM; i++) {
            for (int j = 0; j < this.BOARD_DIM; j++) {
                if ( j == 0){
                    System.out.printf("Looking at row %d from coordinate (%d, %d)\n", i, i, j);
                }

                if ( i == 0) {
                    System.out.printf("Looking at column %d from coordinate (%d, %d)\n", j, i, j);
                }

                if ( (isMultiple(i, this.box) || i == 0) &&
                        (isMultiple(j, this.box) || j == 0) ){
                    System.out.printf("Looking at a box with coordinates (%d, %d)\n", i, j);
                }


            }


        }
        System.out.println();
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

    public Collection<SudokuConfig> getSuccessors(){
        ArrayList<SudokuConfig> successors = new ArrayList<SudokuConfig>();
        for (int i = 0; i < this.BOARD_DIM; i++) {
            for (int j = 0; j < this.BOARD_DIM; j++) {
                if ( this.board[i][j] == 0 ){
                    for (int k = 1; k < this.BOARD_DIM+1; k++) {
                        SudokuConfig tmp = new SudokuConfig(this);
                        tmp.board[i][j] = k;
                        successors.add(tmp);
                    }
                    return successors;
                }

            }
        }
        assert(successors != null);
        return successors;
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


        return board;
    }
}
