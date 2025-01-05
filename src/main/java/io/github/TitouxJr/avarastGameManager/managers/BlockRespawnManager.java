package io.github.TitouxJr.avarastGameManager.managers;

import io.github.TitouxJr.avarastGameManager.AvarastGameManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public class BlockRespawnManager {
    private final AvarastGameManager plugin;
    private final Map<Location, Material> brokenBlocks;

    public BlockRespawnManager(AvarastGameManager plugin) {
        this.plugin = plugin;
        this.brokenBlocks = new HashMap<>();
    }

    public void trackBrokenBlock(Block block) {
        brokenBlocks.put(block.getLocation(), block.getType());
    }

    public void respawnAllBlocks() {
        brokenBlocks.forEach((location, material) -> {
            location.getBlock().setType(material);
        });
        brokenBlocks.clear();
    }

    public void clear() {
        brokenBlocks.clear();
    }
} 