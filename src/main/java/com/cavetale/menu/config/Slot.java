package com.cavetale.menu.config;

import com.cavetale.menu.gui.Gui;
import com.cavetale.menu.util.Effects;
import java.util.Objects;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@Data
public final class Slot {
    /**
     * Index where the slot will be placed within the
     * menu. Alternatively, column and row may be specified.
     */
    int index = -1;
    int column = -1;
    int row = -1;
    /**
     * A description of the item stack to be displayed. Mandatory.
     */
    Icon icon;
    /**
     * What should happen if this item is clicked. Optional.
     */
    ClickEvent click;

    public void validate() {
        if (index >= 0) {
            row = index / 9;
            column = index % 9;
        } else if (row >= 0 && column >= 0) {
            index = row * 9 + column;
        }
        if (column > 8) throw new IllegalStateException("column=" + column);
        if (row > 6) throw new IllegalStateException("row=" + row);
        Objects.requireNonNull(icon, "icon=null");
        try {
            icon.validate();
        } catch (Exception e) {
            throw new IllegalStateException("slot[" + index + "] icon", e);
        }
        if (click != null) {
            try {
                click.validate();
            } catch (Exception e) {
                throw new IllegalStateException("slot[" + index + "] click", e);
            }
        }
    }

    public ItemStack toItemStack() {
        try {
            return icon.toItemStack();
        } catch (Exception e) {
            throw new IllegalStateException("slot[" + index + "]", e);
        }
    }

    public void onClick(Gui gui, InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (!event.isLeftClick()) {
            if (gui.getMenu() != null && gui.getMenu().isAutoSounds()) {
                Effects.fail(player);
            }
            return;
        }
        if (click != null) {
            if (gui.getMenu() != null && gui.getMenu().isAutoSounds()) {
                Effects.click(player);
            }
            click.onClick(gui, player, event, index);
        } else {
            if (gui.getMenu() != null && gui.getMenu().isAutoSounds()) {
                Effects.fail(player);
            }
        }
    }
}
