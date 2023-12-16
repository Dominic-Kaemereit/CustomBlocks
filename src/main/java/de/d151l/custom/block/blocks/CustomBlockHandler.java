package de.d151l.custom.block.blocks;

import com.jeff_media.customblockdata.CustomBlockData;
import de.d151l.custom.block.CustomBlocks;
import de.d151l.custom.block.config.CustomBlockConfig;
import de.d151l.custom.block.config.CustomBlocksConfig;
import de.d151l.custom.block.config.json.ConfigMapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public class CustomBlockHandler {

    private final CustomBlocks plugin;

    private final MiniMessage miniMessage;

    private final Map<String, CustomBlocksConfig> customBlocks = new HashMap<>();

    private final NamespacedKey isCustomBlockNamespacedKey;
    private final NamespacedKey customBlockKeyNamespacedKey;

    public CustomBlockHandler(CustomBlocks plugin) {
        this.plugin = plugin;

        this.isCustomBlockNamespacedKey = new NamespacedKey(plugin, "custom_block");
        this.customBlockKeyNamespacedKey = new NamespacedKey(plugin, "custom_block_key");

        this.miniMessage = MiniMessage.builder()
                .tags(
                        TagResolver.builder()
                                .resolver(this.plugin.getPrefix())
                                .resolver(StandardTags.color())
                                .resolver(StandardTags.decorations())
                                .resolver(StandardTags.font())
                                .build()
                ).build();

        final String dataFolder = this.plugin.getDataFolder().getPath();
        File blocksFolder = new File(dataFolder + "/blocks");

        if (!blocksFolder.exists())
            blocksFolder.mkdirs();

        if (!blocksFolder.isDirectory())
            throw new RuntimeException("Could not create blocks folder!");

        if (!blocksFolder.canRead())
            throw new RuntimeException("Could not read blocks folder!");

        if (!blocksFolder.canWrite())
            throw new RuntimeException("Could not write to blocks folder!");

        if (!blocksFolder.canExecute())
            throw new RuntimeException("Could not execute blocks folder!");

        if (blocksFolder.listFiles() == null)
            throw new RuntimeException("Could not list files in blocks folder!");

        if (Objects.requireNonNull(blocksFolder.listFiles()).length == 0) {
            try {
                ConfigMapper.writeJson(
                        blocksFolder,
                        "stones.json",
                        new CustomBlocksConfig()
                );
            } catch (Exception exception) {
                plugin.getLogger().log(Level.ALL, "Could not create default stones.json!", exception);
            }
        }

        for (File file : Objects.requireNonNull(blocksFolder.listFiles())) {
            if (!file.getName().endsWith(".json"))
                continue;

            try {
                final CustomBlocksConfig blocksConfig = ConfigMapper.getOrCreate(
                        blocksFolder,
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

        itemMeta.getPersistentDataContainer().set(this.isCustomBlockNamespacedKey, PersistentDataType.BOOLEAN, true);
        itemMeta.getPersistentDataContainer().set(this.customBlockKeyNamespacedKey, PersistentDataType.STRING, customBlockConfig.key());

        itemStack.setItemMeta(itemMeta);

        player.getInventory().addItem(itemStack);
        return true;
    }

    /**
     * Places a custom block at a location.
     * @param customBlockConfig The custom block config.
     * @param location The location to place the custom block at.
     */
    public void placeCustomBlock(final CustomBlockConfig customBlockConfig, final Location location) {
        final Block block = location.getBlock();
        block.setType(Material.NOTE_BLOCK);

        final NoteBlock blockData = (NoteBlock) block.getBlockData();
        blockData.setInstrument(customBlockConfig.noteBlockConfig().instrument());
        blockData.setNote(new Note(customBlockConfig.noteBlockConfig().note()));
        blockData.setPowered(customBlockConfig.noteBlockConfig().powered());

        block.setBlockData(blockData);

        location.getWorld().playSound(location, customBlockConfig.noteBlockConfig().placementSound(), 1, 1);

        final CustomBlockData customBlockData = new CustomBlockData(block, plugin);
        customBlockData.set(this.isCustomBlockNamespacedKey, PersistentDataType.BOOLEAN, true);
        customBlockData.set(this.customBlockKeyNamespacedKey, PersistentDataType.STRING, customBlockConfig.key());
    }

    public boolean breakCustomBlock(final Block block) {
        final CustomBlockData customBlockData = new CustomBlockData(block, plugin);
        if (!customBlockData.has(this.isCustomBlockNamespacedKey, PersistentDataType.BOOLEAN))
            return false;

        final String customBlockKey = customBlockData.get(this.customBlockKeyNamespacedKey, PersistentDataType.STRING);
        final CustomBlockConfig customBlockConfig = this.getCustomBlockConfig(customBlockKey);

        if (customBlockConfig == null)
            return false;

        block.setType(Material.AIR);
        block.getWorld().playSound(block.getLocation(), customBlockConfig.noteBlockConfig().breakSound(), 1, 1);
        return true;
    }

    /**
     * Returns whether a block is a custom block.
     * @param block The block to check.
     * @return Whether a block is a custom block.
     */
    public boolean isNotCustomBlock(final Block block) {
        final CustomBlockData customBlockData = new CustomBlockData(block, plugin);
        return !customBlockData.has(this.isCustomBlockNamespacedKey, PersistentDataType.BOOLEAN);
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
     * Returns a custom block config by its key.
     * @param key The key of the custom block.
     * @return The custom block config.
     */
    public CustomBlockConfig getCustomBlockConfig(final String key) {
        for (CustomBlocksConfig customBlocksConfig : this.customBlocks.values()) {
            for (CustomBlockConfig customBlock : customBlocksConfig.getCustomBlocks()) {
                if (customBlock.key().equalsIgnoreCase(key)) {
                    return customBlock;
                }
            }
        }

        return null;
    }

    /**
     * Returns a map of all custom blocks.
     * @return A map of all custom blocks.
     */
    public Map<String, CustomBlocksConfig> getCustomBlocks() {
        return customBlocks;
    }

    /**
     * Returns the namespaced key for the isCustomBlock tag.
     * @return The namespaced key for the isCustomBlock tag.
     */
    public NamespacedKey getIsCustomBlockNamespacedKey() {
        return isCustomBlockNamespacedKey;
    }

    /**
     * Returns the namespaced key for the customBlockKey tag.
     * @return The namespaced key for the customBlockKey tag.
     */
    public NamespacedKey getCustomBlockKeyNamespacedKey() {
        return customBlockKeyNamespacedKey;
    }
}
