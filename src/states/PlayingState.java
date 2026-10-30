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
    private static final int MISSION_TIME_FRAMES = 120 * GameConfig.TARGET_FPS;

    private final GameStateManager stateManager;
    private final Player player;
    private final Guard guard;
    private final MuseumMap museumMap;
    private final boolean[] copiedPaintings = new boolean[6];

    private int nearbyPainting = -1;
    private int activePainting = -1;
    private int copyProgress;
    private int stolenPainting = -1;
    private int fakeReplacedPainting = -1;
    private int timeRemainingFrames = MISSION_TIME_FRAMES;
    private int suspicion = 0;

    public PlayingState(GameStateManager stateManager) {
        this.stateManager = stateManager;
        player = new Player();
        guard = new Guard(520, 300);
        museumMap = new MuseumMap();
    }

    @Override
    public void update() {
        if (timeRemainingFrames > 0) {
            timeRemainingFrames--;
        }
        if (timeRemainingFrames <= 0) {
            stateManager.setState(new GameOverState(stateManager, true));
            return;
        }

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
                player.getY() + player.getSize() / 2,
                museumMap, fakeReplacedPainting);

        // Reaching the exit with the original ends the mission before suspicion is processed.
        if (stolenPainting != -1 && museumMap.isPlayerAtExit(
                player.getX(), player.getY(), player.getSize())) {
            stateManager.setState(new VictoryState(stateManager));
            return;
        }

        if (guard.hasCaughtPlayer(player.getX(), player.getY(), player.getSize())) {
            stateManager.setState(new GameOverState(stateManager));
            return;
        }

        if (guard.hasDiscoveredFake()) {
            suspicion = Math.min(100, suspicion + 2);
        } else if (guard.isChasing()) {
            suspicion = Math.min(100, suspicion + 1);
        } else {
            suspicion = Math.max(0, suspicion - 1);
        }

        if (suspicion >= 100) {
            stateManager.setState(new GameOverState(stateManager, false, true));
            return;
        }
    }

    @Override
    public void render(Graphics2D g2) {
        museumMap.render(g2, stolenPainting, fakeReplacedPainting);
        guard.render(g2);
        player.render(g2);

        g2.setColor(new Color(20, 20, 24, 220));
        g2.fillRect(48, 48, 250, 48);
        g2.setColor(new Color(231, 194, 111));
        g2.setFont(new Font("Monospaced", Font.BOLD, 16));
        g2.drawString("MUSEUM: MAIN HALL", 62, 78);

        g2.setColor(new Color(20, 20, 24, 220));
        g2.fillRect(48, GameConfig.HEIGHT - 82, 470, 34);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 13));
        g2.drawString("MOVE: WASD / ARROWS   E: COPY   F: STEAL   P: PAUSE",
                60, GameConfig.HEIGHT - 60);

        renderTimer(g2);
        renderSuspicion(g2);
        renderGuardStatus(g2);
        renderObjective(g2);
        renderInteractionUI(g2);
    }

    private void renderTimer(Graphics2D g2) {
        int totalSeconds = (timeRemainingFrames + GameConfig.TARGET_FPS - 1)
                / GameConfig.TARGET_FPS;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        String timeText = String.format("TIME: %02d:%02d", minutes, seconds);

        g2.setColor(new Color(20, 20, 24, 220));
        g2.fillRoundRect(48, 104, 180, 42, 8, 8);
        g2.setFont(new Font("Monospaced", Font.BOLD, 16));
        g2.setColor(totalSeconds <= 20 ? new Color(235, 95, 82)
                : new Color(245, 219, 154));
        g2.drawString(timeText, 64, 132);
    }

    private void renderSuspicion(Graphics2D g2) {
        g2.setColor(new Color(20, 20, 24, 220));
        g2.fillRoundRect(48, 154, 250, 48, 8, 8);
        g2.setFont(new Font("Monospaced", Font.BOLD, 12));
        g2.setColor(Color.WHITE);
        g2.drawString("SUSPICION", 62, 173);
        g2.setColor(new Color(65, 58, 48));
        g2.fillRect(62, 181, 220, 10);
        if (suspicion < 40) {
            g2.setColor(new Color(120, 180, 125));
        } else if (suspicion < 75) {
            g2.setColor(new Color(220, 170, 75));
        } else {
            g2.setColor(new Color(225, 80, 70));
        }
        g2.fillRect(62, 181, 220 * suspicion / 100, 10);
    }

    private void renderGuardStatus(Graphics2D g2) {
        g2.setColor(new Color(20, 20, 24, 220));
        g2.fillRoundRect(690, 48, 220, 42, 8, 8);
        g2.setFont(new Font("Monospaced", Font.BOLD, 13));
        if (guard.isChasing()) {
            g2.setColor(new Color(235, 95, 82));
            g2.drawString("GUARD ALERT! RUN!", 710, 74);
        } else if (guard.isInspecting()) {
            g2.setColor(new Color(245, 205, 90));
            g2.drawString("GUARD: CHECKING " + (guard.getInspectionIndex() + 1), 710, 74);
        } else {
            g2.setColor(new Color(190, 190, 190));
            g2.drawString("GUARD: PATROLLING", 710, 74);
        }
    }

    private void renderObjective(Graphics2D g2) {
        g2.setColor(new Color(20, 20, 24, 220));
        g2.fillRoundRect(690, 100, 220, 58, 8, 8);
        g2.setFont(new Font("Monospaced", Font.BOLD, 12));
        g2.setColor(new Color(231, 194, 111));
        if (stolenPainting == -1) {
            g2.drawString("OBJECTIVE:", 705, 123);
            g2.setColor(Color.WHITE);
            g2.drawString("COPY THEN STEAL", 705, 143);
        } else {
            g2.drawString("OBJECTIVE:", 705, 123);
            g2.setColor(new Color(145, 210, 150));
            g2.drawString("REACH THE EXIT", 705, 143);
        }
    }

    private void renderInteractionUI(Graphics2D g2) {
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
        } else if (nearbyPainting != -1 && stolenPainting == -1
                && copiedPaintings[nearbyPainting]) {
            g2.setColor(new Color(20, 20, 24, 220));
            g2.fillRoundRect(340, 500, 280, 42, 8, 8);
            g2.setColor(new Color(245, 219, 154));
            g2.setFont(new Font("Monospaced", Font.BOLD, 13));
            g2.drawString("PRESS F TO STEAL ORIGINAL", 355, 526);
        } else if (nearbyPainting != -1 && !copiedPaintings[nearbyPainting]) {
            g2.setColor(new Color(20, 20, 24, 220));
            g2.fillRoundRect(340, 500, 280, 42, 8, 8);
            g2.setColor(new Color(245, 219, 154));
            g2.setFont(new Font("Monospaced", Font.BOLD, 13));
            g2.drawString("PRESS E TO COPY PAINTING", 360, 526);
        } else if (stolenPainting != -1) {
            g2.setColor(new Color(20, 20, 24, 220));
            g2.fillRoundRect(340, 500, 280, 42, 8, 8);
            g2.setColor(new Color(145, 210, 150));
            g2.setFont(new Font("Monospaced", Font.BOLD, 13));
            g2.drawString("ORIGINAL SECURED! FIND EXIT", 355, 526);
        }
    }

    private void startCopying() {
        if (nearbyPainting != -1 && !copiedPaintings[nearbyPainting]
                && stolenPainting == -1 && activePainting == -1) {
            activePainting = nearbyPainting;
            copyProgress = 0;
        }
    }

    private void stealPainting() {
        if (nearbyPainting != -1 && copiedPaintings[nearbyPainting]
                && stolenPainting == -1 && activePainting == -1) {
            stolenPainting = nearbyPainting;
            fakeReplacedPainting = nearbyPainting;
        }
    }

    private void cancelCopying() {
        activePainting = -1;
        copyProgress = 0;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            stateManager.setState(new PauseState(stateManager, this));
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            stateManager.setState(new MenuState(stateManager));
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_E && activePainting == -1) {
            startCopying();
        }
        if (e.getKeyCode() == KeyEvent.VK_F) {
            stealPainting();
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
