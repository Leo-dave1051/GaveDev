package entities;

import core.GameConfig;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Player {
    private int x = 110;
    private int y = 280;
    private final int size = 24;
    private final int speed = 4;

    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    public void update() {
        int nextX = x;
        int nextY = y;

        if (up) nextY -= speed;
        if (down) nextY += speed;
        if (left) nextX -= speed;
        if (right) nextX += speed;

        x = Math.max(48, Math.min(GameConfig.WIDTH - 48 - size, nextX));
        y = Math.max(48, Math.min(GameConfig.HEIGHT - 48 - size, nextY));
    }

    public void render(Graphics2D g2) {
        g2.setColor(new Color(25, 20, 18));
        g2.fillRect(x + 3, y + size - 3, size - 6, 5);
        g2.setColor(new Color(63, 105, 145));
        g2.fillRect(x + 3, y + 7, size - 6, size - 7);
        g2.setColor(new Color(222, 177, 135));
        g2.fillRect(x + 5, y, size - 10, 10);
        g2.setColor(new Color(35, 28, 26));
        g2.fillRect(x + 5, y, size - 10, 3);
    }

    public void keyPressed(KeyEvent e) {
        setKey(e.getKeyCode(), true);
    }

    public void keyReleased(KeyEvent e) {
        setKey(e.getKeyCode(), false);
    }

    private void setKey(int keyCode, boolean pressed) {
        switch (keyCode) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> up = pressed;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> down = pressed;
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> left = pressed;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> right = pressed;
            default -> {
            }
        }
    }
}
