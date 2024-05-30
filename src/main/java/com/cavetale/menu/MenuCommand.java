package com.cavetale.menu;

import com.cavetale.core.event.player.PluginPlayerEvent;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public final class MenuCommand implements TabExecutor {
    private final MenuPlugin plugin;

    public MenuCommand enable() {
        plugin.getCommand("menu").setExecutor(this);
        return this;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String alias, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Player expected");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            if (!plugin.openMenu(player, "main")) {
                player.sendMessage("Main menu not found!");
            }
            PluginPlayerEvent.Name.OPEN_MENU.call(plugin, player);
        } else if (args.length == 1) {
            if (!plugin.openMenu(player, args[0])) {
                player.sendMessage("Unknown menu: " + args[0]);
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        return Collections.emptyList();
    }
}
