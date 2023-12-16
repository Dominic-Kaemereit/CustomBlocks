package de.d151l.custom.block;

import de.d151l.custom.block.blocks.CustomBlockHandler;
import de.d151l.custom.block.command.CBlocksCommand;
import de.d151l.custom.block.command.CGiveCommand;
import de.d151l.custom.block.config.CustomBlockConfig;
import de.d151l.custom.block.config.CustomBlocksConfig;
import de.d151l.custom.block.config.ItemConfig;
import de.d151l.custom.block.config.NoteBlockConfig;
import de.d151l.custom.block.inventory.CustomBlocksInventory;
import de.d151l.custom.block.listener.BlockBreakListener;
import de.d151l.custom.block.listener.BlockPlaceListener;
import de.d151l.custom.block.listener.NoteBlockProtectionListener;
import de.d151l.custom.block.tag.TagHandler;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CustomBlocks extends JavaPlugin {

    private static CustomBlocks instance;

    private final TagResolver.Single prefix = Placeholder.parsed("prefix", "<gray>[<yellow>CustomBlocks<gray>]");

    private final TagHandler tagHandler = new TagHandler(this);
    private final CustomBlockHandler customBlockHandler = new CustomBlockHandler(this);
    private final CustomBlocksInventory customBlocksInventory = new CustomBlocksInventory(this);

    @Override
    public void onEnable() {
        instance = this;

        Objects.requireNonNull(this.getCommand("cgive")).setExecutor(new CGiveCommand(this));
        Objects.requireNonNull(this.getCommand("cblocks")).setExecutor(new CBlocksCommand(this));

        this.getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        this.getServer().getPluginManager().registerEvents(new NoteBlockProtectionListener(this), this);
    }

    /**
     * Returns the instance of this class.
     * @return The instance of this class.
     */
    public static CustomBlocks getInstance() {
        return instance;
    }

    /**
     * Returns the tag handler.
     * @return The tag handler.
     */
    public TagHandler getTagHandler() {
        return tagHandler;
    }

    /**
     * Returns the custom block handler.
     * @return The custom block handler.
     */
    public CustomBlockHandler getCustomBlockHandler() {
        return customBlockHandler;
    }

    /**
     * Returns the prefix tag.
     * @return The prefix tag.
     */
    public TagResolver.Single getPrefix() {
        return prefix;
    }

    /**
     * Returns the custom blocks inventory.
     * @return The custom blocks inventory.
     */
    public CustomBlocksInventory getCustomBlocksInventory() {
        return customBlocksInventory;
    }
}
