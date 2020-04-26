package common;

import java.io.Serializable;

public class Game implements Serializable {

    private int[][] board;
    private PlayerType currentPlayer = PlayerType.BLACK;
    private GameStatus status = GameStatus.IN_PROGRESS;

    public Game(int boardSize) {
        board = new int[boardSize][boardSize];
    }

    public PlayerType getCurrentPlayer() {
        return currentPlayer;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public GameStatus putStone(int rowPos, int colPos) {
        boolean isRowPosCorrect = 0 <= rowPos && rowPos < board.length;
        boolean isColPosCorrect = 0 <= colPos && colPos < board.length;

        if (!isRowPosCorrect || !isColPosCorrect) {
            status = GameStatus.ERROR;
        } else {
            if (board[rowPos][colPos] != 0) {
                status = GameStatus.ERROR;
            } else {
                board[rowPos][colPos] = currentPlayer.ordinal() + 1;
                status = isEnd(rowPos, colPos) ? GameStatus.END : GameStatus.IN_PROGRESS;
            }
        }

        return status;
    }

    private boolean isEnd(int rowPos, int colPos) {
        int stoneLineLength = 1;

        // Check x=-y direction
        for (int i = rowPos - 1, j = colPos - 1; i >= 0 && j >= 0 && board[i][j] == board[rowPos][colPos]; i--, j--) stoneLineLength++;
        for (int i = rowPos + 1, j = colPos + 1; i < board.length && j < board.length && board[i][j] == board[rowPos][colPos]; i++, j++) stoneLineLength++;
        if (stoneLineLength >= 3) return true;
        stoneLineLength = 1;

        // Check x=y direction
        for (int i = rowPos + 1, j = colPos - 1; i < board.length && j >= 0 && board[i][j] == board[rowPos][colPos]; i++, j--) stoneLineLength++;
        for (int i = rowPos - 1, j = colPos + 1; i >= 0 && j < board.length && board[i][j] == board[rowPos][colPos]; i--, j++) stoneLineLength++;
        if (stoneLineLength >= 3) return true;
        stoneLineLength = 1;

        // Check y=0 direction
        for (int i = rowPos - 1; i >= 0 && board[i][colPos] == board[rowPos][colPos]; i--) stoneLineLength++;
        for (int i = rowPos + 1; i < board.length && board[i][colPos] == board[rowPos][colPos]; i++) stoneLineLength++;
        if (stoneLineLength >= 3) return true;
        stoneLineLength = 1;

        // Check x=0 direction
        for (int j = colPos - 1; j >= 0 && board[rowPos][j] == board[rowPos][colPos]; j--) stoneLineLength++;
        for (int j = colPos + 1; j < board.length && board[rowPos][j] == board[rowPos][colPos]; j++) stoneLineLength++;
        if (stoneLineLength >= 3) return true;

        return false;
    }

    public void changePlayer() {
        currentPlayer = (currentPlayer == PlayerType.BLACK) ? PlayerType.WHITE : PlayerType.BLACK;
    }

    public void printBoard() {
        int size = board.length;

        // Print column indices
        System.out.print("\t ");
        for (int i = 0; i < size; i++) System.out.print(" " + i + "  ");
        System.out.println();

        //Print rows
        for (int i = 0; i < size; i++) {
            System.out.println("\n\t" + "----".repeat(size) + "-");
            System.out.print(i + "\t|");
            for (int j = 0; j < size; j++) {
                char cellChar = (board[i][j] == 0) ? ' ' : (board[i][j] == 1) ? 'X' : 'O';
                System.out.print(" " + cellChar + " |");
            }
        }
        System.out.println("\n\t" + "----".repeat(size) + "-");
    }
}
