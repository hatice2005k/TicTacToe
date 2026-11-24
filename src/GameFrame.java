
import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GameFrame() {

        // Pencere başlığı
        setTitle("Tic Tac Toe");

        // Pencere boyutu
        setSize(500, 650);

        // Kapatma işlemi
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Pencereyi ekranın ortasına yerleştir
        setLocationRelativeTo(null);

        // Pencerenin boyutunu sabitle
        setResizable(false);

        // Ana panel
        setLayout(new BorderLayout());

        // Arka plan rengi
        getContentPane().setBackground(new Color(25, 28, 38));

    }
}
