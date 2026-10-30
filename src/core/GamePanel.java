package core;

import states.MenuState;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GamePanel extends JPanel implements KeyListener, MouseListener {
    private final GameStateManager stateManager;
    private final Timer gameTimer;

    public GamePanel() {
        setPreferredSize(GameConfig.WINDOW_SIZE);
        setFocusable(true);
        setDoubleBuffered(true);
        addKeyListener(this);
        addMouseListener(this);

        stateManager = new GameStateManager();
        stateManager.setState(new MenuState(stateManager));

        gameTimer = new Timer(1000 / GameConfig.TARGET_FPS, event -> {
            stateManager.update();
            repaint();
        });
        gameTimer.start();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        if (stateManager.getCurrentState() != null) {
            stateManager.getCurrentState().render(g2);
        }

        g2.dispose();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (stateManager.getCurrentState() != null) {
            stateManager.getCurrentState().keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (stateManager.getCurrentState() != null) {
            stateManager.getCurrentState().keyReleased(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        requestFocusInWindow();
        if (stateManager.getCurrentState() != null) {
            stateManager.getCurrentState().mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}