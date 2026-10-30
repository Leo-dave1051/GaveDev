package entities;

import java.awt.Color;
import java.awt.Graphics2D;

public class Guard {
    private static final int SIZE = 26;
    private static final double DETECTION_RANGE = 190.0;
    private static final double LOSE_RANGE = 270.0;
    private static final double PATROL_SPEED = 1.4;
    private static final double CHASE_SPEED = 2.1;
    private static final double INSPECT_SPEED = 1.65;
    private static final int INSPECTION_INTERVAL = 6 * 60;
    private static final int INSPECTION_DURATION = 90;

    private double x;
    private double y;
    private double patrolDirection = 1;
    private boolean chasing;
    private boolean inspecting;
    private boolean fakeDiscovered;
    private int inspectionCooldown = INSPECTION_INTERVAL;
    private int inspectionTimer;
    private double inspectionTargetX;
    private double inspectionTargetY;

    public Guard(int startX, int startY) {
        x = startX;
        y = startY;
    }

    public void update(int playerCenterX, int playerCenterY,
                       boolean fakePresent, int fakeCenterX, int fakeCenterY) {
        double dx = playerCenterX - (x + SIZE / 2.0);
        double dy = playerCenterY - (y + SIZE / 2.0);
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (!chasing && distance <= DETECTION_RANGE) {
            chasing = true;
            inspecting = false;
        } else if (chasing && distance > LOSE_RANGE && !fakeDiscovered) {
            chasing = false;
        }

        if (chasing) {
            moveToward(playerCenterX, playerCenterY, CHASE_SPEED);
            return;
        }

        if (fakeDiscovered) {
            chasing = true;
            moveToward(playerCenterX, playerCenterY, CHASE_SPEED);
            return;
        }

        if (inspecting) {
            double targetDx = inspectionTargetX - (x + SIZE / 2.0);
            double targetDy = inspectionTargetY - (y + SIZE / 2.0);
            double targetDistance = Math.sqrt(targetDx * targetDx + targetDy * targetDy);
            if (targetDistance > 18) {
                moveToward(inspectionTargetX, inspectionTargetY, INSPECT_SPEED);
            } else {
                inspectionTimer++;
                if (inspectionTimer >= INSPECTION_DURATION) {
                    inspecting = false;
                    fakeDiscovered = true;
                    chasing = true;
                }
            }
            return;
        }

        if (fakePresent) {
            inspectionCooldown--;
            if (inspectionCooldown <= 0) {
                inspecting = true;
                inspectionTimer = 0;
                inspectionTargetX = fakeCenterX;
                inspectionTargetY = fakeCenterY;
                inspectionCooldown = INSPECTION_INTERVAL;
                return;
            }
        } else {
            inspectionCooldown = INSPECTION_INTERVAL;
        }

        x += patrolDirection * PATROL_SPEED;
        if (x <= 470) {
            x = 470;
            patrolDirection = 1;
        } else if (x >= 790) {
            x = 790;
            patrolDirection = -1;
        }
    }

    private void moveToward(double targetX, double targetY, double speed) {
        double dx = targetX - (x + SIZE / 2.0);
        double dy = targetY - (y + SIZE / 2.0);
        double length = Math.max(1.0, Math.sqrt(dx * dx + dy * dy));
        x += (dx / length) * speed;
        y += (dy / length) * speed;
        x = Math.max(48, Math.min(960 - 48 - SIZE, x));
        y = Math.max(48, Math.min(600 - 48 - SIZE, y));
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

    public boolean isInspecting() {
        return inspecting;
    }

    public boolean hasDiscoveredFake() {
        return fakeDiscovered;
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
        } else if (inspecting) {
            g2.setColor(new Color(245, 205, 90));
            g2.fillOval(drawX + 8, drawY - 10, 10, 10);
        }
    }
}
