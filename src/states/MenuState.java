package states;

import core.GameConfig;
import core.GameStateManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MenuState implements GameState {
    private final GameStateManager stateManager;
    private final int buttonX = 380;
    private final int buttonY = 350;
    private final int buttonWidth = 200;
    private final int buttonHeight = 58;

    public MenuState(GameStateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(18, 20, 29));
        g2.fillRect(0, 0, GameConfig.WIDTH, GameConfig.HEIGHT);

        g2.setColor(new Color(37, 39, 49));
        for (int x = 0; x < GameConfig.WIDTH; x += 48) {
            g2.drawLine(x, 0, x, GameConfig.HEIGHT);
        }
        for (int y = 0; y < GameConfig.HEIGHT; y += 48) {
            g2.drawLine(0, y, GameConfig.WIDTH, y);
        }

        g2.setColor(new Color(211, 174, 92));
        g2.fillRect(0, 0, GameConfig.WIDTH, 8);
        g2.fillRect(0, GameConfig.HEIGHT - 8, GameConfig.WIDTH, 8);

        g2.setFont(new Font("Monospaced", Font.BOLD, 58));
        String title = "PAINTHEIST";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.setColor(new Color(0, 0, 0, 120));
        g2.drawString(title, (GameConfig.WIDTH - titleWidth) / 2 + 4, 222 + 4);
        g2.setColor(new Color(231, 194, 111));
        g2.drawString(title, (GameConfig.WIDTH - titleWidth) / 2, 222);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 16));
        String subtitle = "THE ART OF THE HEIST";
        int subtitleWidth = g2.getFontMetrics().stringWidth(subtitle);
        g2.setColor(new Color(190, 190, 190));
        g2.drawString(subtitle, (GameConfig.WIDTH - subtitleWidth) / 2, 260);

        g2.setColor(new Color(211, 174, 92));
        g2.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
        g2.setColor(new Color(70, 52, 29));
        g2.drawRect(buttonX, buttonY, buttonWidth, buttonHeight);
        g2.setFont(new Font("Monospaced", Font.BOLD, 22));
        String play = "PLAY";
        int playWidth = g2.getFontMetrics().stringWidth(play);
        g2.setColor(new Color(28, 27, 25));
        g2.drawString(play, buttonX + (buttonWidth - playWidth) / 2, buttonY + 37);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 13));
        g2.setColor(new Color(150, 150, 155));
        String hint = "CLICK PLAY OR PRESS ENTER";
        int hintWidth = g2.getFontMetrics().stringWidth(hint);
        g2.drawString(hint, (GameConfig.WIDTH - hintWidth) / 2, 445);
    }

    private void startGame() {
        stateManager.setState(new PlayingState(stateManager));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            startGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX() >= buttonX && e.getX() <= buttonX + buttonWidth
                && e.getY() >= buttonY && e.getY() <= buttonY + buttonHeight) {
            startGame();
        }
    }
}
