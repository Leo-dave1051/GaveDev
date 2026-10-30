package states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface GameState {
    void update();

    void render(Graphics2D g2);

    void keyPressed(KeyEvent e);

    void keyReleased(KeyEvent e);

    void mousePressed(MouseEvent e);
}
