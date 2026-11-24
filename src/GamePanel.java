
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel {

    private static final Color NAVY = new Color(11, 16, 45);
    private static final Color NAVY_CARD = new Color(22, 29, 62);
    private static final Color NAVY_CELL = new Color(30, 36, 73);
    private static final Color BURGUNDY = new Color(104, 6, 27);
    private static final Color BURGUNDY_LIGHT = new Color(145, 24, 53);
    private static final Color PINK = new Color(253, 205, 216);
    private static final Color WHITE = new Color(250, 246, 248);
    private static final Color MUTED = new Color(165, 169, 190);

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel screens = new JPanel(cardLayout);

    private JTextField playerXField;
    private JTextField playerOField;
    private JCheckBox computerCheck;

    private JLabel statusLabel;
    private JLabel xNameLabel;
    private JLabel oNameLabel;
    private JLabel xScoreLabel;
    private JLabel oScoreLabel;
    private JLabel drawLabel;

    private final JButton[][] cells = new JButton[3][3];

    private Board board;
    private boolean computerMode;
    private boolean computerThinking;
    private int draws;

    private Timer computerTimer;

    private final List<Integer> winningCells = new ArrayList<>();

    public GamePanel() {
        setLayout(new BorderLayout());
        setBackground(NAVY);

        screens.setOpaque(false);
        screens.add(createStartScreen(), "START");
        screens.add(createGameScreen(), "GAME");

        add(screens, BorderLayout.CENTER);

        cardLayout.show(screens, "START");
    }

    // =====================================================
    // START SCREEN
    // =====================================================

    private JPanel createStartScreen() {
        GradientPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(30, 35, 30, 35));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));



        JLabel title = makeLabel(
                "TIC TAC TOE",
                39, WHITE, Font.BOLD
        );

        JLabel subtitle = makeLabel(
                "A classic game, beautifully redesigned.",
                14, MUTED, Font.PLAIN
        );


        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(Box.createVerticalStrut(15));

        content.add(Box.createVerticalStrut(13));
        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(38));

        JPanel form = roundedPanel(NAVY_CARD, 24);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel setupTitle = makeLabel(
                "PLAYER SETUP",
                12, PINK, Font.BOLD
        );

        form.add(setupTitle);
        form.add(Box.createVerticalStrut(22));

        form.add(makeLabel("PLAYER ONE  ·  X", 11, MUTED, Font.BOLD));
        form.add(Box.createVerticalStrut(8));

        playerXField = createTextField("Player X");
        form.add(playerXField);

        form.add(Box.createVerticalStrut(20));

        form.add(makeLabel("PLAYER TWO  ·  O", 11, MUTED, Font.BOLD));
        form.add(Box.createVerticalStrut(8));

        playerOField = createTextField("Player O");
        form.add(playerOField);

        form.add(Box.createVerticalStrut(18));

        computerCheck = new JCheckBox("Play against computer");
        computerCheck.setFont(new Font("SansSerif", Font.PLAIN, 13));
        computerCheck.setForeground(PINK);
        computerCheck.setOpaque(false);
        computerCheck.setFocusPainted(false);
        computerCheck.setAlignmentX(Component.LEFT_ALIGNMENT);

        computerCheck.addActionListener(e -> {
            boolean selected = computerCheck.isSelected();

            playerOField.setEnabled(!selected);
            playerOField.setText(selected ? "Computer" : "");
        });

        form.add(computerCheck);

        content.add(form);
        content.add(Box.createVerticalStrut(22));

        JButton startButton = createButton(
                "START GAME",
                BURGUNDY,
                PINK
        );

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        startButton.addActionListener(e -> startGame());

        content.add(startButton);
        content.add(Box.createVerticalStrut(23));

        JLabel footer = makeLabel(
                "DESIGNED FOR THE LOVE OF THE GAME",
                10, MUTED, Font.PLAIN
        );

        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(footer);

        panel.add(content);

        return panel;
    }

    // =====================================================
    // GAME SCREEN
    // =====================================================

    private JPanel createGameScreen() {
        GradientPanel panel = new GradientPanel();

        panel.setLayout(new BorderLayout(0, 15));
        panel.setBorder(new EmptyBorder(22, 25, 25, 25));

        // TOP BAR
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel logo = makeLabel(
                "TIC TAC TOE  ✦",
                14, PINK, Font.BOLD
        );

        JButton menuButton = createButton(
                "MAIN MENU",
                NAVY_CARD,
                WHITE
        );

        menuButton.addActionListener(e -> showStartScreen());

        topBar.add(logo, BorderLayout.WEST);
        topBar.add(menuButton, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        // CENTER CONTENT
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel heading = makeLabel(
                "GAME ARENA",
                27, WHITE, Font.BOLD
        );

        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusLabel = makeLabel(
                "X's turn",
                14, PINK, Font.BOLD
        );

        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(heading);
        center.add(Box.createVerticalStrut(7));
        center.add(statusLabel);
        center.add(Box.createVerticalStrut(22));

        // SCORE CARDS
        JPanel scorePanel = new JPanel(new GridLayout(1, 3, 10, 0));
        scorePanel.setOpaque(false);

        JPanel xCard = createScoreCard("PLAYER X", PINK);
        JPanel drawCard = createScoreCard("DRAWS", WHITE);
        JPanel oCard = createScoreCard("PLAYER O", PINK);

        xNameLabel = (JLabel) xCard.getClientProperty("nameLabel");
        xScoreLabel = (JLabel) xCard.getClientProperty("scoreLabel");

        oNameLabel = (JLabel) oCard.getClientProperty("nameLabel");
        oScoreLabel = (JLabel) oCard.getClientProperty("scoreLabel");

        drawLabel = (JLabel) drawCard.getClientProperty("scoreLabel");

        scorePanel.add(xCard);
        scorePanel.add(drawCard);
        scorePanel.add(oCard);

        center.add(scorePanel);
        center.add(Box.createVerticalStrut(23));

        // GAME BOARD
        JPanel boardContainer = roundedPanel(NAVY_CARD, 25);
        boardContainer.setBorder(new EmptyBorder(12, 12, 12, 12));
        boardContainer.setLayout(new BorderLayout());

        WinningBoardPanel boardPanel = new WinningBoardPanel();
        boardPanel.setLayout(new GridLayout(3, 3, 9, 9));
        boardPanel.setOpaque(false);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {

                final int r = row;
                final int c = col;

                JButton cell = createCellButton();

                cells[row][col] = cell;

                cell.addActionListener(e -> handleMove(r, c));

                boardPanel.add(cell);
            }
        }

        boardContainer.add(boardPanel, BorderLayout.CENTER);

        center.add(boardContainer);
        center.add(Box.createVerticalStrut(22));

        // ACTION BUTTONS
        JPanel actions = new JPanel(new GridLayout(1, 2, 12, 0));
        actions.setOpaque(false);

        JButton newRoundButton = createButton(
                "NEW ROUND",
                BURGUNDY,
                PINK
        );

        JButton resetButton = createButton(
                "RESET SCORES",
                NAVY_CARD,
                WHITE
        );

        newRoundButton.addActionListener(e -> resetGame());
        resetButton.addActionListener(e -> resetScores());

        actions.add(newRoundButton);
        actions.add(resetButton);

        center.add(actions);

        panel.add(center, BorderLayout.CENTER);

        return panel;
    }

    // =====================================================
    // GAME START
    // =====================================================

    private void startGame() {
        stopComputerTimer();

        String xName = playerXField.getText().trim();
        String oName = playerOField.getText().trim();

        if (xName.isEmpty()) {
            xName = "Player X";
        }

        if (oName.isEmpty()) {
            oName = "Player O";
        }

        computerMode = computerCheck.isSelected();

        if (computerMode) {
            oName = "Computer";
        }

        board = new Board(xName, oName);

        draws = 0;
        computerThinking = false;

        clearBoardUI();
        updateScoreboard();
        updateStatus();

        cardLayout.show(screens, "GAME");
    }

    // =====================================================
    // PLAYER MOVES
    // =====================================================

    private void handleMove(int row, int col) {
        if (board == null || computerThinking) {
            return;
        }

        if (computerMode && board.getCurrentPlayerSymbol() == 'O') {
            return;
        }

        if (!board.makeMove(row, col)) {
            return;
        }

        updateBoardUI();
        checkGameState();

        if (computerMode
                && board.getWinner() == null
                && !board.isDraw()
                && board.getCurrentPlayerSymbol() == 'O') {

            computerThinking = true;
            statusLabel.setText("COMPUTER IS THINKING...");

            computerTimer = new Timer(500, e -> {
                stopComputerTimer();
                makeComputerMove();
            });

            computerTimer.setRepeats(false);
            computerTimer.start();
        }
    }

    // =====================================================
    // COMPUTER MOVE
    // =====================================================

    private void makeComputerMove() {
        if (board == null) {
            computerThinking = false;
            return;
        }

        int[] move = findBestMove();

        if (move != null) {
            board.makeMove(move[0], move[1]);
        }

        computerThinking = false;

        updateBoardUI();
        checkGameState();
    }

    private int[] findBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        char[][] state = getBoardState();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {

                if (state[row][col] == ' ') {

                    state[row][col] = 'O';

                    int score = minimax(state, 0, false);

                    state[row][col] = ' ';

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{row, col};
                    }
                }
            }
        }

        return bestMove;
    }

    // =====================================================
    // MINIMAX ALGORITHM
    // =====================================================

    private int minimax(char[][] state, int depth, boolean maximizing) {

        if (hasWinner(state, 'O')) {
            return 10 - depth;
        }

        if (hasWinner(state, 'X')) {
            return depth - 10;
        }

        if (isFull(state)) {
            return 0;
        }

        if (maximizing) {
            int best = Integer.MIN_VALUE;

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {

                    if (state[row][col] == ' ') {

                        state[row][col] = 'O';

                        best = Math.max(
                                best,
                                minimax(state, depth + 1, false)
                        );

                        state[row][col] = ' ';
                    }
                }
            }

            return best;

        } else {
            int best = Integer.MAX_VALUE;

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {

                    if (state[row][col] == ' ') {

                        state[row][col] = 'X';

                        best = Math.min(
                                best,
                                minimax(state, depth + 1, true)
                        );

                        state[row][col] = ' ';
                    }
                }
            }

            return best;
        }
    }

    private char[][] getBoardState() {
        char[][] state = new char[3][3];

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                state[row][col] = board.getCell(row, col);
            }
        }

        return state;
    }

    private boolean hasWinner(char[][] state, char symbol) {

        for (int i = 0; i < 3; i++) {

            if (state[i][0] == symbol
                    && state[i][1] == symbol
                    && state[i][2] == symbol) {
                return true;
            }

            if (state[0][i] == symbol
                    && state[1][i] == symbol
                    && state[2][i] == symbol) {
                return true;
            }
        }

        return (state[0][0] == symbol
                && state[1][1] == symbol
                && state[2][2] == symbol)

                || (state[0][2] == symbol
                && state[1][1] == symbol
                && state[2][0] == symbol);
    }

    private boolean isFull(char[][] state) {
        for (char[] row : state) {
            for (char cell : row) {
                if (cell == ' ') {
                    return false;
                }
            }
        }

        return true;
    }

    // =====================================================
    // GAME STATE
    // =====================================================

    private void checkGameState() {

        if (board.getWinner() != null) {

            winningCells.clear();
            winningCells.addAll(board.getWinningCells());

            statusLabel.setText(
                    board.getWinner() + " WINS!"
            );

            updateBoardUI();
            updateScoreboard();

        } else if (board.isDraw()) {

            draws++;

            statusLabel.setText("IT'S A DRAW!");

            updateScoreboard();

        } else {
            updateStatus();
        }
    }

    private void updateStatus() {
        if (board == null) {
            return;
        }

        statusLabel.setText(
                board.getCurrentPlayerName()
                        + "'S TURN  ·  "
                        + board.getCurrentPlayerSymbol()
        );
    }

    // =====================================================
    // BOARD UI
    // =====================================================

    private void updateBoardUI() {
        if (board == null) {
            return;
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {

                char value = board.getCell(row, col);
                JButton cell = cells[row][col];

                cell.setText(value == ' ' ? "" : String.valueOf(value));

                if (value == 'X') {
                    cell.setForeground(PINK);
                } else if (value == 'O') {
                    cell.setForeground(BURGUNDY_LIGHT);
                }

                cell.setEnabled(
                        value == ' '
                                && board.getWinner() == null
                                && !board.isDraw()
                );
            }
        }

        repaint();
    }

    private void clearBoardUI() {
        winningCells.clear();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                cells[row][col].setText("");
                cells[row][col].setForeground(WHITE);
                cells[row][col].setEnabled(true);
            }
        }

        repaint();
    }

    // =====================================================
    // SCOREBOARD
    // =====================================================

    private void updateScoreboard() {
        if (board == null) {
            return;
        }

        xNameLabel.setText(board.getXName());
        oNameLabel.setText(board.getOName());

        xScoreLabel.setText(String.valueOf(board.getXScore()));
        oScoreLabel.setText(String.valueOf(board.getOScore()));
        drawLabel.setText(String.valueOf(draws));
    }

    private void resetGame() {
        if (board == null) {
            return;
        }

        stopComputerTimer();

        computerThinking = false;

        board.resetBoard();

        clearBoardUI();
        updateScoreboard();
        updateStatus();
    }

    private void resetScores() {
        if (board == null) {
            return;
        }

        stopComputerTimer();

        computerThinking = false;

        board.resetScores();
        board.resetBoard();

        draws = 0;

        clearBoardUI();
        updateScoreboard();
        updateStatus();
    }

    private void showStartScreen() {
        stopComputerTimer();

        computerThinking = false;

        cardLayout.show(screens, "START");
    }

    private void stopComputerTimer() {
        if (computerTimer != null) {
            computerTimer.stop();
            computerTimer = null;
        }
    }

    // =====================================================
    // UI COMPONENTS
    // =====================================================

    private JLabel makeLabel(
            String text,
            int size,
            Color color,
            int style
    ) {
        JLabel label = new JLabel(text);

        label.setFont(new Font("SansSerif", style, size));
        label.setForeground(color);

        return label;
    }

    private JTextField createTextField(String text) {
        JTextField field = new JTextField(text);

        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(WHITE);
        field.setCaretColor(PINK);
        field.setBackground(new Color(30, 36, 73));

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(82, 48, 70), 1
                        ),
                        new EmptyBorder(12, 13, 12, 13)
                )
        );

        field.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 46)
        );

        return field;
    }

    private JButton createButton(
            String text,
            Color background,
            Color foreground
    ) {
        JButton button = new JButton(text);

        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setForeground(foreground);
        button.setBackground(background);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        button.setBorder(
                new EmptyBorder(13, 17, 13, 17)
        );

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BURGUNDY_LIGHT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(background);
            }
        });

        return button;
    }

    private JButton createCellButton() {
        JButton button = new JButton();

        button.setFont(new Font("SansSerif", Font.BOLD, 48));
        button.setForeground(WHITE);
        button.setBackground(NAVY_CELL);

        button.setFocusPainted(false);
        button.setOpaque(true);

        button.setBorder(
                BorderFactory.createLineBorder(
                        new Color(100, 47, 70), 1
                )
        );

        button.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(
                            new Color(48, 38, 77)
                    );
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(NAVY_CELL);
            }
        });

        return button;
    }

    private JPanel roundedPanel(Color color, int radius) {

        JPanel panel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(color);

                g2.fillRoundRect(
                        0, 0,
                        getWidth(), getHeight(),
                        radius, radius
                );

                g2.dispose();

                super.paintComponent(g);
            }
        };

        panel.setOpaque(false);

        return panel;
    }

    private JPanel createScoreCard(String name, Color accent) {

        JPanel card = roundedPanel(NAVY_CARD, 18);

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setBorder(
                new EmptyBorder(13, 5, 13, 5)
        );

        JLabel nameLabel = makeLabel(
                name, 10, MUTED, Font.BOLD
        );

        JLabel scoreLabel = makeLabel(
                "0", 27, accent, Font.BOLD
        );

        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(nameLabel);
        card.add(Box.createVerticalStrut(7));
        card.add(scoreLabel);

        card.putClientProperty("nameLabel", nameLabel);
        card.putClientProperty("scoreLabel", scoreLabel);

        return card;
    }

    // =====================================================
    // GRADIENT BACKGROUND
    // =====================================================

    private class GradientPanel extends JPanel {

        GradientPanel() {
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY
            );

            GradientPaint gradient = new GradientPaint(
                    0, 0, NAVY,
                    getWidth(), getHeight(),
                    new Color(62, 8, 34)
            );

            g2.setPaint(gradient);
            g2.fillRect(
                    0, 0,
                    getWidth(), getHeight()
            );

            g2.setColor(
                    new Color(253, 205, 216, 13)
            );

            g2.fillOval(
                    getWidth() - 190,
                    -100,
                    300,
                    300
            );

            g2.dispose();
        }
    }

    // =====================================================
    // WINNING LINE
    // =====================================================

    private class WinningBoardPanel extends JPanel {

        @Override
        protected void paintChildren(Graphics g) {
            super.paintChildren(g);

            if (winningCells.size() != 3) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int firstIndex = winningCells.get(0);
            int lastIndex = winningCells.get(2);

            Component firstCell = getComponent(firstIndex);
            Component lastCell = getComponent(lastIndex);

            Point start = SwingUtilities.convertPoint(
                    firstCell,
                    firstCell.getWidth() / 2,
                    firstCell.getHeight() / 2,
                    this
            );

            Point end = SwingUtilities.convertPoint(
                    lastCell,
                    lastCell.getWidth() / 2,
                    lastCell.getHeight() / 2,
                    this
            );

            // Soft outer glow
            g2.setColor(new Color(253, 205, 216, 65));
            g2.setStroke(
                    new BasicStroke(
                            12,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            g2.drawLine(
                    start.x, start.y,
                    end.x, end.y
            );

            // Main winning line
            g2.setColor(PINK);
            g2.setStroke(
                    new BasicStroke(
                            5,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            g2.drawLine(
                    start.x, start.y,
                    end.x, end.y
            );

            g2.dispose();
        }
    }
}
