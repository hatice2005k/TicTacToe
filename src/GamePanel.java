
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;

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
    private JCheckBox computerModeBox;

    private Board board;
    private JButton[] cells;
    private WinningBoardPanel boardPanel;

    private JLabel statusLabel;
    private JLabel scoreLabel;
    private JLabel drawLabel;

    private boolean computerMode = false;
    private boolean computerThinking = false;
    private boolean isDrawAlreadyCounted = false;

    private int drawCount = 0;

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

        JPanel panel = new JPanel(new BorderLayout(20, 25));
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(40, 35, 35, 35));

        JLabel title = new JLabel("TIC TAC TOE", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "OYUNCULARI BELİRLE",
                SwingConstants.CENTER
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitle.setForeground(new Color(175, 175, 190));

        JPanel heading = new JPanel(new GridLayout(2, 1, 5, 10));
        heading.setBackground(BACKGROUND);
        heading.add(title);
        heading.add(subtitle);

        panel.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3, 1, 15, 15));
        form.setBackground(BACKGROUND);

        playerXField = createNameField();
        playerOField = createNameField();

        form.add(createPlayerInput("OYUNCU X", X_COLOR, playerXField));
        form.add(createPlayerInput("OYUNCU O", O_COLOR, playerOField));

        computerModeBox = new JCheckBox("Bilgisayara karşı oyna");
        computerModeBox.setFont(new Font("Arial", Font.BOLD, 16));
        computerModeBox.setForeground(TEXT);
        computerModeBox.setBackground(BACKGROUND);
        computerModeBox.setFocusPainted(false);

        computerModeBox.addActionListener(e -> {
            playerOField.setText(
                    computerModeBox.isSelected() ? "Bilgisayar" : ""
            );
            playerOField.setEnabled(!computerModeBox.isSelected());
        });

        form.add(computerModeBox);

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
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (board != null &&
                            isCellEmpty(index) &&
                            !isGameFinished() &&
                            !computerThinking) {
                        button.setBackground(HOVER);
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    updateCellColor(index);
                }
            });

            cells[i] = button;
            boardPanel.add(button);
        }

        panel.add(boardPanel, BorderLayout.CENTER);

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

    // WINNING LINE

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

            Rectangle first = cells[winningCells[0]].getBounds();
            Rectangle last = cells[winningCells[2]].getBounds();

            int startX = first.x + first.width / 2;
            int startY = first.y + first.height / 2;

            int endX = last.x + last.width / 2;
            int endY = last.y + last.height / 2;

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

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

        computerMode = computerModeBox.isSelected();

        if (computerMode) {
            playerOName = "Bilgisayar";
        } else if (playerOName.isEmpty()) {
            playerOName = "Oyuncu 2";
        }

        board = new Board(playerXName, playerOName);

        isDrawAlreadyCounted = false;
        computerThinking = false;

        clearBoard();
        updateGameStatus();

        cardLayout.show(screenPanel, "GAME");
        boardPanel.repaint();
    }

    private void handleCellClick(int index) {

        if (board == null ||
                isGameFinished() ||
                computerThinking) {
            return;
        }

        if (computerMode &&
                board.getCurrentPlayerSymbol() != 'X') {
            return;
        }

        int row = index / 3;
        int col = index % 3;

        if (board.makeMove(row, col)) {

            updateBoard();
            updateGameStatus();
            boardPanel.repaint();

            if (computerMode && !isGameFinished()) {
                computerMove();
            }
        }
    }

    // COMPUTER MOVE

    private void computerMove() {

        computerThinking = true;
        statusLabel.setText("BİLGİSAYAR DÜŞÜNÜYOR...");
        statusLabel.setForeground(O_COLOR);

        // Kullanıcı arayüzünü bloklamadan kısa bir bekleme.
        Timer timer = new Timer(450, e -> {

            ((Timer) e.getSource()).stop();

            int[] bestMove = findBestMove();

            if (bestMove != null && !isGameFinished()) {
                board.makeMove(bestMove[0], bestMove[1]);
            }

            computerThinking = false;

            updateBoard();
            updateGameStatus();
            boardPanel.repaint();
        });

        timer.setRepeats(false);
        timer.start();
    }

    private int[] findBestMove() {

        char[][] simulation = new char[3][3];

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                simulation[row][col] = board.getCell(row, col);
            }
        }

        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {

                if (simulation[row][col] == ' ') {

                    simulation[row][col] = 'O';

                    int score = minimax(
                            simulation,
                            0,
                            false
                    );

                    simulation[row][col] = ' ';

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{row, col};
                    }
                }
            }
        }

        return bestMove;
    }

    private int minimax(
            char[][] cells,
            int depth,
            boolean maximizing
    ) {

        if (hasWinner(cells, 'O')) {
            return 10 - depth;
        }

        if (hasWinner(cells, 'X')) {
            return depth - 10;
        }

        if (isFull(cells)) {
            return 0;
        }

        if (maximizing) {

            int bestScore = Integer.MIN_VALUE;

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {

                    if (cells[row][col] == ' ') {

                        cells[row][col] = 'O';

                        int score = minimax(
                                cells,
                                depth + 1,
                                false
                        );

                        cells[row][col] = ' ';

                        bestScore = Math.max(bestScore, score);
                    }
                }
            }

            return bestScore;

        } else {

            int bestScore = Integer.MAX_VALUE;

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {

                    if (cells[row][col] == ' ') {

                        cells[row][col] = 'X';

                        int score = minimax(
                                cells,
                                depth + 1,
                                true
                        );

                        cells[row][col] = ' ';

                        bestScore = Math.min(bestScore, score);
                    }
                }
            }

            return bestScore;
        }
    }

    private boolean hasWinner(char[][] cells, char symbol) {

        for (int i = 0; i < 3; i++) {

            if (cells[i][0] == symbol &&
                    cells[i][1] == symbol &&
                    cells[i][2] == symbol) {
                return true;
            }

            if (cells[0][i] == symbol &&
                    cells[1][i] == symbol &&
                    cells[2][i] == symbol) {
                return true;
            }
        }

        return (cells[0][0] == symbol &&
                cells[1][1] == symbol &&
                cells[2][2] == symbol)
                ||
                (cells[0][2] == symbol &&
                        cells[1][1] == symbol &&
                        cells[2][0] == symbol);
    }

    private boolean isFull(char[][] cells) {

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {

                if (cells[row][col] == ' ') {
                    return false;
                }
            }
        }

        return true;
    }

    // UI UPDATE

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
        computerThinking = false;

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
        playerOField.setText(computerMode ? "" : playerOName);

        computerModeBox.setSelected(computerMode);
        playerOField.setEnabled(!computerMode);

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
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }
}
