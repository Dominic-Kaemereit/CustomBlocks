package de.d151l.custom.block.listener;

import de.d151l.custom.block.CustomBlocks;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class NoteBlockProtectionListener implements Listener {

    private final CustomBlocks plugin;

    public NoteBlockProtectionListener(CustomBlocks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        final Block block = event.getBlock();
        final Block topBlock = event.getBlock().getRelative(BlockFace.UP);
        final Block downBlock = event.getBlock().getRelative(BlockFace.DOWN);
        final Block northBlock = event.getBlock().getRelative(BlockFace.NORTH);
        final Block eastBlock = event.getBlock().getRelative(BlockFace.EAST);
        final Block southBlock = event.getBlock().getRelative(BlockFace.SOUTH);
        final Block westBlock = event.getBlock().getRelative(BlockFace.WEST);

        if (this.checkBlock(block) || this.checkBlock(topBlock) || this.checkBlock(downBlock) || this.checkBlock(northBlock) || this.checkBlock(eastBlock) || this.checkBlock(southBlock) || this.checkBlock(westBlock))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockRedstone(BlockRedstoneEvent event) {
        final Block block = event.getBlock();

        if (block.getType() != Material.NOTE_BLOCK)
            return;

        if (this.plugin.getCustomBlockHandler().isNotCustomBlock(block))
            return;

        event.setNewCurrent(event.getOldCurrent());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null)
            return;

        if (event.getAction().isLeftClick())
            return;

        if (event.getClickedBlock().getType() != Material.NOTE_BLOCK)
            return;

        event.setCancelled(true);
    }

    private boolean checkBlock(Block block) {
        if (block.getType() != Material.NOTE_BLOCK)
            return false;

        return !this.plugin.getCustomBlockHandler().isNotCustomBlock(block);
    }
}
