package io.github.TitouxJr.avarastGameManager.games;

import io.github.TitouxJr.avarastGameManager.AvarastGameManager;
import io.github.TitouxJr.avarastGameManager.game.GameState;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ExtractionGame implements Listener {
    private final AvarastGameManager plugin;
    private BossBar timeBar;
    private BukkitTask gameTimer;
    private BukkitTask countdownTask;
    private int remainingSeconds;

    public ExtractionGame(AvarastGameManager plugin) {
        this.plugin = plugin;
        this.timeBar = Bukkit.createBossBar(
            ChatColor.GOLD + "Time Remaining",
            BarColor.GREEN,
            BarStyle.SOLID
        );
    }

    public void startGame() {
        if (plugin.getGameManager().getCurrentState() != GameState.LOBBY) {
            return;
        }

        // Set game state to STARTING
        plugin.getGameManager().setGameState(GameState.STARTING);

        // Start countdown
        int countdownSeconds = plugin.getConfig().getInt("countdown-seconds", 10);
        startCountdown(countdownSeconds);
    }

    private void startCountdown(int seconds) {
        countdownTask = new BukkitRunnable() {
            int count = seconds;

            @Override
            public void run() {
                if (count > 0) {
                    // Display countdown title to all players
                    Bukkit.getOnlinePlayers().forEach(player -> 
                        player.sendTitle(
                            ChatColor.GOLD + "Starting in " + ChatColor.RED + count,
                            ChatColor.YELLOW + "Get ready!",
                            5, 20, 5
                        )
                    );
                    count--;
                } else {
                    startMainGame();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void startMainGame() {
        // Configure breakable blocks for the game
        plugin.getBlockManager().clearBreakableBlocks();
        plugin.getBlockManager().loadBreakableBlocks(); // Load blocks from config.yml
        plugin.getBlockManager().setAllowBlockBreaking(true);

        // Set game state to ACTIVE
        plugin.getGameManager().setGameState(GameState.ACTIVE);

        // Broadcast start message
        Bukkit.broadcastMessage(ChatColor.GREEN + "Extraction has begun!");

        // Start the game timer
        remainingSeconds = plugin.getConfig().getInt("game-duration-minutes", 30) * 60;
        startGameTimer();

        // Show boss bar to all players
        Bukkit.getOnlinePlayers().forEach(timeBar::addPlayer);
    }

    private void startGameTimer() {
        gameTimer = new BukkitRunnable() {
            @Override
            public void run() {
                if (remainingSeconds <= 0) {
                    endGame();
                    cancel();
                    return;
                }

                updateTimeDisplay();
                remainingSeconds--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void updateTimeDisplay() {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);
        
        // Update boss bar
        timeBar.setTitle(ChatColor.GOLD + "Time Remaining: " + ChatColor.GREEN + timeString);
        timeBar.setProgress(Math.max(0.0, (double) remainingSeconds / (plugin.getConfig().getInt("game-duration-minutes", 30) * 60)));

        // Update action bar for all players
        String actionBarText = ChatColor.GOLD + "⏰ " + ChatColor.GREEN + timeString + ChatColor.GOLD + " remaining";
        Bukkit.getOnlinePlayers().forEach(player ->
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(actionBarText))
        );
    }

    public void endGame() {
        plugin.getGameManager().setGameState(GameState.ENDING);
        
        // Cancel all tasks
        if (gameTimer != null) gameTimer.cancel();
        if (countdownTask != null) countdownTask.cancel();
        
        // Hide boss bar
        timeBar.removeAll();
        
        // Reset game settings
        plugin.getBlockManager().setAllowBlockBreaking(false);
        plugin.getBlockManager().clearBreakableBlocks();

        // Respawn all broken blocks
        plugin.getBlockRespawnManager().respawnAllBlocks();
        
        // Broadcast end message
        Bukkit.broadcastMessage(ChatColor.RED + "Extraction has ended!");
        
        // Return to lobby state
        plugin.getGameManager().setGameState(GameState.LOBBY);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getGameManager().getCurrentState() == GameState.ACTIVE) {
            timeBar.addPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        timeBar.removePlayer(event.getPlayer());
    }
} 