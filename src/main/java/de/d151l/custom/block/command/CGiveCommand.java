package de.d151l.custom.block.command;

import de.d151l.custom.block.CustomBlocks;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CGiveCommand implements CommandExecutor, TabCompleter {

    private final CustomBlocks plugin;

    private final MiniMessage miniMessage;

    public CGiveCommand(CustomBlocks plugin) {
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

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(miniMessage.deserialize("<prefix> <red>Usage: /cgive <block> [amount]"));
            return true;
        }

        int amount = 1;
        if (args.length > 1) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(miniMessage.deserialize("<prefix> <red>Invalid amount!"));
                return true;
            }
        }

        if (amount < 1) {
            player.sendMessage(miniMessage.deserialize("<prefix> <red>Invalid amount!"));
            return true;
        }

        if (!plugin.getCustomBlockHandler().giveCustomBlock(player, args[0], amount)) {
            player.sendMessage(miniMessage.deserialize("<prefix> <red>Invalid block!"));
            return true;
        }

        player.sendMessage(miniMessage.deserialize("<prefix> <green>Successfully given custom block!"));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

            if (args.length == 1) {
                return plugin.getCustomBlockHandler().getCustomBlockKeys();
            }

            return null;
    }
}
