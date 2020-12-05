package com.cavetale.menu.config;

import com.cavetale.menu.gui.Gui;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

@Data
public final class ClickEvent {
    List<String> commands;
    List<String> consoleCommands;
    String openMenu;
    boolean close;
    SoundEffect sound;

    public void validate() {
        if (sound != null) {
            sound.validate();
        }
        if (commands == null) commands = Collections.emptyList();
        if (consoleCommands == null) consoleCommands = Collections.emptyList();
    }

    public void onClick(Gui gui, Player player, InventoryClickEvent event, int index) {
        if (close) {
            player.closeInventory();
        }
        for (String command : commands) {
            player.performCommand(command);
        }
        for (String consoleCommand : consoleCommands) {
            consoleCommand = consoleCommand
                .replace("{player}", player.getName())
                .replace("{uuid}", player.getUniqueId().toString());
            String menuId = gui.getMenu() != null ? gui.getMenu().getId() : "?";
            gui.getPlugin().getLogger().info("[" + menuId + "/" + index + "] Dispatching console command: " + consoleCommand);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), consoleCommand);
        }
        if (sound != null) {
            sound.play(player);
        }
        if (openMenu != null) {
            gui.getPlugin().openMenu(player, openMenu);
        }
    }
}
