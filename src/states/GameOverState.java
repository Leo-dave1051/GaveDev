package states;

import core.GameConfig;
import core.GameStateManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameOverState implements GameState {
    private final GameStateManager stateManager;
    private final boolean timeExpired;
    private final boolean securityAlert;

    public GameOverState(GameStateManager stateManager) {
        this(stateManager, false);
    }

    public GameOverState(GameStateManager stateManager, boolean timeExpired) {
        this(stateManager, timeExpired, false);
    }

    public GameOverState(GameStateManager stateManager, boolean timeExpired, boolean securityAlert) {
        this.stateManager = stateManager;
        this.timeExpired = timeExpired;
        this.securityAlert = securityAlert;
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(15, 16, 22));
        g2.fillRect(0, 0, GameConfig.WIDTH, GameConfig.HEIGHT);

        g2.setFont(new Font("Monospaced", Font.BOLD, 54));
        String title = securityAlert ? "ALARM RAISED!" : timeExpired ? "TIME'S UP!" : "CAUGHT!";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.setColor(new Color(220, 75, 65));
        g2.drawString(title, (GameConfig.WIDTH - titleWidth) / 2, 235);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 17));
        String message = securityAlert
                ? "Security noticed you for too long."
                : timeExpired
                ? "You ran out of time before escaping."
                : "The museum guard caught you.";
        int messageWidth = g2.getFontMetrics().stringWidth(message);
        g2.setColor(new Color(215, 215, 215));
        g2.drawString(message, (GameConfig.WIDTH - messageWidth) / 2, 290);

        g2.setFont(new Font("Monospaced", Font.BOLD, 16));
        String restart = "PRESS ENTER TO RETURN TO MENU";
        int restartWidth = g2.getFontMetrics().stringWidth(restart);
        g2.setColor(new Color(231, 194, 111));
        g2.drawString(restart, (GameConfig.WIDTH - restartWidth) / 2, 370);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            stateManager.setState(new MenuState(stateManager));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }
}
