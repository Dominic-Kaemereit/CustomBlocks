package de.d151l.custom.block.config;

import org.bukkit.Material;

public record ItemConfig(
        String displayName,
        String[] lore,
        Material material,
        int customModelData,
        String[] tags
) {
}
