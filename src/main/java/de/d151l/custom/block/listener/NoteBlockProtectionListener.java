package de.d151l.custom.block.listener;

import de.d151l.custom.block.CustomBlocks;
import io.papermc.paper.event.world.WorldGameRuleChangeEvent;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

public class NoteBlockProtectionListener implements Listener {

    private final CustomBlocks plugin;

    public NoteBlockProtectionListener(CustomBlocks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPhysicsEvent event) {
        final Block block = event.getBlock();

        if (block.getType() != org.bukkit.Material.NOTE_BLOCK)
            return;

        if (!this.plugin.getCustomBlockHandler().isCustomBlock(block))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockRedstone(BlockRedstoneEvent event) {
        final Block block = event.getBlock();

        if (block.getType() != org.bukkit.Material.NOTE_BLOCK)
            return;

        if (!this.plugin.getCustomBlockHandler().isCustomBlock(block))
            return;

        event.setNewCurrent(event.getOldCurrent());
    }
}
