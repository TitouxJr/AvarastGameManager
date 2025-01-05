package io.github.TitouxJr.avarastGameManager.managers;

import io.github.TitouxJr.avarastGameManager.AvarastGameManager;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

public class BlockManager {
    private final AvarastGameManager plugin;
    private final Set<Material> breakableBlocks;
    private boolean allowBlockBreaking;

    public BlockManager(AvarastGameManager plugin) {
        this.plugin = plugin;
        this.breakableBlocks = new HashSet<>();
        this.allowBlockBreaking = false;
    }

    public boolean isAllowBlockBreaking() {
        return allowBlockBreaking;
    }

    public void setAllowBlockBreaking(boolean allow) {
        this.allowBlockBreaking = allow;
    }

    public void addBreakableBlock(Material material) {
        breakableBlocks.add(material);
    }

    public void removeBreakableBlock(Material material) {
        breakableBlocks.remove(material);
    }

    public void clearBreakableBlocks() {
        breakableBlocks.clear();
    }

    public Set<Material> getBreakableBlocks() {
        return new HashSet<>(breakableBlocks);
    }

    public void loadBreakableBlocks() {
        // Load from config
        plugin.reloadConfig();
        clearBreakableBlocks();
        for (String materialName : plugin.getConfig().getStringList("breakable-blocks")) {
            try {
                Material material = Material.valueOf(materialName.toUpperCase());
                addBreakableBlock(material);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid material in config: " + materialName);
            }
        }
    }
} 