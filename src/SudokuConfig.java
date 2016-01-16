/**
 * Created by Stephen on 1/11/2016.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.HashMap;

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
        int validations = 0;
        for (int i = 0; i < this.BOARD_DIM; i++) {
            for (int j = 0; j < this.BOARD_DIM; j++) {

                if ( j == 0){
                    validations += validateRow(i, j) == true ? 1 : 0;
                }

                if ( i == 0) {
                    validations += validateCol(i, j) == true ? 1 : 0;
                }

                if ( (isMultiple(i, this.box) || i == 0) &&
                        (isMultiple(j, this.box) || j == 0) ){
                    //System.out.printf("Looking at a box with coordinates (%d, %d)\n", i, j);
                    validations += validateBox(i, j) == true ? 1 : 0;
                }


            }


        }
        if ( validations != 3* this.BOARD_DIM)
            return false;
        System.out.println();
        return true;
    }

    public boolean validateRow(int row, int col){
        // maps a number to how many times it has occurred
        HashMap<Integer, Integer> numCount = new HashMap<Integer, Integer>();
        for (int i = col; i < this.BOARD_DIM; i++) {
            if ( this.board[row][i] == 0)
                continue;
            else if ( ! numCount.containsKey(this.board[row][i]))
                numCount.put(this.board[row][i], 1);

            else{
                int oldval = numCount.get(this.board[row][i]);
                numCount.put(this.board[row][i], oldval+1);
            }
        }

        for( Integer count: numCount.values()){
            if ( count > 1) {
                return false;
            }
        }
        return true;
    }

    public boolean validateCol(int row, int col){
        // maps a number to how many times it has occurred
        HashMap<Integer, Integer> numCount = new HashMap<Integer, Integer>();
        for (int i = row; i < this.BOARD_DIM; i++) {
            if ( this.board[i][col] == 0)
                continue;
            else if ( ! numCount.containsKey(this.board[i][col]))
                numCount.put(this.board[i][col], 1);

            else{
                int oldval = numCount.get(this.board[i][col]);
                numCount.put(this.board[i][col], oldval+1);
            }
        }

        for( Integer count: numCount.values()){
            if ( count > 1)
                return false;
        }
        return true;
    }

    public boolean validateBox(int row, int col){
        HashMap<Integer, Integer> numCount = new HashMap<Integer, Integer>();
        for (int i = row; i < row+this.box; i++) {
            for (int j = col; j < col + this.box; j++) {
                if ( this.board[i][j] == 0)
                    continue;
                else if ( ! numCount.containsKey(this.board[i][j]))
                    numCount.put(this.board[i][j], 1);

                else{
                    int oldval = numCount.get(this.board[i][j]);
                    numCount.put(this.board[i][j], oldval+1);
                }
            }
        }

        for( Integer count: numCount.values()){
            if ( count > 1)
                return false;
        }
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
