package de.d151l.custom.block.config;

import de.d151l.custom.block.tag.Tag;

import java.util.List;

public class CustomTagsConfig {

    private static CustomTagsConfig instance;

    private final List<Tag> tags = List.of(
            new Tag("test:stone", "<gray>Stone")
    );

    public CustomTagsConfig(CustomTagsConfig instance) {
        CustomTagsConfig.instance = instance;
    }

    /**
     * Returns a list of all tags.
     * @return A list of all tags.
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Returns the instance of this class.
     * @return The instance of this class.
     */
    public static CustomTagsConfig getInstance() {
        return instance;
    }
}
