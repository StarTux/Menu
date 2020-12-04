package com.cavetale.menu;

import com.cavetale.menu.config.Menu;
import com.cavetale.menu.config.Restrictions;
import com.cavetale.menu.gui.Gui;
import com.cavetale.menu.gui.GuiListener;
import com.cavetale.menu.util.Yaml;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class MenuPlugin extends JavaPlugin {
    @Getter private MenuPlugin instance;
    private MenuCommand menuCommand;
    private AdminCommand adminCommand;
    private GuiListener guiListener;
    private Map<String, Menu> menus = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        menuCommand = new MenuCommand(this).enable();
        adminCommand = new AdminCommand(this).enable();
        guiListener = new GuiListener(this).enable();
        Bukkit.getScheduler().runTaskTimer(this, this::tick, 1L, 1L);
        loadAllMenus();
        getLogger().info(menus.size() + " menus loaded");
    }

    @Override
    public void onDisable() {
        closeAllGuis();
        instance = null;
    }

    public void closeAllGuis() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            Gui gui = Gui.of(player);
            if (gui != null) player.closeInventory();
        }
    }

    void tick() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            Gui gui = Gui.of(player);
            if (gui == null) continue;
            gui.tick(player);
        }
    }

    public int loadAllMenus() {
        menus.clear();
        File folder = new File(getDataFolder(), "menus");
        folder.mkdirs();
        for (File file : folder.listFiles()) {
            String name = file.getName();
            if (!name.endsWith(".yml")) continue;
            name = name.substring(0, name.length() - 4);
            Menu menu;
            try {
                menu = Yaml.load(file, Menu.class);
            } catch (Exception e) {
                getLogger().warning("Error loading file " + file);
                e.printStackTrace();
                continue;
            }
            if (menu.getId() == null) menu.setId(name);
            try {
                menu.validate();
            } catch (Exception e) {
                getLogger().warning("Error validating menu " + file);
                e.printStackTrace();
                continue;
            }
            menus.put(name, menu);
        }
        return menus.size();
    }

    public Menu findMenu(String id) {
        return menus.get(id);
    }

    /**
     * Convenience method.
     * @return true if menu was found, false otherwise.
     */
    public boolean openMenu(Player player, String menuId) {
        Menu menu = menus.get(menuId);
        if (menu == null) return false;
        try {
            menu.testRestrictions(player);
        } catch (Restrictions.Error err) {
            player.sendMessage(ChatColor.RED + err.getMessage());
            return true;
        }
        Gui gui;
        try {
            gui = menu.open(this, player);
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "An error has occured. Please contact an administrator.");
            getLogger().warning("Error opening menu for " + player.getName() + ": " + menuId);
            e.printStackTrace();
        }
        return true;
    }
}
