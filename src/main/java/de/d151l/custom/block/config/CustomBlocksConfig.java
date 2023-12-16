package de.d151l.custom.block.config;

import org.bukkit.Instrument;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class CustomBlocksConfig {

    private final List<CustomBlockConfig> customBlocks = new ArrayList<>();

    public CustomBlocksConfig() {
        if (this.customBlocks.isEmpty()) {
            this.customBlocks.add(
                    new CustomBlockConfig(
                            "test:stone",
                            new ItemConfig(
                                    "Custom Block",
                                    new String[] {
                                            "This is a custom block."
                                    },
                                    Material.STONE,
                                    1,
                                    List.of("test:stone")
                            ),
                            new NoteBlockConfig(
                                    Instrument.PIANO,
                                    0,
                                    true ,
                                    "block_stone_place",
                                    "block_stone_break"
                            )
                    )
            );
        }
    }

    /**
     * Returns the list of custom blocks.
     * @return The list of custom blocks.
     */
    public List<CustomBlockConfig> getCustomBlocks() {
        return customBlocks;
    }
}
