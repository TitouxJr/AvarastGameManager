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
            sender.sendMessage(ChatColor.RED + "Cette commande ne peut être utilisée que par des joueurs !");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Utilisation : /extraction <start|stop>");
            return true;
        }

        if (args[0].equalsIgnoreCase("start")) {
            if (plugin.getGameManager().getCurrentState() != GameState.LOBBY) {
                player.sendMessage(ChatColor.RED + "Une partie est déjà en cours !");
                return true;
            }
            
            // Start the game
            plugin.getExtractionGame().startGame();
            return true;
        }

        if (args[0].equalsIgnoreCase("stop")) {
            if (plugin.getGameManager().getCurrentState() == GameState.LOBBY) {
                player.sendMessage(ChatColor.RED + "Aucune partie n'est en cours !");
                return true;
            }
            
            // Stop the game
            plugin.getExtractionGame().endGame();
            player.sendMessage(ChatColor.GREEN + "La partie a été arrêtée !");
            return true;
        }

        player.sendMessage(ChatColor.RED + "Utilisation : /extraction <start|stop>");
        return true;
    }
} 