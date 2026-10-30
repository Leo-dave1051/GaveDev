import core.GameConfig;
import core.GamePanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame(GameConfig.TITLE);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);
            window.setContentPane(new GamePanel());
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }
}
