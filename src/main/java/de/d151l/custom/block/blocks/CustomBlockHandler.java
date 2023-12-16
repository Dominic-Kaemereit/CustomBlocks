package de.d151l.custom.block.blocks;

import de.d151l.custom.block.CustomBlocks;
import de.d151l.custom.block.config.CustomBlocksConfig;
import de.d151l.custom.block.config.json.ConfigMapper;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class CustomBlockHandler {

    private final CustomBlocks plugin;

    private final File blocksFolder;

    private final Map<String, CustomBlocksConfig> customBlocks = new HashMap<>();

    public CustomBlockHandler(CustomBlocks plugin) {
        this.plugin = plugin;

        final String dataFolder = this.plugin.getDataFolder().getPath();
        this.blocksFolder = new File(dataFolder + "/blocks");

        if (!this.blocksFolder.exists())
            this.blocksFolder.mkdirs();

        if (!this.blocksFolder.isDirectory())
            throw new RuntimeException("Could not create blocks folder!");

        if (!this.blocksFolder.canRead())
            throw new RuntimeException("Could not read blocks folder!");

        if (!this.blocksFolder.canWrite())
            throw new RuntimeException("Could not write to blocks folder!");

        if (!this.blocksFolder.canExecute())
            throw new RuntimeException("Could not execute blocks folder!");

        if (this.blocksFolder.listFiles() == null)
            throw new RuntimeException("Could not list files in blocks folder!");

        if (Objects.requireNonNull(this.blocksFolder.listFiles()).length == 0) {
            try {
                ConfigMapper.writeJson(
                        this.blocksFolder,
                        "stones.json",
                        new CustomBlocksConfig()
                );
            } catch (Exception exception) {
                plugin.getLogger().log(Level.ALL, "Could not create default stones.json!", exception);
            }
        }

        for (File file : Objects.requireNonNull(this.blocksFolder.listFiles())) {
            if (!file.getName().endsWith(".json"))
                continue;

            try {
                final CustomBlocksConfig blocksConfig = ConfigMapper.getOrCreate(
                        this.blocksFolder,
                        file.getName(),
                        new CustomBlocksConfig(),
                        CustomBlocksConfig.class
                );

                this.customBlocks.put(FilenameUtils.removeExtension(file.getName()), blocksConfig);
            } catch (Exception exception) {
                plugin.getLogger().log(Level.SEVERE, "Could not load " + file.getName() + "!", exception);
            }
        }
    }

    /**
     * Returns a map of all custom blocks.
     * @return A map of all custom blocks.
     */
    public Map<String, CustomBlocksConfig> getCustomBlocks() {
        return customBlocks;
    }
}
