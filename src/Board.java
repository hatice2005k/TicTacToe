
public class Board {

    private final char[][] cells = new char[3][3];

    private final Player playerX;
    private final Player playerO;

    private Player currentPlayer;

    private char winner = ' ';
    private boolean draw = false;
    private int[] winningCells;

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

        if (cells[row][col] != ' ' || winner != ' ' || draw) {
            return false;
        }

        cells[row][col] = currentPlayer.getSymbol();

        if (checkWinner()) {
            winner = currentPlayer.getSymbol();
            currentPlayer.increaseScore();
        } else if (isBoardFull()) {
            draw = true;
        } else {
            switchPlayer();
        }

        return true;
    }

    private boolean checkWinner() {

        char symbol = currentPlayer.getSymbol();

        for (int i = 0; i < 3; i++) {

            if (cells[i][0] == symbol &&
                    cells[i][1] == symbol &&
                    cells[i][2] == symbol) {

                winningCells = new int[]{i * 3, i * 3 + 1, i * 3 + 2};
                return true;
            }

            if (cells[0][i] == symbol &&
                    cells[1][i] == symbol &&
                    cells[2][i] == symbol) {

                winningCells = new int[]{i, i + 3, i + 6};
                return true;
            }
        }

        if (cells[0][0] == symbol &&
                cells[1][1] == symbol &&
                cells[2][2] == symbol) {

            winningCells = new int[]{0, 4, 8};
            return true;
        }

        if (cells[0][2] == symbol &&
                cells[1][1] == symbol &&
                cells[2][0] == symbol) {

            winningCells = new int[]{2, 4, 6};
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

    private void switchPlayer() {
        currentPlayer = (currentPlayer == playerX) ? playerO : playerX;
    }

    public void resetBoard() {

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                cells[row][col] = ' ';
            }
        }

        winner = ' ';
        draw = false;
        winningCells = null;
        currentPlayer = playerX;
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

    public char getWinner() {
        return winner;
    }

    public boolean isDraw() {
        return draw;
    }

    public int[] getWinningCells() {
        return winningCells;
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
