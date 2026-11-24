
import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Tic Tac Toe");

            frame.setSize(500, 650);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            // Ana panel
            JPanel mainPanel = new JPanel();
            mainPanel.setBackground(new Color(25, 28, 38));
            mainPanel.setLayout(new BorderLayout(15, 15));
            mainPanel.setBorder(
                    BorderFactory.createEmptyBorder(25, 25, 25, 25)
            );

            // Başlık
            JLabel title = new JLabel("TIC TAC TOE");
            title.setFont(new Font("Arial", Font.BOLD, 30));
            title.setForeground(Color.WHITE);
            title.setHorizontalAlignment(SwingConstants.CENTER);

            mainPanel.add(title, BorderLayout.NORTH);

            // Oyun tahtası
            JPanel boardPanel = new JPanel();
            boardPanel.setLayout(new GridLayout(3, 3, 10, 10));
            boardPanel.setBackground(new Color(25, 28, 38));

            for (int i = 0; i < 9; i++) {

                JButton button = new JButton("");

                button.setFont(new Font("Arial", Font.BOLD, 50));
                button.setBackground(new Color(48, 53, 68));
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                button.setBorderPainted(false);

                boardPanel.add(button);
            }

            mainPanel.add(boardPanel, BorderLayout.CENTER);

            // Alt bilgi
            JLabel status = new JLabel("Player X's Turn");

            status.setFont(new Font("Arial", Font.BOLD, 18));
            status.setForeground(Color.WHITE);
            status.setHorizontalAlignment(SwingConstants.CENTER);

            mainPanel.add(status, BorderLayout.SOUTH);

            frame.add(mainPanel);
            frame.setVisible(true);

        });
    }
}
