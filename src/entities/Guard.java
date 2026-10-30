package entities;

import java.awt.Color;
import java.awt.Graphics2D;

public class Guard {
    private static final int SIZE = 26;
    private static final double DETECTION_RANGE = 190.0;
    private static final double LOSE_RANGE = 270.0;
    private static final double PATROL_SPEED = 1.4;
    private static final double CHASE_SPEED = 2.1;

    private double x;
    private final double y;
    private double patrolDirection = 1;
    private boolean chasing;

    public Guard(int startX, int startY) {
        x = startX;
        y = startY;
    }

    public void update(int playerCenterX, int playerCenterY) {
        double dx = playerCenterX - (x + SIZE / 2.0);
        double dy = playerCenterY - (y + SIZE / 2.0);
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (!chasing && distance <= DETECTION_RANGE) {
            chasing = true;
        } else if (chasing && distance > LOSE_RANGE) {
            chasing = false;
        }

        if (chasing) {
            double length = Math.max(1.0, distance);
            x += (dx / length) * CHASE_SPEED;
        } else {
            x += patrolDirection * PATROL_SPEED;
            if (x <= 470) {
                x = 470;
                patrolDirection = 1;
            } else if (x >= 790) {
                x = 790;
                patrolDirection = -1;
            }
        }
    }

    public boolean hasCaughtPlayer(int playerX, int playerY, int playerSize) {
        double guardCenterX = x + SIZE / 2.0;
        double guardCenterY = y + SIZE / 2.0;
        double playerCenterX = playerX + playerSize / 2.0;
        double playerCenterY = playerY + playerSize / 2.0;
        double dx = guardCenterX - playerCenterX;
        double dy = guardCenterY - playerCenterY;
        return dx * dx + dy * dy <= 24.0 * 24.0;
    }

    public boolean isChasing() {
        return chasing;
    }

    public void render(Graphics2D g2) {
        int drawX = (int) Math.round(x);
        int drawY = (int) Math.round(y);

        g2.setColor(new Color(25, 18, 18));
        g2.fillRect(drawX + 3, drawY + SIZE - 3, SIZE - 6, 5);
        g2.setColor(new Color(100, 45, 43));
        g2.fillRect(drawX + 3, drawY + 8, SIZE - 6, SIZE - 8);
        g2.setColor(new Color(45, 42, 43));
        g2.fillRect(drawX + 4, drawY + 2, SIZE - 8, 9);
        g2.setColor(new Color(220, 177, 137));
        g2.fillRect(drawX + 7, drawY + 5, SIZE - 14, 8);
        g2.setColor(new Color(25, 20, 20));
        g2.fillRect(drawX + 8, drawY + 8, 3, 2);
        g2.fillRect(drawX + 15, drawY + 8, 3, 2);

        if (chasing) {
            g2.setColor(new Color(235, 75, 65));
            g2.fillOval(drawX + 8, drawY - 10, 10, 10);
        }
    }
}
