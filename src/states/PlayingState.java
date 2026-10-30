package states;

import core.GameConfig;
import core.GameStateManager;
import entities.Player;
import world.MuseumMap;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class PlayingState implements GameState {
    private final GameStateManager stateManager;
    private final Player player;
    private final MuseumMap museumMap;

    public PlayingState(GameStateManager stateManager) {
        this.stateManager = stateManager;
        player = new Player();
        museumMap = new MuseumMap();
    }

    @Override
    public void update() {
        player.update(museumMap);
    }

    @Override
    public void render(Graphics2D g2) {
        museumMap.render(g2);
        player.render(g2);

        g2.setColor(new Color(20, 20, 24, 220));
        g2.fillRect(48, 48, 250, 48);
        g2.setColor(new Color(231, 194, 111));
        g2.setFont(new Font("Monospaced", Font.BOLD, 16));
        g2.drawString("MUSEUM: MAIN HALL", 62, 78);

        g2.setColor(new Color(20, 20, 24, 220));
        g2.fillRect(48, GameConfig.HEIGHT - 82, 380, 34);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 13));
        g2.drawString("MOVE: WASD / ARROW KEYS     ESC: MENU", 60, GameConfig.HEIGHT - 60);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            stateManager.setState(new MenuState(stateManager));
            return;
        }
        player.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.keyReleased(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }
}
