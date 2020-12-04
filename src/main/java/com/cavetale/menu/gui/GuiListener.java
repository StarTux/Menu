package com.cavetale.menu.gui;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor
public final class GuiListener implements Listener {
    private final JavaPlugin plugin;

    public GuiListener enable() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        return this;
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    void onInventoryOpen(final InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof Gui) {
            Gui gui = (Gui) event.getInventory().getHolder();
            gui.onInventoryOpen(event);
        }
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    void onInventoryClose(final InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof Gui) {
            Gui gui = (Gui) event.getInventory().getHolder();
            gui.onInventoryClose(event);
        }
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    void onInventoryClick(final InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof Gui) {
            Gui gui = (Gui) event.getInventory().getHolder();
            gui.onInventoryClick(event);
        }
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    void onInventoryDrag(final InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof Gui) {
            Gui gui = (Gui) event.getInventory().getHolder();
            gui.onInventoryDrag(event);
        }
    }
}
