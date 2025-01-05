package io.github.TitouxJr.avarastGameManager;

import io.github.TitouxJr.avarastGameManager.commands.ExtractionCommand;
import io.github.TitouxJr.avarastGameManager.game.GameManager;
import io.github.TitouxJr.avarastGameManager.games.ExtractionGame;
import io.github.TitouxJr.avarastGameManager.listeners.BlockListener;
import io.github.TitouxJr.avarastGameManager.managers.BlockManager;
import io.github.TitouxJr.avarastGameManager.managers.BlockRespawnManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class AvarastGameManager extends JavaPlugin {
    private GameManager gameManager;
    private BlockManager blockManager;
    private BlockRespawnManager blockRespawnManager;
    private ExtractionGame extractionGame;

    @Override
    public void onEnable() {
        // Save default config
        saveDefaultConfig();

        // Initialize managers
        gameManager = new GameManager(this);
        blockManager = new BlockManager(this);
        blockRespawnManager = new BlockRespawnManager(this);
        extractionGame = new ExtractionGame(this);

        // Register listeners
        getServer().getPluginManager().registerEvents(
            new BlockListener(this, blockManager, blockRespawnManager), 
            this
        );
        getServer().getPluginManager().registerEvents(extractionGame, this);

        // Load breakable blocks from config
        blockManager.loadBreakableBlocks();

        // Register commands
        getCommand("extraction").setExecutor(new ExtractionCommand(this));

        getLogger().info("AvarastGameManager has been enabled!");
    }

    @Override
    public void onDisable() {
        // Cleanup
        if (gameManager != null) {
            gameManager.cleanup();
        }
        if (blockRespawnManager != null) {
            blockRespawnManager.respawnAllBlocks();
        }
        getLogger().info("AvarastGameManager has been disabled!");
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }

    public BlockRespawnManager getBlockRespawnManager() {
        return blockRespawnManager;
    }

    public ExtractionGame getExtractionGame() {
        return extractionGame;
    }
}
