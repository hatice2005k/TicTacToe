
public class Board {

    private char[][] cells = new char[3][3];

    private Player playerX;
    private Player playerO;

    private Player currentPlayer;

    private char winner;
    private boolean draw;

    // Constructor
    public Board() {

        playerX = new Player('X');
        playerO = new Player('O');

        currentPlayer = playerX;

        resetBoard();
    }

    // Hamle yapma
    public boolean makeMove(int row, int col) {

        // Hücre doluysa hamleyi kabul etme
        if (cells[row][col] != ' ') {
            return false;
        }

        // Kazanan veya beraberlik varsa hamle yapma
        if (winner != ' ' || draw) {
            return false;
        }

        cells[row][col] = currentPlayer.getSymbol();

        // Kazanma kontrolü
        if (checkWinner()) {

            winner = currentPlayer.getSymbol();
            currentPlayer.increaseScore();

        } else if (isBoardFull()) {

            draw = true;

        } else {

            // Sırayı diğer oyuncuya geçir
            switchPlayer();
        }

        return true;
    }

    // Kazanan kontrolü
    private boolean checkWinner() {

        char symbol = currentPlayer.getSymbol();

        // Satır kontrolü
        for (int i = 0; i < 3; i++) {

            if (cells[i][0] == symbol &&
                    cells[i][1] == symbol &&
                    cells[i][2] == symbol) {

                return true;
            }
        }

        // Sütun kontrolü
        for (int i = 0; i < 3; i++) {

            if (cells[0][i] == symbol &&
                    cells[1][i] == symbol &&
                    cells[2][i] == symbol) {

                return true;
            }
        }

        // Sol üstten sağ alta çapraz
        if (cells[0][0] == symbol &&
                cells[1][1] == symbol &&
                cells[2][2] == symbol) {

            return true;
        }

        // Sağ üstten sol alta çapraz
        if (cells[0][2] == symbol &&
                cells[1][1] == symbol &&
                cells[2][0] == symbol) {

            return true;
        }

        return false;
    }

    // Tahtanın dolu olup olmadığını kontrol et
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

    // Oyuncu değiştirme
    private void switchPlayer() {

        if (currentPlayer == playerX) {
            currentPlayer = playerO;
        } else {
            currentPlayer = playerX;
        }
    }

    // Tahtayı sıfırla
    public void resetBoard() {

        for (int row = 0; row < 3; row++) {

            for (int col = 0; col < 3; col++) {

                cells[row][col] = ' ';
            }
        }

        winner = ' ';
        draw = false;
        currentPlayer = playerX;
    }

    // Getter metotları

    public char getCell(int row, int col) {
        return cells[row][col];
    }

    public char getCurrentPlayerSymbol() {
        return currentPlayer.getSymbol();
    }

    public char getWinner() {
        return winner;
    }

    public boolean isDraw() {
        return draw;
    }

    public int getXScore() {
        return playerX.getScore();
    }

    public int getOScore() {
        return playerO.getScore();
    }
}
