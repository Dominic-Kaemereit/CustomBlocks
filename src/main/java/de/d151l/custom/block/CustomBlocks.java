package de.d151l.custom.block;

import de.d151l.custom.block.tag.TagHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomBlocks extends JavaPlugin {

    private static CustomBlocks instance;

    private final TagHandler tagHandler = new TagHandler(this);

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
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
}
