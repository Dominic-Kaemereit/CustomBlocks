package de.d151l.custom.block.config;

import org.bukkit.Instrument;
import org.bukkit.Material;

import java.util.List;

public class CustomBlocksConfig {

    private final List<CustomBlockConfig> customBlocks = List.of(
            new CustomBlockConfig(
                    "custom_block",
                    new ItemConfig(
                            "Custom Block",
                            new String[] {
                                    "This is a custom block."
                            },
                            Material.NOTE_BLOCK,
                            1,
                            new String[] {
                                    "test:stone"
                            }
                    ),
                    new NoteBlockConfig(
                            Instrument.BANJO,
                            1,
                            false,
                            "block_stone_place",
                            "block_stone_break"
                    )
            )
    );

    public CustomBlocksConfig() {
    }

    /**
     * Returns the list of custom blocks.
     * @return The list of custom blocks.
     */
    public List<CustomBlockConfig> getCustomBlocks() {
        return customBlocks;
    }
}
