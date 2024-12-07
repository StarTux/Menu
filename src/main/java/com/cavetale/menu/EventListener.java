package com.cavetale.menu;

import com.cavetale.core.menu.MenuItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public final class EventListener implements Listener {
    public void enable() {
        Bukkit.getPluginManager().registerEvents(this, MenuPlugin.menuPlugin());
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if (!event.isRightClick() && !event.isLeftClick()) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        if (!player.hasPermission("menu.menu")) {
            return;
        }
        // Player inventory
        // This will not work in creative mode
        if (event.getView().getType() != InventoryType.CRAFTING) {
            return;
        }
        // Empty cursor
        if (event.getCursor() != null && !event.getCursor().isEmpty()) {
            return;
        }
        if (event.getSlot() >= 0) {
            return;
        }
        // No return
        event.setCancelled(true);
        player.closeInventory();
        Bukkit.getScheduler().runTask(MenuPlugin.menuPlugin(), () -> {
                Menu.openMenu(player);
            });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onMenuItem(MenuItemEvent event) {
        if (!event.shouldOpen()) return;
        final Menu menu = new Menu(event.getPlayer());
        menu.load(event.getEntries());
        menu.open();
    }
}
