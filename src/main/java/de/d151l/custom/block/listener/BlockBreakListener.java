package de.d151l.custom.block.listener;

import de.d151l.custom.block.CustomBlocks;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private final CustomBlocks plugin;

    private final MiniMessage miniMessage;

    public BlockBreakListener(CustomBlocks plugin) {
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

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();

        if (block.getType() != Material.NOTE_BLOCK)
            return;

        if (this.plugin.getCustomBlockHandler().isNotCustomBlock(block))
            return;

        event.setCancelled(true);

        if (!this.plugin.getCustomBlockHandler().breakCustomBlock(block)) {
            player.sendMessage(this.miniMessage.deserialize("<prefix> <red>Custom blocks can't be broken!"));
        }
    }
}
