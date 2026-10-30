package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import world.MuseumMap;

public class Guard {
    private static final int SIZE = 26;
    private static final double DETECTION_RANGE = 190.0;
    private static final double LOSE_RANGE = 270.0;
    private static final double CHASE_SPEED = 2.1;
    private static final double INSPECT_SPEED = 1.7;
    private static final int INSPECTION_DURATION = 75;

    private double x;
    private double y;
    private boolean chasing;
    private boolean inspecting;
    private boolean fakeDiscovered;
    private int inspectionIndex;
    private int inspectionTimer;
    private boolean movingToInspection = true;
    private boolean fakeInvestigationStarted;

    public Guard(int startX, int startY) {
        x = startX;
        y = startY;
    }

    public void update(int playerCenterX, int playerCenterY,
                       MuseumMap map, int fakePaintingIndex) {
        double dx = playerCenterX - (x + SIZE / 2.0);
        double dy = playerCenterY - (y + SIZE / 2.0);
        double distance = Math.sqrt(dx * dx + dy * dy);

        boolean investigatingFake = fakePaintingIndex >= 0 && !fakeDiscovered;
        if (investigatingFake && !fakeInvestigationStarted) {
            fakeInvestigationStarted = true;
            chasing = false;
            inspecting = false;
            movingToInspection = true;
            inspectionTimer = 0;
        }

        // Once a fake has been planted, finish the inspection route instead of
        // abandoning it to chase the player. The guard will chase after discovery.
        if (!investigatingFake && !fakeDiscovered && distance <= DETECTION_RANGE) {
            chasing = true;
            inspecting = false;
        } else if (!investigatingFake && chasing && !fakeDiscovered
                && distance > LOSE_RANGE) {
            chasing = false;
        }

        if ((chasing && !investigatingFake) || fakeDiscovered) {
            chasing = true;
            moveToward(playerCenterX, playerCenterY, CHASE_SPEED, map);
            return;
        }

        int targetX = map.getInspectionPointX(inspectionIndex);
        int targetY = map.getInspectionPointY(inspectionIndex);

        if (movingToInspection) {
            inspecting = false;
            double targetDx = targetX - (x + SIZE / 2.0);
            double targetDy = targetY - (y + SIZE / 2.0);
            double targetDistance = Math.sqrt(targetDx * targetDx + targetDy * targetDy);

            if (targetDistance > 5) {
                moveToward(targetX, targetY, INSPECT_SPEED, map);
            } else {
                movingToInspection = false;
                inspecting = true;
                inspectionTimer = 0;
            }
            return;
        }

        inspecting = true;
        inspectionTimer++;

        if (inspectionTimer >= INSPECTION_DURATION) {
            if (fakePaintingIndex == inspectionIndex) {
                fakeDiscovered = true;
                chasing = true;
                inspecting = false;
                return;
            }

            inspectionIndex = (inspectionIndex + 1) % map.getPaintingCount();
            movingToInspection = true;
            inspecting = false;
        }
    }

    private void moveToward(double targetX, double targetY, double speed, MuseumMap map) {
        double dx = targetX - (x + SIZE / 2.0);
        double dy = targetY - (y + SIZE / 2.0);
        double length = Math.max(1.0, Math.sqrt(dx * dx + dy * dy));
        double stepX = (dx / length) * speed;
        double stepY = (dy / length) * speed;

        int nextX = (int) Math.round(x + stepX);
        int nextY = (int) Math.round(y + stepY);

        if (map.canOccupy(nextX, (int) Math.round(y), SIZE, SIZE)) {
            x += stepX;
        }
        if (map.canOccupy((int) Math.round(x), nextY, SIZE, SIZE)) {
            y += stepY;
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

    public boolean isInspecting() {
        return inspecting;
    }

    public boolean hasDiscoveredFake() {
        return fakeDiscovered;
    }

    public int getInspectionIndex() {
        return inspectionIndex;
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