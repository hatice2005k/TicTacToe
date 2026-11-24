
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    // Renkler
    private final Color BACKGROUND = new Color(25, 28, 38);
    private final Color CELL_COLOR = new Color(48, 53, 68);
    private final Color X_COLOR = new Color(70, 190, 180);
    private final Color O_COLOR = new Color(235, 115, 135);

    // Arayüz bileşenleri
    JButton[] cells = new JButton[9];

    JLabel statusLabel;
    JLabel xScoreLabel;
    JLabel oScoreLabel;

    JButton restartButton;

    public GamePanel() {

        setLayout(new BorderLayout(15, 15));
        setBackground(BACKGROUND);

        setBorder(
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        // Üst bölüm
        add(createHeader(), BorderLayout.NORTH);

        // Oyun tahtası
        add(createBoard(), BorderLayout.CENTER);

        // Alt bölüm
        add(createFooter(), BorderLayout.SOUTH);
    }

    // Başlık ve skor tablosu
    private JPanel createHeader() {

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BACKGROUND);

        JLabel title = new JLabel("TIC TAC TOE");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel scorePanel = new JPanel(new GridLayout(1, 2, 15, 0));
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

    // 3x3 oyun tahtası
    private JPanel createBoard() {

        JPanel board = new JPanel(new GridLayout(3, 3, 10, 10));
        board.setBackground(BACKGROUND);

        for (int i = 0; i < 9; i++) {

            JButton cell = new JButton("");

            cell.setFont(new Font("Arial", Font.BOLD, 55));
            cell.setForeground(Color.WHITE);
            cell.setBackground(CELL_COLOR);

            cell.setFocusPainted(false);
            cell.setBorderPainted(false);

            cells[i] = cell;

            board.add(cell);
        }

        return board;
    }

    // Alt bölüm
    private JPanel createFooter() {

        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
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

        footer.add(statusLabel);
        footer.add(Box.createVerticalStrut(12));
        footer.add(restartButton);

        return footer;
    }
}
