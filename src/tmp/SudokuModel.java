import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SudokuModel {
    
    // FIELDS
    public static final int EMPTY = 0;

    private int BOARD_DIM;
    private int box;
    // list of lists of size 2 or 3 keeping track of undo moves 
    // if size 2, it is a delete move with [r,c] coords as the elements
    // if size 3, it is an add move with [r,c,v] as the elements
    private ArrayList<ArrayList<Integer>> undos;
    private int[][] moves;         // list of moves
    private int[][] board;         // 2-d grid
    private int[][] solution;      // 2-d grid 
    private int[][] refboard;      // 2-d grid 
    private int curRow;
    private int curCol;
    private int curMove;
    private int curHint;
    private double solve_time;

    /**
     * Constructor for a SudokuConfig; takes in a Scanner as a parameter
     * @param filename - A String representing the configuration filename
     *                   this will be used to create a Scanner
     */ 
    public SudokuModel(String filename){
        File file;
        Scanner f = null;
        try{
            file = new File(filename);
            f = new Scanner(file);
        } catch(FileNotFoundException e){
            System.out.printf("file: %s was not found\n", filename);
            System.exit(-1);
        }
        String line = f.nextLine();
        undos = new ArrayList<ArrayList<Integer>>();

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
        this.solution = new int[this.BOARD_DIM][this.BOARD_DIM];
        this.refboard = new int[this.BOARD_DIM][this.BOARD_DIM];
        int empties = 0;

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
                int val = Integer.parseInt(token[j], 16);
                board[i][j] = val;
                solution[i][j] = val;
                refboard[i][j] = val;
                empties = (val == EMPTY) ? empties + 1 : empties + 0;
            }
        }
        this.moves = new int[empties][3];
        this.curMove = 0;
        f.close();
        
        int tmpRow = this.curRow;
        int tmpCol = this.curCol;
        long sTime = System.currentTimeMillis();
        boolean solved = solve();
        long eTime = System.currentTimeMillis();
        this.solve_time = (eTime - sTime) / 1000.0;
        if ( !solved ){
            System.out.println("The configuration file created a puzzle that" +
                " cannot be solved");
            System.exit(-1);
        }

        // moves puzzle solution into the solution grid
        for ( int r = 0; r < BOARD_DIM; r++ ){
            for ( int c = 0; c < BOARD_DIM; c++ ) {
                int tmp = board[r][c];
                board[r][c] = solution[r][c];
                solution[r][c] = tmp;
            }
        }

        // populating hints array
        int i = 0;
        while ( i < empties ){
            for ( int r = 0; r < BOARD_DIM; r++ ){
                for ( int c = 0; c < BOARD_DIM; c++ ){
                    if ( refboard[r][c] != solution[r][c] ){
                        moves[i][0] = r;
                        moves[i][1] = c;
                        moves[i][2] = solution[r][c];
                        i++;
                        System.out.printf("%d being added at (%d, %d) at index %d\n", solution[r][c], r, c, i);  
                    }
                }
            }
        }

        this.curHint = 0;

        this.curRow = tmpRow;
        this.curCol = tmpCol;

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
    private void incrementCursor(){
        if ( curCol == BOARD_DIM - 1 ){
            curCol = 0;
            curRow++;
        }

        else{
            curCol++;
        }
    }

    /**
     * method that creates a successor for the sudoku board.
     *
     * makeSuccessor will first increment the cursor until it finds an empty
     * cell. If at any point it reaches the bottom right corner of any sub-box
     * it will need to determine whether that slot is an initial value or
     * a "guessed" element. This step is necessary because if the cursor 
     * reaches the bottom right corner of the box that means the box is filled
     * and therefore the check for valid box needs to be checked.
     *
     * If the slot is an initial value then the 
     * function will return false allowing the backtracker to know not to
     * overwrite that value. If the slot is empty then proceed normally. 
     *
     * Once an empty cell is found, fill in that slot with the "guess" value
     * from paramter guess.
     *
     * @param guess - the guess value that will placed in the next empty cell
     * @return false if the cursor reaches a bottom right corner of a sub-box
     *         AND the cell is populated. true otherwise
     */
    private boolean makeSuccessor(int guess){
        while ( board[curRow][curCol] != EMPTY ){
            incrementCursor();
            if ( isMultiple(curRow+1, box) && isMultiple(curCol+1, box)){
                //System.out.printf("successor with coordinate (%d, %d)\n",
                //    curRow, curCol);  
                if ( board[curRow][curCol] != EMPTY )
                    return false;
            }

            //return true; don't think this is needed
        }
        board[curRow][curCol] = guess;
        moves[curMove][0] = curRow;
        moves[curMove][1] = curCol;
        moves[curMove][2] = guess;
        return true;
        //System.out.printf("board[%d][%d] = %d\n", curRow, curCol, guess);  
    }

    /**
     * solve attempts to find a solution to a sudoku board through a
     * backtracking algorithm.
     *
     * @return true if a solution is found, false otherwise.
     */
    public boolean solve(){
        if ( isGoal() )
            return true;

        int oldR = this.curRow;
        int oldC = this.curCol;
        int oldM = this.curMove;

        for ( int i = 1; i < BOARD_DIM+1; i++){
            // need to handle hints
            boolean boxed = makeSuccessor(i);
            //System.out.println(this); 
            if ( isValid() ){
                if ( solve() )
                    return true;
            }

            if ( !boxed )
                break;
            board[curRow][curCol] = 0;
        }

        curRow = oldR;
        curCol = oldC;
        return false;
    }
    
    /**
     * isValid is a method that gets called every time a successor is made
     * in the solve() method.
     *
     * note that this method does not validate the entire board. It only
     * validates the row, column, and box relative to the current coordinates
     * (curRow, curCol). This design decision was made because the backtracking
     * in solve() executes from left to right starting from the top left-most
     * empty cell. This method of validation is also much less intensive on
     * the number of computation because it doesn't need to validate each
     * row, column, and box (a 3*n^2 operation). 
     * Instead this method is a 3n operation where n is BOARD_DIM
     *
     * due to the nature of this method, a separate validation method
     * must be called in order to iterate through and validate the entire
     * board.
     *
     * @return true if the current configuration is valid, false otherwise
     */
    private boolean isValid(){
        if ( !validate_col(curCol) )
            return false;

        if ( !validate_row(curRow) )
            return false;

        if ( !validate_box() )
            return false;

        return true;
    }

    /**
     * validates a column number denoted by c.
     *
     * the value of c should be within the bounds of [0, BOARD_DIM-1] 
     * (i.e. if the board is 9x9 then the bounds are [0, 8] )
     *
     * numCount will count the occurrences of the numbers in column c
     *   - the index = the number being counted[0, BOARD_DIM-1]
     *   - numCount[index] = the occurrence value of index
     *
     * this method will return true if every element in numCount is NOT greater
     * than 1 
     *
     * @param c - integer denoting the column number to validate
     * @return true if the column conforms to the rules of Sudoku columns
     *
     */
    private boolean validate_col(int c){
        int[] numCount = new int[BOARD_DIM];

        /* Populating occurrences of each number in the column*/
        for ( int r = 0; r < BOARD_DIM; r++ ){
            int cellVal = this.board[r][c];
            if ( cellVal == 0 )
                continue;
            else{
                numCount[cellVal-1]++;
            }

        }

        for ( int i = 0; i < numCount.length; i++ ){
            if ( numCount[i] > 1 )
                return false;
        }
        return true;
    }

    /**
     * validates a row number denoted by r.
     *
     * the value of r should be within the bounds of [0, BOARD_DIM-1] 
     * (i.e. if the board is 9x9 then the bounds are [0, 8] )
     *
     * numCount will count the occurrences of the numbers in row r 
     *   - the index = the number being counted [0, BOARD_DIM-1]
     *   - numCount[index] = the occurrence value of index
     *
     * this method will return true if every element in numCount is NOT greater
     * than 1 
     *
     * @param r - integer denoting the row number to validate
     * @return true if the row conforms to the rules of Sudoku row
     *
     */
    private boolean validate_row(int r){
        /* corresponds to occurrences of a particular numbers */
        /* the index = the number that is having occurrences counted */
        /* the value at the index = the occurrences*/
        int[] numCount = new int[BOARD_DIM];

        /* Populating occurrences of each number in the row*/
        for ( int c = 0; c < BOARD_DIM; c++ ){
            int cellVal = this.board[r][c];
            if ( cellVal == 0 )
                continue;
            else{
                numCount[cellVal-1]++;
            }

        }

        for ( int i = 0; i < numCount.length; i++ ){
            if ( numCount[i] > 1 )
                return false;
        }
        return true;
    }

    /**
     * validates a box number relative to the board's current coordinates.
     *
     * numCount will count the occurrences of the numbers in a particular box 
     *   - the index = the number being counted [0, BOARD_DIM-1]
     *   - numCount[index] = the occurrence value of index
     *
     * this method will return true if every element in numCount is NOT greater
     * than 1 
     *
     * @param r - integer denoting the row number to validate
     * @return true if the row conforms to the rules of Sudoku row
     *
     */
    private boolean validate_box(){
        int[] numCount = new int[BOARD_DIM];
        // TODO: Figure out why i put this here
        if ( curRow == 0 || curCol == 0 )
            return true;

        else if ( isMultiple(curRow+1, box) && isMultiple(curCol+1, box)){
            //System.out.printf("Looking at a box at (%d, %d)\n", curRow, curCol);
            for ( int r = curRow - (box-1); r <= curRow; r++ ){
                for ( int c = curCol - (box-1); c <= curCol; c++ ){
                    int cellVal = this.board[r][c];
                    //System.out.printf(" %d", cellVal); 
                    if ( cellVal == 0 )
                        continue;
                    else{
                        numCount[cellVal-1]++;
                    }
                }
            }
            //System.out.println(""); 

            //System.out.println( Arrays.toString(numCount) ); 

            for ( int i = 0; i < numCount.length; i++ ) {
                if ( numCount[i] > 1 )
                    return false;
            }
        }
        return true;
    }


    /**
     * determines if the sudoku board is completely filled.
     *
     * Due to the nature of the backtracking algorithm, if isGoal is true,
     * then it must mean that the board is complete and a solution exists
     *
     * @return true if the board is filled, false otherwise 
     */
    private boolean isGoal(){
        for (int i = 0; i < this.BOARD_DIM; i++) {
            for (int j = 0; j < this.BOARD_DIM; j++) {
                if ( this.board[i][j] == 0)
                    return false;
            }
        }

        return true;
    }

    //
    // methods for interactive mode
    //
    
    /**
     * determines whether a string is an integer or not;
     * this method was taken from rosettacode.org
     *
     * TODO this needs to be modified to check if hex values are numbers
     *
     * @param s - a string that may or may not be an integer
     * @return true if s is an integer, false otherwise
     *
     */
    public static boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) return false;
        for (int x = 0; x < s.length(); x++) {
            final char c = s.charAt(x);

            if (x == 0 && (c == '-')) 
                continue;  // negative

            if ((c >= '0') && (c <= '9')) 
                continue;  // 0 - 9
            return false; // invalid
        }
        return true; // valid
    }
    
    /**
     * Method that validates a coordinate pair (r, c).
     *
     * @param r - an integer representing the row
     * @param c - an integer representing the column
     * 
     * @return true if (r, c) is a valid coordinate pair, false otherwise
     */
    private boolean validateCoords(int r, int c){
        return r >= 0 && r <= (BOARD_DIM - 1) &&
            c >= 0 && c <= (BOARD_DIM - 1);
    }

    /**
     * Given a coordinate pair (r, c) this method attempts to add
     * v in the sudoku board at the coordinate pair's location.
     *
     * This method prevents from overwriting cells defined by the initial 
     * configuration file. It also ensures valid coordinate pairs and a valid
     * value to be placed at (r,c) in the board.
     *
     * @param r - an integer representing the row
     * @param c - an integer representing the column
     * @param v - an integer representing the value to be placed in (r, c)
     *
     * @return true if v was placed at (r,c) in the board, false otherwise 
     */
    public boolean addToBoard(int r, int c, int v){
        // invalid coorindates
        if ( !validateCoords(r, c) ){
            System.out.printf("Invalid coordinates (%d, %d)\n", r, c);
            return false;
        }

        // value is out of bounds
        if ( v < 0 || v > BOARD_DIM ){
            System.out.printf("Invalid value:  to place at (%d, %d)\n",r,c,v);  
            return false;
        }

        if (this.refboard[r][c] != EMPTY){
            System.out.println("Cannot change an initial cell"); 
            return false;
        }

        this.board[r][c] = v;
        System.out.printf("%d was added to (%d, %d)\n", v, r, c);  
        // adds the corresponding delete move
        ArrayList<Integer> mv = new ArrayList<Integer>();
        mv.add(r);
        mv.add(c);
        this.undos.add(mv);
        return true;
    }

    /**
     * Given a coordinate pair (r, c) this method attempts to delete the 
     * element located at that coordinate.
     *
     * This method prevents from deleting cells defined by the initial 
     * configuration file. It also ensures valid coordinate pairs
     *
     * @param r - an integer representing the row
     * @param c - an integer representing the column
     *
     * @return true if element was deleted at (r,c) in the board,
     *         false otherwise 
     */
    public boolean deleteElement(int r, int c){
        // invalid coorindates
        if ( !validateCoords(r, c) ){
            System.out.printf("Invalid coordinates (%d, %d)\n", r, c);
            return false;
        }

        // can't delete empty cell
        if (this.refboard[r][c] != EMPTY){
            System.out.println("Cannot delete an initial element"); 
            return false;
        }

        if (this.board[r][c] == EMPTY){
            System.out.println("Cannot delete if the cell is empty");
            return false;
        }

        // adds the corresponding add move
        ArrayList<Integer> mv = new ArrayList<Integer>();
        mv.add(r);
        mv.add(c);
        mv.add(this.board[r][c]);
        this.undos.add(mv);
        System.out.printf("%d was removed from (%d, %d)\n",
            this.board[r][c], r, c);  
        this.board[r][c] = EMPTY;
        return true;
    }

    public void undo(){
        if ( this.undos.size() == 0 )
            System.out.println("Nothing to undo!");

        else{
            System.out.print("Undo-ing last move... "); 
            int idx = this.undos.size() - 1; // index of most recent move
            ArrayList<Integer> m = this.undos.remove(idx);
            // undo-ing an add move -> delete that value
            if ( m.size() == 2 )
                deleteElement(m.get(0), m.get(1) );

            // undo-ing a delete move -> add the value back in
            else if ( m.size() == 3 )
                addToBoard(m.get(0), m.get(1), m.get(2) );

            else{
                System.out.println("Something wrong happened in undo()");
                System.exit(-1);
            }

            // calling either addToBoard or deleteElement will cause a new
            // move to be added to undos; most recent move needs to be 
            // removed to prevent circular undos
            // this hacky solution could be remedied by not calling those
            // methods, assumining the coordinates are valid, and
            // assigning directly to the board but seems less safe
            this.undos.remove(idx);

        }
    }

    public void hint(){
        if ( curHint >= moves.length ){
            return;
        }
        addToBoard(moves[curHint][0], moves[curHint][1], moves[curHint][2]);
        curHint++;
    }

    public boolean validate_one_box(int row, int col){
        //System.out.printf("Looking at a box at (%d, %d)\n", row, col);
        int[] numCount = new int[BOARD_DIM];
        for ( int r = row; r < row+box; r++ ){
            for ( int c = col; c < col+box; c++ ){
                int cellVal = this.board[r][c];
                //System.out.printf("(%d, %d): %d\n", r, c, cellVal); 
                if ( cellVal == 0 )
                    continue;
                else
                    numCount[cellVal-1]++;
            }
        }

        for ( int i = 0; i < numCount.length; i++ ) {
            if ( numCount[i] > 1 )
                return false;
        }
        return true;

    }

    public boolean validate_board(){
        for ( int r = 0; r < BOARD_DIM; r++ ){
            if ( !validate_row(r) )
                return false;
        }

        for ( int c = 0; c < BOARD_DIM; c++ ){
            if ( !validate_col(c) )
                return false;
        }

        for ( int r = 0; r < BOARD_DIM; r += box ){
            for ( int c = 0; c < BOARD_DIM; c += box ){
                if ( !validate_one_box(r, c) )
                    return false; 
            }
        }
        
        return true;
    }

    public void show_solution(){
        for ( int r = 0; r < BOARD_DIM; r++ ){
            for ( int c = 0; c < BOARD_DIM; c++ )
                board[r][c] = solution[r][c];
        }
    }

    public double get_solve_time(){ return this.solve_time; }

    /**
     * creates a formatted sudoku board
     */
    public String toString() {
        //                            3
        int linelen = 2 * BOARD_DIM + 4 + 2 * ((BOARD_DIM/this.box)-1);
        String board = "\n";
        //
        board += " | ";
        for (int j = 0; j < BOARD_DIM; j++) {
            if ( isMultiple(j, this.box) )
                board += "| ";

            board += j + " ";
        }
        board += "|\n";

        //
        for (int i = 0; i < linelen; i++)
            board += "-";

        board += "\n";

        for (int i = 0; i < BOARD_DIM; i++) {
            if ( isMultiple(i, this.box) ) {
                for (int j = 0; j < linelen; j++)
                    board += "-";
                board += "\n";
            }
            board += i + "| ";
            for (int j = 0; j < BOARD_DIM; j++) {
                if ( isMultiple(j, this.box) )
                    board += "| ";

                // populates the board
                if ( this.board[i][j] == 0)
                    board += ". ";
                else
                    board += Integer.toHexString(this.board[i][j]).toUpperCase()
                        + " ";
            }
            board += "|\n";
        }

        for (int i = 0; i < linelen; i++)
            board += "-";

        board += "\n";

        //board += "Current coord is (" + curRow + ", " + curCol + ")\n";


        return board;
    }

    public static void main(String[] args){
        System.out.println("fuck"); 
    }

}
