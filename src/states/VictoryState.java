package states;

import core.GameConfig;
import core.GameStateManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class VictoryState implements GameState {
    private final GameStateManager stateManager;

    public VictoryState(GameStateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(15, 18, 22));
        g2.fillRect(0, 0, GameConfig.WIDTH, GameConfig.HEIGHT);

        g2.setColor(new Color(211, 174, 92));
        g2.fillRect(0, 0, GameConfig.WIDTH, 8);
        g2.fillRect(0, GameConfig.HEIGHT - 8, GameConfig.WIDTH, 8);

        g2.setFont(new Font("Monospaced", Font.BOLD, 48));
        String title = "HEIST COMPLETE!";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (GameConfig.WIDTH - titleWidth) / 2, 225);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 17));
        String message = "You escaped with the original painting.";
        int messageWidth = g2.getFontMetrics().stringWidth(message);
        g2.setColor(new Color(220, 220, 220));
        g2.drawString(message, (GameConfig.WIDTH - messageWidth) / 2, 285);

        g2.setFont(new Font("Monospaced", Font.BOLD, 16));
        String prompt = "PRESS ENTER TO RETURN TO MENU";
        int promptWidth = g2.getFontMetrics().stringWidth(prompt);
        g2.setColor(new Color(231, 194, 111));
        g2.drawString(prompt, (GameConfig.WIDTH - promptWidth) / 2, 365);
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
