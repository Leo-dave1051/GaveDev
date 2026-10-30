package world;

import core.GameConfig;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class MuseumMap {
    private static final int WALL_SIZE = 48;
    private static final int[] PAINTING_X = {150, 390, 630, 150, 390, 630};
    private static final int[] PAINTING_Y = {75, 75, 75, 470, 470, 470};
    private static final Color[] ARTWORK_COLORS = {
            new Color(77, 105, 125),
            new Color(133, 82, 65),
            new Color(88, 105, 73),
            new Color(112, 86, 127),
            new Color(172, 135, 73),
            new Color(74, 104, 105)
    };

    public boolean canOccupy(int x, int y, int width, int height) {
        Rectangle playerBounds = new Rectangle(x, y, width, height);
        if (x < WALL_SIZE || y < WALL_SIZE
                || x + width > GameConfig.WIDTH - WALL_SIZE
                || y + height > GameConfig.HEIGHT - WALL_SIZE) {
            return false;
        }
        for (int i = 0; i < PAINTING_X.length; i++) {
            if (intersectsPainting(playerBounds, PAINTING_X[i], PAINTING_Y[i])) {
                return false;
            }
        }
        return true;
    }

    public int getNearbyPaintingIndex(int playerX, int playerY, int playerSize) {
        int playerCenterX = playerX + playerSize / 2;
        int playerCenterY = playerY + playerSize / 2;
        int interactionRange = 82;
        for (int i = 0; i < PAINTING_X.length; i++) {
            int paintingCenterX = PAINTING_X[i] + 28;
            int paintingCenterY = PAINTING_Y[i] + 38;
            int dx = playerCenterX - paintingCenterX;
            int dy = playerCenterY - paintingCenterY;
            if (dx * dx + dy * dy <= interactionRange * interactionRange) {
                return i;
            }
        }
        return -1;
    }

    public int getPaintingCenterX(int index) {
        return PAINTING_X[index] + 28;
    }

    public int getPaintingCenterY(int index) {
        return PAINTING_Y[index] + 38;
    }

    public boolean isPlayerAtExit(int playerX, int playerY, int playerSize) {
        Rectangle player = new Rectangle(playerX, playerY, playerSize, playerSize);
        Rectangle exit = new Rectangle(840, 245, 38, 110);
        return player.intersects(exit);
    }

    private boolean intersectsPainting(Rectangle player, int x, int y) {
        Rectangle frame = new Rectangle(x - 5, y - 5, 66, 86);
        return player.intersects(frame);
    }

    public void render(Graphics2D g2) {
        render(g2, -1, -1);
    }

    public void render(Graphics2D g2, int stolenPainting, int fakePainting) {
        g2.setColor(new Color(24, 25, 31));
        g2.fillRect(0, 0, GameConfig.WIDTH, GameConfig.HEIGHT);
        g2.setColor(new Color(77, 65, 52));
        g2.fillRect(32, 32, GameConfig.WIDTH - 64, GameConfig.HEIGHT - 64);
        g2.setColor(new Color(112, 91, 65));
        g2.fillRect(48, 48, GameConfig.WIDTH - 96, GameConfig.HEIGHT - 96);

        g2.setColor(new Color(126, 103, 73));
        for (int x = 48; x < GameConfig.WIDTH - 48; x += 48) {
            for (int y = 48; y < GameConfig.HEIGHT - 48; y += 48) {
                if ((x / 48 + y / 48) % 2 == 0) {
                    g2.fillRect(x, y, 48, 48);
                }
            }
        }

        for (int i = 0; i < PAINTING_X.length; i++) {
            if (i == fakePainting) {
                drawFakePainting(g2, PAINTING_X[i], PAINTING_Y[i], ARTWORK_COLORS[i]);
            } else if (i == stolenPainting) {
                drawEmptyFrame(g2, PAINTING_X[i], PAINTING_Y[i]);
            } else {
                drawPainting(g2, PAINTING_X[i], PAINTING_Y[i], ARTWORK_COLORS[i]);
            }
        }

        g2.setColor(new Color(54, 39, 26));
        g2.fillRect(32, 32, GameConfig.WIDTH - 64, 16);
        g2.fillRect(32, GameConfig.HEIGHT - 48, GameConfig.WIDTH - 64, 16);
        g2.fillRect(32, 32, 16, GameConfig.HEIGHT - 64);
        g2.fillRect(GameConfig.WIDTH - 48, 32, 16, GameConfig.HEIGHT - 64);

        g2.setColor(new Color(211, 174, 92));
        g2.fillRect(850, 250, 18, 100);
        g2.setColor(new Color(245, 219, 154));
        g2.fillRect(854, 258, 10, 84);
        g2.setColor(new Color(45, 35, 25));
        g2.drawString("EXIT", 842, 240);
    }

    private void drawEmptyFrame(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(48, 32, 20));
        g2.fillRect(x - 5, y - 5, 66, 86);
        g2.setColor(new Color(211, 174, 92));
        g2.fillRect(x - 3, y - 3, 62, 82);
        g2.setColor(new Color(42, 37, 31));
        g2.fillRect(x + 3, y + 3, 50, 70);
        g2.setColor(new Color(22, 20, 19));
        g2.fillRect(x + 7, y + 7, 42, 62);
        g2.setColor(new Color(175, 145, 95));
        g2.drawLine(x + 9, y + 9, x + 47, y + 69);
        g2.drawLine(x + 47, y + 9, x + 9, y + 69);
    }

    private void drawFakePainting(Graphics2D g2, int x, int y, Color artwork) {
        drawPainting(g2, x, y, artwork);
        g2.setColor(new Color(225, 75, 65));
        g2.drawRect(x + 7, y + 7, 42, 62);
        g2.drawLine(x + 10, y + 10, x + 46, y + 66);
    }

    private void drawPainting(Graphics2D g2, int x, int y, Color artwork) {
        g2.setColor(new Color(48, 32, 20));
        g2.fillRect(x - 5, y - 5, 66, 86);
        g2.setColor(new Color(211, 174, 92));
        g2.fillRect(x - 3, y - 3, 62, 82);
        g2.setColor(new Color(42, 37, 31));
        g2.fillRect(x + 3, y + 3, 50, 70);
        g2.setColor(artwork);
        g2.fillRect(x + 7, y + 7, 42, 62);
        g2.setColor(new Color(224, 190, 123));
        g2.fillRect(x + 17, y + 17, 12, 12);
        g2.fillRect(x + 13, y + 42, 25, 5);
    }
}
