package io.github.TitouxJr.avarastGameManager.commands;

import io.github.TitouxJr.avarastGameManager.AvarastGameManager;
import io.github.TitouxJr.avarastGameManager.game.GameState;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExtractionCommand implements CommandExecutor {
    private final AvarastGameManager plugin;

    public ExtractionCommand(AvarastGameManager plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            if (plugin.getGameManager().getCurrentState() != GameState.LOBBY) {
                player.sendMessage(ChatColor.RED + "A game is already in progress!");
                return true;
            }
            
            // Start the game
            plugin.getExtractionGame().startGame();
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("stop")) {
            if (plugin.getGameManager().getCurrentState() == GameState.LOBBY) {
                player.sendMessage(ChatColor.RED + "No game is currently running!");
                return true;
            }
            
            // Stop the game
            plugin.getExtractionGame().endGame();
            player.sendMessage(ChatColor.GREEN + "Game has been forcefully stopped!");
            return true;
        }

        return false;
    }
} 