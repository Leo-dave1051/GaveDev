package core;

import java.awt.Dimension;

public final class GameConfig {
    public static final String TITLE = "PaintHeist";
    public static final int WIDTH = 960;
    public static final int HEIGHT = 600;
    public static final int TARGET_FPS = 60;
    public static final Dimension WINDOW_SIZE = new Dimension(WIDTH, HEIGHT);

    private GameConfig() {
    }
}
