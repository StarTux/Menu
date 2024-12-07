package com.cavetale.menu;

import org.bukkit.plugin.java.JavaPlugin;

public final class MenuPlugin extends JavaPlugin {
    private static MenuPlugin instance;
    private MenuCommand menuCommand = new MenuCommand(this);
    private AdminCommand adminCommand = new AdminCommand(this);

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        menuCommand.enable();
        adminCommand.enable();
        new EventListener().enable();
    }

    @Override
    public void onDisable() {
    }

    public static MenuPlugin menuPlugin() {
        return instance;
    }
}
