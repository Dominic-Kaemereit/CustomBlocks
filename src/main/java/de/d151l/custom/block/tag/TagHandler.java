package de.d151l.custom.block.tag;

import de.d151l.custom.block.CustomBlocks;
import de.d151l.custom.block.config.CustomTagsConfig;
import de.d151l.custom.block.config.json.ConfigMapper;

import java.util.List;
import java.util.logging.Level;

public class TagHandler {

    public TagHandler(CustomBlocks plugin) {

        try {
            new CustomTagsConfig(ConfigMapper.getOrCreate(
                    plugin.getDataFolder(),
                    "tags.json",
                    new CustomTagsConfig(null),
                    CustomTagsConfig.class
            ));
        } catch (Exception exception) {
            plugin.getLogger().log(Level.SEVERE, "Could not load tags.json!", exception);
        }
    }

    public Tag getTag(String key) {
        if (CustomTagsConfig.getInstance() == null)
            throw new NullPointerException("CustomTagsConfig is not initialized yet!");

        return this.getTags().stream().filter(tag -> tag.key().equals(key)).findFirst().orElse(null);
    }

    /**
     * Returns a list of all tags.
     * @return A list of all tags.
     */
    public List<Tag> getTags() {
        if (CustomTagsConfig.getInstance() == null)
            throw new NullPointerException("CustomTagsConfig is not initialized yet!");

        return CustomTagsConfig.getInstance().getTags();
    }
}
