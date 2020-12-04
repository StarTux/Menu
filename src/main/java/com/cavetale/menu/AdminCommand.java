package com.cavetale.menu;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import com.cavetale.core.command.CommandNode;

@RequiredArgsConstructor
public final class AdminCommand implements TabExecutor {
    private final MenuPlugin plugin;
    private CommandNode rootNode;

    public AdminCommand enable() {
        rootNode = new CommandNode("menuadmin");
        rootNode.addChild("reload").denyTabCompletion()
            .description("Reload menus")
            .senderCaller(this::reload);
        plugin.getCommand("menuadmin").setExecutor(this);
        return this;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String alias, final String[] args) {
        return rootNode.call(sender, command, alias, args);
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        return rootNode.complete(sender, command, alias, args);
    }

    boolean reload(CommandSender sender, String[] args) {
        if (args.length != 0) return false;
        int count = plugin.loadAllMenus();
        sender.sendMessage(count + " menus loaded.");
        return true;
    }
}
