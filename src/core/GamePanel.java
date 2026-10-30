package core;

import states.MenuState;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GamePanel extends JPanel implements MouseListener {
    private final GameStateManager stateManager;
    private final Timer gameTimer;

    public GamePanel() {
        setPreferredSize(GameConfig.WINDOW_SIZE);
        setFocusable(true);
        setDoubleBuffered(true);
        addMouseListener(this);
        installKeyBindings();

        stateManager = new GameStateManager();
        stateManager.setState(new MenuState(stateManager));

        gameTimer = new Timer(1000 / GameConfig.TARGET_FPS, event -> {
            stateManager.update();
            repaint();
        });
        gameTimer.start();
    }

    private void installKeyBindings() {
        int[] keys = {
            KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D,
            KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
            KeyEvent.VK_E, KeyEvent.VK_F, KeyEvent.VK_P, KeyEvent.VK_ESCAPE,
            KeyEvent.VK_ENTER, KeyEvent.VK_R, KeyEvent.VK_M
        };

        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        for (int keyCode : keys) {
            String keyName = "key_" + keyCode;
            inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, false), keyName + "_pressed");
            inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, true), keyName + "_released");

            actionMap.put(keyName + "_pressed", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    dispatchKey(keyCode, KeyEvent.KEY_PRESSED);
                }
            });
            actionMap.put(keyName + "_released", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    dispatchKey(keyCode, KeyEvent.KEY_RELEASED);
                }
            });
        }
    }

    private void dispatchKey(int keyCode, int eventType) {
        if (stateManager.getCurrentState() == null) {
            return;
        }
        KeyEvent keyEvent = new KeyEvent(this, eventType, System.currentTimeMillis(),
                0, keyCode, KeyEvent.CHAR_UNDEFINED);
        if (eventType == KeyEvent.KEY_PRESSED) {
            stateManager.getCurrentState().keyPressed(keyEvent);
        } else {
            stateManager.getCurrentState().keyReleased(keyEvent);
        }
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
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (stateManager.getCurrentState() != null) {
            stateManager.getCurrentState().mousePressed(e);
        }
        requestFocusInWindow();
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