
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GamePanel extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel screenPanel;

    private final Color BACKGROUND = new Color(25, 25, 34);
    private final Color CELL = new Color(43, 43, 55);
    private final Color HOVER = new Color(65, 65, 82);
    private final Color WIN = new Color(40, 115, 85);
    private final Color X_COLOR = new Color(100, 180, 255);
    private final Color O_COLOR = new Color(255, 130, 130);
    private final Color TEXT = new Color(240, 240, 245);

    private JTextField playerXField;
    private JTextField playerOField;

    private Board board;
    private JButton[] cells;
    private WinningBoardPanel boardPanel;

    private JLabel statusLabel;
    private JLabel scoreLabel;
    private JLabel drawLabel;

    private int drawCount = 0;
    private boolean isDrawAlreadyCounted = false;

    private String playerXName;
    private String playerOName;

    public GamePanel() {

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        cardLayout = new CardLayout();
        screenPanel = new JPanel(cardLayout);

        screenPanel.add(createStartScreen(), "START");
        screenPanel.add(createGameScreen(), "GAME");

        add(screenPanel, BorderLayout.CENTER);

        cardLayout.show(screenPanel, "START");
    }

    // START SCREEN

    private JPanel createStartScreen() {

        JPanel panel = new JPanel(new BorderLayout(20, 30));
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(45, 35, 40, 35));

        JLabel title = new JLabel("TIC TAC TOE", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "OYUNCULARI BELİRLE",
                SwingConstants.CENTER
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitle.setForeground(new Color(175, 175, 190));

        JPanel heading = new JPanel(new GridLayout(2, 1, 5, 12));
        heading.setBackground(BACKGROUND);
        heading.add(title);
        heading.add(subtitle);

        panel.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(2, 1, 20, 20));
        form.setBackground(BACKGROUND);

        playerXField = createNameField();
        playerOField = createNameField();

        form.add(createPlayerInput("OYUNCU X", X_COLOR, playerXField));
        form.add(createPlayerInput("OYUNCU O", O_COLOR, playerOField));

        panel.add(form, BorderLayout.CENTER);

        JButton startButton = createButton(
                "OYUNU BAŞLAT",
                new Color(55, 90, 125)
        );

        startButton.addActionListener(e -> startGame());

        panel.add(startButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createPlayerInput(
            String title,
            Color color,
            JTextField field
    ) {

        JPanel panel = new JPanel(new BorderLayout(5, 10));
        panel.setBackground(BACKGROUND);

        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 17));
        label.setForeground(color);

        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private JTextField createNameField() {

        JTextField field = new JTextField();

        field.setFont(new Font("Arial", Font.PLAIN, 19));
        field.setForeground(Color.WHITE);
        field.setBackground(CELL);
        field.setCaretColor(Color.WHITE);

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(75, 75, 90)),
                new EmptyBorder(10, 12, 10, 12)
        ));

        return field;
    }

    // GAME SCREEN

    private JPanel createGameScreen() {

        JPanel panel = new JPanel(new BorderLayout(10, 20));
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(25, 25, 20, 25));

        // Üst bilgiler
        JPanel topPanel = new JPanel(new GridLayout(3, 1, 5, 8));
        topPanel.setBackground(BACKGROUND);

        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 23));
        statusLabel.setForeground(TEXT);

        scoreLabel = new JLabel("", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        scoreLabel.setForeground(new Color(190, 190, 205));

        drawLabel = new JLabel("Beraberlik: 0", SwingConstants.CENTER);
        drawLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        drawLabel.setForeground(new Color(160, 160, 175));

        topPanel.add(statusLabel);
        topPanel.add(scoreLabel);
        topPanel.add(drawLabel);

        panel.add(topPanel, BorderLayout.NORTH);

        // Oyun tahtası
        boardPanel = new WinningBoardPanel();
        boardPanel.setLayout(new GridLayout(3, 3, 10, 10));
        boardPanel.setBackground(BACKGROUND);

        cells = new JButton[9];

        for (int i = 0; i < 9; i++) {

            final int index = i;

            JButton button = new JButton("");

            button.setFont(new Font("Arial", Font.BOLD, 65));
            button.setForeground(TEXT);
            button.setBackground(CELL);

            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setOpaque(true);
            button.setContentAreaFilled(true);

            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            button.addActionListener(e -> handleCellClick(index));

            button.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (board != null &&
                            isCellEmpty(index) &&
                            !isGameFinished()) {
                        button.setBackground(HOVER);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    updateCellColor(index);
                }
            });

            cells[i] = button;
            boardPanel.add(button);
        }

        panel.add(boardPanel, BorderLayout.CENTER);

        // Alt butonlar
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        bottomPanel.setBackground(BACKGROUND);

        JButton newRoundButton = createButton(
                "YENİ TUR",
                new Color(55, 90, 125)
        );

        JButton menuButton = createButton(
                "ANA MENÜ",
                new Color(65, 65, 80)
        );

        newRoundButton.addActionListener(e -> resetGame());
        menuButton.addActionListener(e -> showStartScreen());

        bottomPanel.add(newRoundButton);
        bottomPanel.add(menuButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    // WINNING LINE DRAWING

    private class WinningBoardPanel extends JPanel {

        @Override
        protected void paintChildren(Graphics g) {

            super.paintChildren(g);

            if (board == null || board.getWinner() == ' ') {
                return;
            }

            int[] winningCells = board.getWinningCells();

            if (winningCells == null || winningCells.length != 3) {
                return;
            }

            JButton firstCell = cells[winningCells[0]];
            JButton lastCell = cells[winningCells[2]];

            Rectangle firstBounds = firstCell.getBounds();
            Rectangle lastBounds = lastCell.getBounds();

            int startX = firstBounds.x + firstBounds.width / 2;
            int startY = firstBounds.y + firstBounds.height / 2;

            int endX = lastBounds.x + lastBounds.width / 2;
            int endY = lastBounds.y + lastBounds.height / 2;

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            // Kazanma çizgisi
            g2.setColor(new Color(160, 255, 205));
            g2.setStroke(new BasicStroke(
                    7,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND
            ));

            g2.drawLine(startX, startY, endX, endY);

            g2.dispose();
        }
    }

    // GAME OPERATIONS

    private void startGame() {

        playerXName = playerXField.getText().trim();
        playerOName = playerOField.getText().trim();

        if (playerXName.isEmpty()) {
            playerXName = "Oyuncu 1";
        }

        if (playerOName.isEmpty()) {
            playerOName = "Oyuncu 2";
        }

        board = new Board(playerXName, playerOName);

        isDrawAlreadyCounted = false;

        clearBoard();
        updateGameStatus();

        cardLayout.show(screenPanel, "GAME");
        boardPanel.repaint();
    }

    private void handleCellClick(int index) {

        if (board == null || isGameFinished()) {
            return;
        }

        int row = index / 3;
        int col = index % 3;

        if (board.makeMove(row, col)) {

            updateBoard();
            updateGameStatus();

            boardPanel.repaint();
        }
    }

    private void updateBoard() {

        for (int i = 0; i < 9; i++) {

            int row = i / 3;
            int col = i % 3;

            char symbol = board.getCell(row, col);

            cells[i].setText(
                    symbol == ' ' ? "" : String.valueOf(symbol)
            );

            if (symbol == 'X') {
                cells[i].setForeground(X_COLOR);
            } else if (symbol == 'O') {
                cells[i].setForeground(O_COLOR);
            }

            updateCellColor(i);
        }
    }

    private void updateCellColor(int index) {

        if (board == null) {
            return;
        }

        int[] winningCells = board.getWinningCells();

        if (winningCells != null) {

            for (int winningIndex : winningCells) {

                if (winningIndex == index) {
                    cells[index].setBackground(WIN);
                    return;
                }
            }
        }

        if (isCellEmpty(index) &&
                !isGameFinished() &&
                cells[index].getMousePosition() != null) {

            cells[index].setBackground(HOVER);

        } else {

            cells[index].setBackground(CELL);
        }
    }

    private void updateGameStatus() {

        if (board.getWinner() != ' ') {

            String winnerName = board.getWinner() == 'X'
                    ? board.getXName()
                    : board.getOName();

            statusLabel.setText(winnerName + " KAZANDI!");
            statusLabel.setForeground(new Color(100, 220, 155));

        } else if (board.isDraw()) {

            statusLabel.setText("BERABERE!");
            statusLabel.setForeground(new Color(255, 190, 100));

            if (!isDrawAlreadyCounted) {
                drawCount++;
                isDrawAlreadyCounted = true;
            }

        } else {

            statusLabel.setText(
                    board.getCurrentPlayerName()
                            + " (" + board.getCurrentPlayerSymbol() + ") OYNUYOR"
            );

            statusLabel.setForeground(TEXT);
        }

        scoreLabel.setText(
                board.getXName() + ": " + board.getXScore()
                        + "     |     "
                        + board.getOName() + ": " + board.getOScore()
        );

        drawLabel.setText("Beraberlik: " + drawCount);
    }

    private boolean isCellEmpty(int index) {

        int row = index / 3;
        int col = index % 3;

        return board.getCell(row, col) == ' ';
    }

    private boolean isGameFinished() {
        return board.getWinner() != ' ' || board.isDraw();
    }

    private void resetGame() {

        board.resetBoard();
        isDrawAlreadyCounted = false;

        clearBoard();
        updateGameStatus();

        boardPanel.repaint();
    }

    private void clearBoard() {

        for (JButton cell : cells) {
            cell.setText("");
            cell.setForeground(TEXT);
            cell.setBackground(CELL);
        }
    }

    private void showStartScreen() {

        playerXField.setText(playerXName);
        playerOField.setText(playerOName);

        cardLayout.show(screenPanel, "START");
    }

    // BUTTON DESIGN

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 17));
        button.setForeground(Color.WHITE);
        button.setBackground(color);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, 48));

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }
}
