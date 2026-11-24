
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    // Renkler
    private final Color BACKGROUND = new Color(25, 28, 38);
    private final Color CELL_COLOR = new Color(48, 53, 68);
    private final Color X_COLOR = new Color(70, 190, 180);
    private final Color O_COLOR = new Color(235, 115, 135);
    private final Color WIN_COLOR = new Color(40, 115, 85);

    // Oyun motoru
    private Board board;

    // Arayüz bileşenleri
    private JButton[] cells = new JButton[9];

    private JLabel statusLabel;
    private JLabel xScoreLabel;
    private JLabel oScoreLabel;

    private JButton restartButton;

    public GamePanel() {

        board = new Board();

        setLayout(new BorderLayout(15, 15));
        setBackground(BACKGROUND);

        setBorder(
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        add(createHeader(), BorderLayout.NORTH);
        add(createBoard(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    // Başlık ve skor tablosu
    private JPanel createHeader() {

        JPanel header = new JPanel();

        header.setLayout(
                new BoxLayout(header, BoxLayout.Y_AXIS)
        );

        header.setBackground(BACKGROUND);

        JLabel title = new JLabel("TIC TAC TOE");

        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel scorePanel = new JPanel(
                new GridLayout(1, 2, 15, 0)
        );

        scorePanel.setBackground(BACKGROUND);

        xScoreLabel = new JLabel("X : 0", SwingConstants.CENTER);
        oScoreLabel = new JLabel("O : 0", SwingConstants.CENTER);

        styleScoreLabel(xScoreLabel, X_COLOR);
        styleScoreLabel(oScoreLabel, O_COLOR);

        scorePanel.add(xScoreLabel);
        scorePanel.add(oScoreLabel);

        header.add(title);
        header.add(Box.createVerticalStrut(20));
        header.add(scorePanel);

        return header;
    }

    // Skor etiketlerinin tasarımı
    private void styleScoreLabel(JLabel label, Color color) {

        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(color);
        label.setOpaque(true);
        label.setBackground(CELL_COLOR);

        label.setBorder(
                BorderFactory.createEmptyBorder(12, 10, 12, 10)
        );
    }

    // Oyun tahtası
    private JPanel createBoard() {

        JPanel gameBoard = new JPanel(
                new GridLayout(3, 3, 10, 10)
        );

        gameBoard.setBackground(BACKGROUND);

        for (int i = 0; i < 9; i++) {

            JButton cell = new JButton("");

            cell.setFont(new Font("Arial", Font.BOLD, 55));
            cell.setForeground(Color.WHITE);
            cell.setBackground(CELL_COLOR);

            cell.setFocusPainted(false);
            cell.setBorderPainted(false);

            cells[i] = cell;

            final int index = i;

            cell.addActionListener(e -> handleMove(index));

            gameBoard.add(cell);
        }

        return gameBoard;
    }

    // Tıklanan hücreyi işle
    private void handleMove(int index) {

        int row = index / 3;
        int col = index % 3;

        boolean validMove = board.makeMove(row, col);

        if (!validMove) {
            return;
        }

        char symbol = board.getCell(row, col);

        cells[index].setText(String.valueOf(symbol));

        if (symbol == 'X') {
            cells[index].setForeground(X_COLOR);
        } else {
            cells[index].setForeground(O_COLOR);
        }

        updateGameStatus();
    }

    // Oyun durumunu güncelle
    private void updateGameStatus() {

        char winner = board.getWinner();

        if (winner != ' ') {

            statusLabel.setText("Player " + winner + " Wins!");

            highlightWinningCells();

            updateScores();

        } else if (board.isDraw()) {

            statusLabel.setText("It's a Draw!");

        } else {

            statusLabel.setText(
                    "Player " +
                            board.getCurrentPlayerSymbol() +
                            "'s Turn"
            );
        }
    }

    // Kazanan hücreleri renklendir
    private void highlightWinningCells() {

        int[] winningCells = board.getWinningCells();

        if (winningCells == null) {
            return;
        }

        for (int index : winningCells) {

            cells[index].setBackground(WIN_COLOR);
        }
    }

    // Skorları güncelle
    private void updateScores() {

        xScoreLabel.setText("X : " + board.getXScore());
        oScoreLabel.setText("O : " + board.getOScore());
    }

    // Alt bölüm
    private JPanel createFooter() {

        JPanel footer = new JPanel();

        footer.setLayout(
                new BoxLayout(footer, BoxLayout.Y_AXIS)
        );

        footer.setBackground(BACKGROUND);

        statusLabel = new JLabel("Player X's Turn");

        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        restartButton = new JButton("NEW GAME");

        restartButton.setFont(new Font("Arial", Font.BOLD, 14));
        restartButton.setForeground(Color.WHITE);
        restartButton.setBackground(new Color(95, 85, 190));

        restartButton.setFocusPainted(false);
        restartButton.setBorderPainted(false);
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        restartButton.addActionListener(e -> resetGame());

        footer.add(statusLabel);
        footer.add(Box.createVerticalStrut(12));
        footer.add(restartButton);

        return footer;
    }

    // Oyunu yeniden başlat
    private void resetGame() {

        board.resetBoard();

        for (JButton cell : cells) {

            cell.setText("");
            cell.setForeground(Color.WHITE);
            cell.setBackground(CELL_COLOR);
        }

        statusLabel.setText("Player X's Turn");

        updateScores();
    }
}
