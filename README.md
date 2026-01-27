# SudokuBacktracker

A sudoku puzzle solver using a backtracking algorithm

## How to run

Navigate to the `src` directory, then run the following commands

```bash
# compile code
javac main/Sudoku.java

# run java executable - arg0 needs to be either gui or ptui;
# arg1 is a txt file representing a sudoku board
javac main.Sudoku ptui ../easy1
```

## Board configuration files

A board configuration file represents a Sudoku puzzle. It must adhere to the
following format:

- The first non empty line that starts with a number determines the board's
  dimensions (e.g., a value of 9 indicates a standard Sudoku puzzle)
- The lines immediately following the board's dimensions must include the
  contents of the board row by row starting from top to bottom.
- Values of -1 indicate an empty cell

Note: Hexadoku is currently not supported
