package gs;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class SudokuGame{

	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);

		
		// prompt the user to select the difficulty level
		System.out.println("Select diffulty level 1.Easy 2.Medium 3.Hard");
		int d = sc.nextInt();
		Sudoku sudoku = new Sudoku(d);

		// generate a new puzzle with the selected difficulty level
		int puzzle[][] = sudoku.generatePuzzle();
		sudoku.printSudoku();

		// keep prompting the user for updates until the puzzle is solved
		while (!sudoku.isSolved()) {
			// prompt the user for a command
			System.out.println("\nEnter command (u to undo, q to quit, r to replay, or a to update):");
			String command = sc.next();

			if (command.equals("u")) {
				// undo the last move made by the user
				sudoku.undo();
				sudoku.printSudoku();
			} 
			else if (command.equals("q")) {
				break;
			} 
			else if (command.equals("r")) {
				sudoku.replay();
			}
			else {
				// prompt the user for the row, column, and value to update in the puzzle
				System.out.println("enter row");
				int row = sc.nextInt()-1;
				System.out.println("enter col");
				int col = sc.nextInt()-1;
				System.out.println("enter value");
				int value = sc.nextInt();

				// update the puzzle with the new value
				if (sudoku.update(row, col, value)) {
					sudoku.printSudoku();
				}
			}
		}

		if(sudoku.isSolved()) {
			System.out.println("Congratulations! You solved the puzzle!");
		}
		else {
			System.out.println("Exiting the game  \n");
			System.out.println("Here's the solution ");
			sudoku.printAns();
			
		}
	}
}


class Sudoku {
	  // constants for the dimensions of the puzzle
    static final int N = 9;
    static final int SRN = 3;


	// 2D array to store the puzzle
	 int[][] puzzle;
	 
	 int[][] ans;
	

	// stack to store the moves made by the user
	private Stack<int[]> moves;

	// set to store the free positions in the puzzle
	private Set<String> freePositions;

	// level of difficulty (number of empty cells)
	private int difficulty;

	// constructor to generate a new puzzle
	public Sudoku(int difficulty) {
		this.difficulty = difficulty*20;
		puzzle = new int[N][N];
		ans = new int[N][N];
		moves = new Stack<>();
		freePositions = new HashSet<>();
	}

	// method to generate a new puzzle with a given difficulty level
	int[][] generatePuzzle() {

		 // fill the diagonal blocks with random values
	    fillDiagonal();

	    // fill the remaining blocks with valid values
	    fillRemaining(0, SRN);

	    // store the solution in another array
	    for (int i = 0; i < N; i++) {
	        for (int j = 0; j < N; j++) {
	            ans[i][j] = puzzle[i][j];
	        }
	    }

	    // remove randomly some digits to create the puzzle for the selected difficulty level
	    removeKDigits();

	    return puzzle;
	}


	void fillDiagonal()
	{

		for (int i = 0; i<N; i=i+SRN)

			// for diagonal box, start coordinates->i==j
			fillBox(i, i);
	}


	// Returns false if given 3 x 3 block contains num.
	boolean unUsedInBox(int rowStart, int colStart, int num)
	{
		for (int i = 0; i<SRN; i++)
			for (int j = 0; j<SRN; j++)
				if (puzzle[rowStart+i][colStart+j]==num)
					return false;

		return true;
	}

	// Fill a 3 x 3 puzzlerix.
	void fillBox(int row,int col)
	{
		int num;
		for (int i=0; i<SRN; i++)
		{
			for (int j=0; j<SRN; j++)
			{
				do
				{
					num = randomGenerator(N);
				}
				while (!unUsedInBox(row, col, num));

				puzzle[row+i][col+j] = num;
			}
		}
	}

	// Random generator
	int randomGenerator(int num)
	{
		return (int) Math.floor((Math.random()*num+1));
	}

	// Check if safe to put in cell
	boolean CheckIfSafe(int i,int j,int num)
	{
		return (unUsedInRow(i, num) &&
				unUsedInCol(j, num) &&
				unUsedInBox(i-i%SRN, j-j%SRN, num));
	}

	// check in the row for existence
	boolean unUsedInRow(int i,int num)
	{
		for (int j = 0; j<N; j++)
			if (puzzle[i][j] == num)
				return false;
		return true;
	}

	// check in the row for existence
	boolean unUsedInCol(int j,int num)
	{
		for (int i = 0; i<N; i++)
			if (puzzle[i][j] == num)
				return false;
		return true;
	}

	// A recursive function to fill remaining
	// puzzle
	boolean fillRemaining(int i, int j)
	{
		//  System.out.println(i+" "+j);
		if (j>=N && i<N-1)
		{
			i = i + 1;
			j = 0;
		}
		if (i>=N && j>=N)
			return true;

		if (i < SRN)
		{
			if (j < SRN)
				j = SRN;
		}
		else if (i < N-SRN)
		{
			if (j==(int)(i/SRN)*SRN)
				j =  j + SRN;
		}
		else
		{
			if (j == N-SRN)
			{
				i = i + 1;
				j = 0;
				if (i>=N)
					return true;
			}
		}

		for (int num = 1; num<=N; num++)
		{
			if (CheckIfSafe(i, j, num))
			{
				puzzle[i][j] = num;
				if (fillRemaining(i, j+1))
					return true;

				puzzle[i][j] = 0;
			}
		}
		return false;
	}

