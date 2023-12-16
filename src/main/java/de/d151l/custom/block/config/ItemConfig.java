package de.d151l.custom.block.config;

import org.bukkit.Material;

import java.util.List;

public record ItemConfig(
        String displayName,
        String[] lore,
        Material material,
        int customModelData,
        List<String> tags
) {
}
