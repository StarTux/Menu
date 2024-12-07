package com.cavetale.menu;

import com.cavetale.core.command.AbstractCommand;
import org.bukkit.entity.Player;

public final class MenuCommand extends AbstractCommand<MenuPlugin> {
    public MenuCommand(final MenuPlugin plugin) {
        super(plugin, "menu");
    }

    @Override
    protected void onEnable() {
        rootNode.denyTabCompletion()
            .description("Open the menu")
            .playerCaller(this::menu);
    }

    private void menu(Player player) {
        Menu.openMenu(player);
    }
}
