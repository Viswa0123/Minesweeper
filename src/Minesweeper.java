import java.util.Random;
import java.util.Scanner;

public class Minesweeper {
    private static final char COVERED = '*';
    private static final char FLAGGED = 'F';
    private static final char MINE = 'M';
    private static final char EMPTY = ' ';

    private final char[][] board;
    private final boolean[][] revealed;
    private final boolean[][] flagged;
    private final int rows;
    private final int cols;
    private final int numMines;
    private int remainingCells;
    
    public Minesweeper(int rows, int cols, int numMines) {
        this.rows = rows;
        this.cols = cols;
        this.numMines = numMines;
        this.board = new char[rows][cols];
        this.revealed = new boolean[rows][cols];
        this.flagged = new boolean[rows][cols];
        this.remainingCells = rows * cols - numMines;
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = COVERED;
            }
        }

        Random random = new Random();
        int minesPlaced = 0;
        while (minesPlaced < numMines) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);
            if (board[r][c] != MINE) {
                board[r][c] = MINE;
                minesPlaced++;
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == MINE) continue;
                int adjacentMines = countAdjacentMines(i, j);
                if (adjacentMines > 0) {
                    board[i][j] = (char) (adjacentMines + '0');
                } else {
                    board[i][j] = EMPTY;
                }
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                    if (board[newRow][newCol] == MINE) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public void displayBoard() {
        System.out.println("Current Board:");
        System.out.print("   ");
        for (int i = 0; i < cols; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < rows; i++) {
            System.out.print(i + "| ");
            for (int j = 0; j < cols; j++) {
                if (flagged[i][j]) {
                    System.out.print(FLAGGED + " ");
                } else if (!revealed[i][j]) {
                    System.out.print(COVERED + " ");
                } else {
                    System.out.print(board[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    public boolean isValidMove(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols && !revealed[row][col];
    }

    public void reveal(int row, int col) {
        if (!isValidMove(row, col)) return;

        revealed[row][col] = true;
        if (board[row][col] == MINE) {
            System.out.println("BOOM! You hit a mine. Game Over.");
            System.exit(0);
        }

        remainingCells--;
        if (board[row][col] == EMPTY) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int newRow = row + i;
                    int newCol = col + j;
                    if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                        if (!revealed[newRow][newCol] && board[newRow][newCol] != MINE) {
                            reveal(newRow, newCol);
                        }
                    }
                }
            }
        }

        if (remainingCells == 0) {
            System.out.println("Congratulations! You win!");
            displayBoard();
            System.exit(0);
        }
    }

    public void flag(int row, int col) {
        if (isValidMove(row, col)) {
            flagged[row][col] = true;
        }
    }

    public void unflag(int row, int col) {
        if (isValidMove(row, col)) {
            flagged[row][col] = false;
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("Welcome to Console Minesweeper (Google Style)");
        System.out.print("Enter board size (rows cols): ");
        int rows = in.nextInt();
        int cols = in.nextInt();
        System.out.print("Enter number of mines: ");
        int numMines = in.nextInt();
        System.out.println();

        Minesweeper game = new Minesweeper(rows, cols, numMines);

        game.displayBoard();

        while (true) {
            System.out.print("Enter move (reveal/flag/unflag row col): ");
            String command = in.next();
            int row = in.nextInt();
            int col = in.nextInt();

            switch (command.toLowerCase().charAt(0)) {
                case 'r':
                    game.reveal(row, col);
                    break;
                case 'f':
                    game.flag(row, col);
                    break;
                case 'u':
                    game.unflag(row, col);
                    break;
                default:
                    System.out.println("Invalid command.");
                    continue;
            }
            game.displayBoard();
        }
    }
}
