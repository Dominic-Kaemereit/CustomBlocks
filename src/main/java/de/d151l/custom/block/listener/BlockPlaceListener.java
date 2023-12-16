package de.d151l.custom.block.listener;

import de.d151l.custom.block.CustomBlocks;
import de.d151l.custom.block.config.CustomBlockConfig;
import de.d151l.custom.block.config.NoteBlockConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class BlockPlaceListener implements Listener {

    private final CustomBlocks plugin;

    private final MiniMessage miniMessage;

    public BlockPlaceListener(CustomBlocks plugin) {
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
    public void onBlockPlace(BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = event.getItemInHand();

        if (!itemStack.hasItemMeta())
            return;

        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (!itemMeta.getPersistentDataContainer().has(this.plugin.getCustomBlockHandler().getIsCustomBlockNamespacedKey()))
            return;

        event.setCancelled(true);

        if (!itemMeta.getPersistentDataContainer().has(this.plugin.getCustomBlockHandler().getCustomBlockKeyNamespacedKey())) {
            player.sendMessage(this.miniMessage.deserialize("<prefix> <red>Could not find custom block key!"));
            return;
        }

        final String customBlockKey = itemMeta.getPersistentDataContainer().get(this.plugin.getCustomBlockHandler().getCustomBlockKeyNamespacedKey(), PersistentDataType.STRING);
        final CustomBlockConfig customBlockConfig = this.plugin.getCustomBlockHandler().getCustomBlockConfig(customBlockKey);

        if (customBlockConfig == null) {
            player.sendMessage(this.miniMessage.deserialize("<prefix> <red>Could not find custom block config!"));
            return;
        }

        final Location location = new Location(
                player.getWorld(),
                event.getBlock().getLocation().getBlockX(),
                event.getBlock().getLocation().getBlockY(),
                event.getBlock().getLocation().getBlockZ()
        );

        Bukkit.getScheduler().runTask(this.plugin, () -> {
            this.plugin.getCustomBlockHandler().placeCustomBlock(customBlockConfig, location);
        });

        if (player.getGameMode().equals(GameMode.CREATIVE))
            return;

        this.plugin.getLogger().info("9BlockPlaceEvent: " + player.getGameMode().equals(GameMode.CREATIVE));

        itemStack.setAmount(itemStack.getAmount() - 1);
        player.getInventory().setItemInMainHand(itemStack);
    }
}
