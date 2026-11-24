
import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {

        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 720);
        setMinimumSize(new java.awt.Dimension(480, 650));
        setLocationRelativeTo(null);
        setResizable(false);

        setContentPane(new GamePanel());
    }
}
