package core;

import states.GameState;

public class GameStateManager {
    private GameState currentState;

    public void setState(GameState state) {
        currentState = state;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void update() {
        if (currentState != null) {
            currentState.update();
        }
    }
}
