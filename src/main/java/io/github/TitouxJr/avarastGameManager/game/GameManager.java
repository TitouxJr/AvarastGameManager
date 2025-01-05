package io.github.TitouxJr.avarastGameManager.game;

import io.github.TitouxJr.avarastGameManager.AvarastGameManager;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class GameManager implements Listener {
    private final AvarastGameManager plugin;
    private GameState currentState;

    public GameManager(AvarastGameManager plugin) {
        this.plugin = plugin;
        this.currentState = GameState.LOBBY; // Default state
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setGameState(GameState newState) {
        // Don't change if it's the same state
        if (currentState == newState) return;

        // Update the state
        GameState oldState = currentState;
        currentState = newState;

        // Call state change event
        Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(oldState, newState));

        // Handle state specific logic
        handleStateChange(oldState, newState);
    }

    private void handleStateChange(GameState oldState, GameState newState) {
        switch (newState) {
            case LOBBY:
                // Reset game elements for lobby state
                break;
            case STARTING:
                // Initialize game start sequence
                break;
            case ACTIVE:
                // Start the actual game
                break;
            case ENDING:
                // Clean up and prepare for game end
                break;
        }
    }

    public void cleanup() {
        HandlerList.unregisterAll(this);
    }
} 