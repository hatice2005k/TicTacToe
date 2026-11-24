
import java.util.ArrayList;
import java.util.List;

public class Board {

    private final char[][] cells = new char[3][3];

    private final Player playerX;
    private final Player playerO;

    private Player currentPlayer;
    private Character winner;
    private boolean draw;

    private final List<Integer> winningCells = new ArrayList<>();

    public Board(String playerXName, String playerOName) {
        playerX = new Player(playerXName, 'X');
        playerO = new Player(playerOName, 'O');

        currentPlayer = playerX;
        resetBoard();
    }

    public boolean makeMove(int row, int col) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3) {
            return false;
        }

        if (cells[row][col] != ' ' || winner != null || draw) {
            return false;
        }

        cells[row][col] = currentPlayer.getSymbol();

        if (checkWinner()) {
            winner = currentPlayer.getSymbol();
            currentPlayer.increaseScore();
        } else if (isBoardFull()) {
            draw = true;
        } else {
            currentPlayer = (currentPlayer == playerX) ? playerO : playerX;
        }

        return true;
    }

    private boolean checkWinner() {
        winningCells.clear();

        // Rows
        for (int row = 0; row < 3; row++) {
            if (cells[row][0] != ' '
                    && cells[row][0] == cells[row][1]
                    && cells[row][1] == cells[row][2]) {

                winningCells.add(row * 3);
                winningCells.add(row * 3 + 1);
                winningCells.add(row * 3 + 2);
                return true;
            }
        }

        // Columns
        for (int col = 0; col < 3; col++) {
            if (cells[0][col] != ' '
                    && cells[0][col] == cells[1][col]
                    && cells[1][col] == cells[2][col]) {

                winningCells.add(col);
                winningCells.add(3 + col);
                winningCells.add(6 + col);
                return true;
            }
        }

        // Main diagonal
        if (cells[0][0] != ' '
                && cells[0][0] == cells[1][1]
                && cells[1][1] == cells[2][2]) {

            winningCells.add(0);
            winningCells.add(4);
            winningCells.add(8);
            return true;
        }

        // Other diagonal
        if (cells[0][2] != ' '
                && cells[0][2] == cells[1][1]
                && cells[1][1] == cells[2][0]) {

            winningCells.add(2);
            winningCells.add(4);
            winningCells.add(6);
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (cells[row][col] == ' ') {
                    return false;
                }
            }
        }

        return true;
    }

    public void resetBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                cells[row][col] = ' ';
            }
        }

        currentPlayer = playerX;
        winner = null;
        draw = false;
        winningCells.clear();
    }

    public void resetScores() {
        playerX.resetScore();
        playerO.resetScore();
    }

    public char getCell(int row, int col) {
        return cells[row][col];
    }

    public char getCurrentPlayerSymbol() {
        return currentPlayer.getSymbol();
    }

    public String getCurrentPlayerName() {
        return currentPlayer.getName();
    }

    public Character getWinner() {
        return winner;
    }

    public boolean isDraw() {
        return draw;
    }

    public List<Integer> getWinningCells() {
        return new ArrayList<>(winningCells);
    }

    public int getXScore() {
        return playerX.getScore();
    }

    public int getOScore() {
        return playerO.getScore();
    }

    public String getXName() {
        return playerX.getName();
    }

    public String getOName() {
        return playerO.getName();
    }
}
