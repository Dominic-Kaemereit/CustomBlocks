package de.d151l.custom.block.blocks;

import de.d151l.custom.block.CustomBlocks;
import de.d151l.custom.block.config.CustomBlockConfig;
import de.d151l.custom.block.config.CustomBlocksConfig;
import de.d151l.custom.block.config.json.ConfigMapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public class CustomBlockHandler {

    private final CustomBlocks plugin;

    private final File blocksFolder;

    private final Map<String, CustomBlocksConfig> customBlocks = new HashMap<>();

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

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
     * Gives a custom block to a player.
     * @param player The player to give the custom block to.
     * @param blockName The name of the custom block.
     * @param amount The amount of custom blocks to give.
     * @return Whether the custom block was given successfully.
     */
    public boolean giveCustomBlock(final Player player, final String blockName, final int amount) {
        CustomBlockConfig customBlockConfig = null;
        for (CustomBlocksConfig customBlocksConfig : this.customBlocks.values()) {
            for (CustomBlockConfig customBlock : customBlocksConfig.getCustomBlocks()) {
                if (customBlock.key().equalsIgnoreCase(blockName)) {
                    customBlockConfig = customBlock;
                    break;
                }
            }
        }

        if (customBlockConfig == null)
            return false;

        ItemStack itemStack = new ItemStack(customBlockConfig.itemConfig().material(), amount);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(miniMessage.deserialize(customBlockConfig.itemConfig().displayName()));

        List<Component> components = Arrays.stream(customBlockConfig.itemConfig().lore()).map(miniMessage::deserialize).toList();
        itemMeta.lore(components);

        itemMeta.setCustomModelData(customBlockConfig.itemConfig().customModelData());

        itemStack.setItemMeta(itemMeta);

        player.getInventory().addItem(itemStack);
        return true;
    }

    /**
     * Returns a list of all custom block keys.
     * @return A list of all custom block keys.
     */
    public List<String> getCustomBlockKeys() {
        List<String> keys = new ArrayList<>();
        for (CustomBlocksConfig customBlocksConfig : this.customBlocks.values()) {
            for (CustomBlockConfig customBlock : customBlocksConfig.getCustomBlocks()) {
                keys.add(customBlock.key());
            }
        }

        return keys;
    }

    /**
     * Returns a map of all custom blocks.
     * @return A map of all custom blocks.
     */
    public Map<String, CustomBlocksConfig> getCustomBlocks() {
        return customBlocks;
    }
}