	// Remove the K no. of digits to
	// complete game
	public void removeKDigits()
	{
		int count = difficulty;
		while (count != 0)
		{
			int cellId = randomGenerator(N*N)-1;

			// System.out.println(cellId);
			// extract coordinates i  and j
			int i = (cellId/N);
			int j = cellId%9;
			if (j != 0)
				j = j - 1;

			// System.out.println(i+" "+j);
			if (puzzle[i][j] != 0)
			{
				count--;
				puzzle[i][j] = 0;
				freePositions.add(i + "-" + j);
			}
		}
	}

	// method to check if the puzzle is solved correctly
	public boolean isSolved() {
		// check if all cells are within the valid range
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (puzzle[i][j] < 1 || puzzle[i][j] > 9) {
					return false;
				}
			}
		}

		// check if all cells have unique values in their rows, columns, and 3x3 regions
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (!isUniqueInRow(i, j) || !isUniqueInColumn(i, j) || !isUniqueInRegion(i, j)) {
					return false;
				}
			}
		}

		return true;
	}


	// method to check if the value at a given cell is unique in its row
	private boolean isUniqueInRow(int row, int col) {
		int value = puzzle[row][col];
		for (int i = 0; i < N; i++) {
			if (i != col && puzzle[row][i] == value) {
				return false;
			}
		}
		return true;
	}

	// method to check if the value at a given cell is unique in its column
	private boolean isUniqueInColumn(int row, int col) {
		int value = puzzle[row][col];
		for (int i = 0; i < N; i++) {
			if (i != row && puzzle[i][col] == value) {
				return false;
			}
		}
		return true;
	}

	// method to check if the value at a given cell is unique in its 3x3 region
	private boolean isUniqueInRegion(int row, int col) {
		int value = puzzle[row][col];
		int regionRow = row / 3;
		int regionCol = col / 3;
		for (int i = regionRow * 3; i < regionRow * 3 + 3; i++) {
			for (int j = regionCol * 3; j < regionCol * 3 + 3; j++) {
				if (i != row && j != col && puzzle[i][j] == value) {
					return false;
				}
			}
		}
		return true;
	}


	// Print sudoku
	public void printSudoku() {
	    // print the column numbers at the top
	    System.out.print("   ");
	    for (int i = 0; i < N; i++) {
	        System.out.print((i+1) + " ");
	        if (i == 2 || i == 5) {
	            System.out.print("  ");
	        }
	    }
	    System.out.println("\n");

	    for (int i = 0; i < N; i++) {
	        // print the row number at the beginning of each row
	        System.out.print((i+1) + "  ");

	        for (int j = 0; j < N; j++) {
	            System.out.print(puzzle[i][j] + " ");
	            if (j == 2 || j == 5) {
	                System.out.print("| ");
	            }
	        }
	        System.out.println();
	        if (i == 2 || i == 5) {
	            System.out.println("   ---------------------");
	        }
	    }
	}

	
	public void printAns() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				System.out.print(ans[i][j] + " ");
				if(j==2||j==5) System.out.print("| ");
			}
			System.out.println();
			if (i == 2 || i == 5) {
				System.out.println("---------------------");
			}
		}
	}

	// method to shuffle the values in the puzzle
	private void shuffle(int[][] puzzle) {
		Random rnd = new Random();
		for (int i = puzzle.length - 1; i > 0; i--) {
			for (int j = puzzle[i].length - 1; j > 0; j--) {
				int row = rnd.nextInt(i + 1);
				int col = rnd.nextInt(j + 1);
				int temp = puzzle[i][j];
				puzzle[i][j] = puzzle[row][col];
				puzzle[row][col] = temp;
			}
		}
	}

	// method to update the puzzle
	public boolean update(int row, int col, int value) {
		// check if the position is free
		if (!freePositions.contains(row + "-" + col)) {
			System.out.println("Cannot update fixed position");
			return false;
		}

		if(!CheckIfSafe(row, col, value)){
			System.out.println("Can't enter this value here");
			return false;
		}

		// update the puzzle and add the move to the stack
		puzzle[row][col] = value;
		moves.push(new int[]{row, col, value});

		return true;
	}



	// method to undo the last update
	public void undo() {
		if (moves.isEmpty()) {
			System.out.println("No moves to undo");
			return;
		}

		// get the last move and update the puzzle
		int[] lastMove = moves.pop();
		puzzle[lastMove[0]][lastMove[1]] = 0;

	}
	
	public void replay() {
	    for (int[] move : moves) {
	        System.out.println("(" + move[0] + ", " + move[1] + ", " + move[2] + ")");
	    }
	}

}

