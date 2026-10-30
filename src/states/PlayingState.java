package states;

import core.GameConfig;
import core.GameStateManager;
import entities.Guard;
import entities.Player;
import world.MuseumMap;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class PlayingState implements GameState {
    private static final int COPY_DURATION_FRAMES = 180;

    private final GameStateManager stateManager;
    private final Player player;
    private final Guard guard;
    private final MuseumMap museumMap;
    private final boolean[] copiedPaintings = new boolean[6];

    private int nearbyPainting = -1;
    private int activePainting = -1;
    private int copyProgress;

    public PlayingState(GameStateManager stateManager) {
        this.stateManager = stateManager;
        player = new Player();
        guard = new Guard(520, 300);
        museumMap = new MuseumMap();
    }

    @Override
    public void update() {
        player.update(museumMap);
        nearbyPainting = museumMap.getNearbyPaintingIndex(
                player.getX(), player.getY(), player.getSize());

        if (activePainting != -1) {
            if (player.isMoving() || nearbyPainting != activePainting) {
                cancelCopying();
            } else {
                copyProgress++;
                if (copyProgress >= COPY_DURATION_FRAMES) {
                    copiedPaintings[activePainting] = true;
                    cancelCopying();
                }
            }
        }

        guard.update(player.getX() + player.getSize() / 2,
                player.getY() + player.getSize() / 2);

        if (guard.hasCaughtPlayer(player.getX(), player.getY(), player.getSize())) {
            stateManager.setState(new GameOverState(stateManager));
        }
    }

    @Override
    public void render(Graphics2D g2) {
        museumMap.render(g2);
        guard.render(g2);
        player.render(g2);

        g2.setColor(new Color(20, 20, 24, 220));
        g2.fillRect(48, 48, 250, 48);
        g2.setColor(new Color(231, 194, 111));
        g2.setFont(new Font("Monospaced", Font.BOLD, 16));
        g2.drawString("MUSEUM: MAIN HALL", 62, 78);

        g2.setColor(new Color(20, 20, 24, 220));
        g2.fillRect(48, GameConfig.HEIGHT - 82, 420, 34);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 13));
        g2.drawString("MOVE: WASD / ARROWS   E: COPY   ESC: MENU",
                60, GameConfig.HEIGHT - 60);

        renderGuardStatus(g2);
        renderCopyingUI(g2);
    }

    private void renderGuardStatus(Graphics2D g2) {
        g2.setColor(new Color(20, 20, 24, 220));
        g2.fillRoundRect(690, 48, 220, 42, 8, 8);
        g2.setFont(new Font("Monospaced", Font.BOLD, 13));
        if (guard.isChasing()) {
            g2.setColor(new Color(235, 95, 82));
            g2.drawString("GUARD ALERT! RUN!", 710, 74);
        } else {
            g2.setColor(new Color(190, 190, 190));
            g2.drawString("GUARD: PATROLLING", 710, 74);
        }
    }

    private void renderCopyingUI(Graphics2D g2) {
        if (activePainting != -1) {
            int percent = Math.min(100, copyProgress * 100 / COPY_DURATION_FRAMES);
            g2.setColor(new Color(20, 20, 24, 230));
            g2.fillRoundRect(350, 485, 260, 66, 8, 8);
            g2.setColor(new Color(231, 194, 111));
            g2.setFont(new Font("Monospaced", Font.BOLD, 14));
            g2.drawString("COPYING PAINTING...", 370, 507);
            g2.setColor(new Color(65, 58, 48));
            g2.fillRect(370, 518, 220, 12);
            g2.setColor(new Color(231, 194, 111));
            g2.fillRect(370, 518, 220 * percent / 100, 12);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Monospaced", Font.PLAIN, 11));
            g2.drawString(percent + "%", 555, 510);
        } else if (nearbyPainting != -1 && !copiedPaintings[nearbyPainting]) {
            g2.setColor(new Color(20, 20, 24, 220));
            g2.fillRoundRect(340, 500, 280, 42, 8, 8);
            g2.setColor(new Color(245, 219, 154));
            g2.setFont(new Font("Monospaced", Font.BOLD, 13));
            g2.drawString("PRESS E TO COPY PAINTING", 360, 526);
        } else if (nearbyPainting != -1) {
            g2.setColor(new Color(20, 20, 24, 220));
            g2.fillRoundRect(370, 500, 220, 42, 8, 8);
            g2.setColor(new Color(145, 210, 150));
            g2.setFont(new Font("Monospaced", Font.BOLD, 13));
            g2.drawString("PAINTING COPIED", 393, 526);
        }
    }

    private void startCopying() {
        if (nearbyPainting != -1 && !copiedPaintings[nearbyPainting]) {
            activePainting = nearbyPainting;
            copyProgress = 0;
        }
    }

    private void cancelCopying() {
        activePainting = -1;
        copyProgress = 0;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            stateManager.setState(new MenuState(stateManager));
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_E && activePainting == -1) {
            startCopying();
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
