
import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        setTitle("Tic Tac Toe | Premium Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(560, 760);
        setMinimumSize(new Dimension(500, 680));
        setLocationRelativeTo(null);
        setResizable(false);

        setContentPane(new GamePanel());
    }
}
