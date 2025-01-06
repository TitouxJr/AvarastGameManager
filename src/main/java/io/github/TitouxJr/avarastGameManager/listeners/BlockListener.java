package io.github.TitouxJr.avarastGameManager.listeners;

import io.github.TitouxJr.avarastGameManager.AvarastGameManager;
import io.github.TitouxJr.avarastGameManager.game.GameState;
import io.github.TitouxJr.avarastGameManager.managers.BlockManager;
import io.github.TitouxJr.avarastGameManager.managers.BlockRespawnManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockListener implements Listener {
    private final AvarastGameManager plugin;
    private final BlockManager blockManager;
    private final BlockRespawnManager blockRespawnManager;

    public BlockListener(AvarastGameManager plugin, BlockManager blockManager, BlockRespawnManager blockRespawnManager) {
        this.plugin = plugin;
        this.blockManager = blockManager;
        this.blockRespawnManager = blockRespawnManager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        GameState currentState = plugin.getGameManager().getCurrentState();
        
        // Si le joueur est un opérateur, il peut casser n'importe quel bloc
        if (player.isOp()) {
            return;
        }
        
        // Si le jeu n'est pas actif, permettre le cassage normal des blocs
        if (currentState != GameState.ACTIVE) {
            return;
        }

        // Pendant le jeu, vérifier si le bloc peut être cassé
        if (!blockManager.isAllowBlockBreaking() || !blockManager.getBreakableBlocks().contains(event.getBlock().getType())) {
            event.setCancelled(true);
            return;
        }

        Location blockLoc = event.getBlock().getLocation();

        // Track the block for respawning
        blockRespawnManager.trackBrokenBlock(event.getBlock());

        // Get drops from the block
        ItemStack[] drops = event.getBlock().getDrops().toArray(new ItemStack[0]);
        if (drops.length > 0) {
            // Show notification
            String itemName = formatItemName(drops[0].getType().name());
            String message = ChatColor.GREEN + "+1 " + ChatColor.GOLD + itemName;
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));

            // Play effects
            blockLoc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, 
                blockLoc.clone().add(0.5, 1.5, 0.5), 10, 0.3, 0.3, 0.3, 0);
            player.playSound(blockLoc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1.0f);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        GameState currentState = plugin.getGameManager().getCurrentState();
        
        // Si le joueur est un opérateur, il peut placer n'importe quel bloc
        if (player.isOp()) {
            return;
        }
        
        // Empêcher le placement de blocs uniquement pendant le jeu
        if (currentState == GameState.ACTIVE) {
            event.setCancelled(true);
        }
    }

    private String formatItemName(String name) {
        String[] words = name.toLowerCase().replace("_", " ").split(" ");
        StringBuilder result = new StringBuilder();
        
        for (String word : words) {
            if (result.length() > 0) result.append(" ");
            result.append(Character.toUpperCase(word.charAt(0)))
                  .append(word.substring(1));
        }
        
        return result.toString();
    }
} 