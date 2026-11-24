
import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {

        setTitle("Tic Tac Toe");
        setSize(500, 650);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);
        setResizable(false);

        // Oyun arayüzünü pencereye ekle
        GamePanel gamePanel = new GamePanel();

        add(gamePanel);
    }
}
