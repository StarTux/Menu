package com.cavetale.menu;

import com.cavetale.core.command.AbstractCommand;

public final class AdminCommand extends AbstractCommand<MenuPlugin> {
    public AdminCommand(final MenuPlugin plugin) {
        super(plugin, "menuadmin");
    }

    @Override
    protected void onEnable() {
    }
}
