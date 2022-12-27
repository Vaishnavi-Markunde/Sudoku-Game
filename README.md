# Sudoku Game
This is a simple Sudoku game implemented in Java. The game allows the user to play a Sudoku puzzle and provides options to update, undo, and quit the game.

## Getting Started
To run the game, simply compile and run the SudokuGame.java file. The game will prompt the user to select a difficulty level (Easy, Medium, or Hard) and will generate a new puzzle based on the selected difficulty. The user can then enter commands to update the puzzle, undo their previous move, or quit the game.

## Gameplay
To make a move, the user must enter the command 'a', followed by the row number, column number, and value to be placed in that cell. The game will update the puzzle and print the updated puzzle to the console.

The user can also use the 'u' command to undo their previous move, or the 'q' command to quit the game or the 'r' command to replay moves. If the user quits the game before solving the puzzle, the game will print the solution to the puzzle.

## Winning
The game is won when the puzzle is solved correctly, with each row, column, and 3x3 region containing unique values from 1 to 9. If the puzzle is solved correctly, the game will print a congratulations message and end.
