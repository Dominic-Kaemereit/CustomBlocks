package de.d151l.custom.block.inventory;

import de.d151l.custom.block.CustomBlocks;
import de.d151l.custom.block.config.CustomBlockConfig;
import de.d151l.custom.block.head.Head;
import de.d151l.custom.block.tag.Tag;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.ScrollType;
import dev.triumphteam.gui.components.util.GuiFiller;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.ScrollingGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomBlocksInventory {

    private final CustomBlocks plugin;

    private final MiniMessage miniMessage;

    public CustomBlocksInventory(CustomBlocks plugin) {
        this.plugin = plugin;

        this.miniMessage = MiniMessage.builder()
                .tags(
                        TagResolver.builder()
                                .resolver(this.plugin.getPrefix())
                                .resolver(StandardTags.color())
                                .resolver(StandardTags.decorations())
                                .resolver(StandardTags.font())
                                .build()
                ).build();
    }

    public void open(final Player player, @Nullable String searchKey) {
        if (searchKey == null) {
            searchKey = "";
        }

        Tag tag = this.plugin.getTagHandler().getTag(searchKey);
        String title = "";

        if (tag != null) {
            title = " " + tag.displayName();
        }

        final ScrollingGui gui = Gui.scrolling()
                .title(this.miniMessage.deserialize("<gray>Custom Blocks" + title))
                .rows(6)
                .pageSize(45)
                .scrollType(ScrollType.HORIZONTAL)
                .create();

        gui.disableAllInteractions();

        gui.getFiller().fillSide(GuiFiller.Side.RIGHT, List.of(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem()));

        gui.setItem(8, ItemBuilder.skull().texture(Head.OAK_WOOD_ARROW_UP.getValue()).name(
                this.miniMessage.deserialize("<gray>Nach oben")
        ).asGuiItem(event -> {
            gui.previous();
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.8F, 0.8F);
        }));

        gui.setItem(53, ItemBuilder.skull().texture(Head.OAK_WOOD_ARROW_DOWN.getValue()).name(
                this.miniMessage.deserialize("<gray>Nach unten")
        ).asGuiItem(event -> {
            gui.next();
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.8F, 0.8F);
        }));

        final List<CustomBlockConfig> customBlockKeys = new ArrayList<>();
        if (tag != null)
            customBlockKeys.addAll(this.plugin.getCustomBlockHandler().getCustomBlockConfigByTag(tag.key()));
         else
            customBlockKeys.addAll(this.plugin.getCustomBlockHandler().getAllCustomBlockConfig());

        for (CustomBlockConfig customBlockConfig : customBlockKeys) {
            List<Tag> tags = customBlockConfig.itemConfig().tags().stream().map(this.plugin.getTagHandler()::getTag).toList();
            List<Component> list = tags.stream().map(Tag::displayName).map(this.miniMessage::deserialize).toList();
            Component tagComponent = this.miniMessage.deserialize("<gray>Tags: <white>");
            for (Component component : list) {
                tagComponent = tagComponent.append(component).append(this.miniMessage.deserialize("<gray>, <white>"));
            }

            List<Component> components = new ArrayList<>();
            components.add(Component.empty());
            components.addAll(Arrays.stream(customBlockConfig.itemConfig().lore()).map(miniMessage::deserialize).toList());

            gui.addItem(ItemBuilder.from(customBlockConfig.itemConfig().material())
                    .name(this.miniMessage.deserialize(customBlockConfig.itemConfig().displayName()))
                    .lore(components)
                    .model(customBlockConfig.itemConfig().customModelData())
                    .asGuiItem(event -> {
                        player.performCommand("cgive " + customBlockConfig.key() + " 1");
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.8F, 0.8F);
                    })
            );
        }

        gui.open(player);
    }
}
