package entities;

import world.MuseumMap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Player {
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private int x = 110;
    private int y = 280;
    private final int size = 24;
    private final int speed = 4;

    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean moving;
    private Direction facing = Direction.DOWN;

    public void update(MuseumMap map) {
        int moveX = 0;
        int moveY = 0;

        if (up) moveY--;
        if (down) moveY++;
        if (left) moveX--;
        if (right) moveX++;

        moving = moveX != 0 || moveY != 0;

        if (moving) {
            if (moveX != 0 && moveY != 0) {
                moveX *= 3;
                moveY *= 3;
            } else {
                moveX *= speed;
                moveY *= speed;
            }

            if (Math.abs(moveX) > Math.abs(moveY)) {
                facing = moveX < 0 ? Direction.LEFT : Direction.RIGHT;
            } else if (moveY != 0) {
                facing = moveY < 0 ? Direction.UP : Direction.DOWN;
            }
        }

        int nextX = x + moveX;
        if (map.canOccupy(nextX, y, size, size)) {
            x = nextX;
        }

        int nextY = y + moveY;
        if (map.canOccupy(x, nextY, size, size)) {
            y = nextY;
        }
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

        g2.setColor(new Color(25, 20, 18));
        switch (facing) {
            case UP -> g2.fillRect(x + 9, y + 2, 2, 2);
            case DOWN -> g2.fillRect(x + 9, y + 7, 2, 2);
            case LEFT -> g2.fillRect(x + 6, y + 5, 2, 2);
            case RIGHT -> g2.fillRect(x + 14, y + 5, 2, 2);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public boolean isMoving() {
        return moving;
    }

    public Direction getFacing() {
        return facing;
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
