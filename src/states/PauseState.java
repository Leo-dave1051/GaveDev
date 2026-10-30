package states;

import core.GameConfig;
import core.GameStateManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class PauseState implements GameState {
    private final GameStateManager stateManager;
    private final PlayingState playingState;

    public PauseState(GameStateManager stateManager, PlayingState playingState) {
        this.stateManager = stateManager;
        this.playingState = playingState;
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics2D g2) {
        playingState.render(g2);
        g2.setColor(new Color(8, 9, 13, 205));
        g2.fillRect(0, 0, GameConfig.WIDTH, GameConfig.HEIGHT);

        g2.setColor(new Color(231, 194, 111));
        g2.setFont(new Font("Monospaced", Font.BOLD, 48));
        String title = "PAUSED";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (GameConfig.WIDTH - titleWidth) / 2, 205);

        g2.setFont(new Font("Monospaced", Font.BOLD, 17));
        drawCentered(g2, "P / ENTER  -  RESUME", 285);
        drawCentered(g2, "R  -  RESTART MISSION", 330);
        drawCentered(g2, "M  -  MAIN MENU", 375);
    }

    private void drawCentered(Graphics2D g2, String text, int y) {
        int width = g2.getFontMetrics().stringWidth(text);
        g2.setColor(Color.WHITE);
        g2.drawString(text, (GameConfig.WIDTH - width) / 2, y);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_P, KeyEvent.VK_ENTER, KeyEvent.VK_ESCAPE ->
                    stateManager.setState(playingState);
            case KeyEvent.VK_R ->
                    stateManager.setState(new PlayingState(stateManager));
            case KeyEvent.VK_M ->
                    stateManager.setState(new MenuState(stateManager));
            default -> {
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        playingState.keyReleased(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }
}
