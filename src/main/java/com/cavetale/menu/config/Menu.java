package com.cavetale.menu.config;

import com.cavetale.menu.MenuPlugin;
import com.cavetale.menu.gui.Gui;
import com.cavetale.menu.util.Effects;
import com.cavetale.menu.util.Text;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * This is the configuration for a menu.
 * It is able to create a Gui which in turn is what the player gets to
 * see.
 */
@Data
public final class Menu {
    /**
     * Unique menu id.
     * If no id is provided, MenuPlugin will fill it in based on the filename.
     */
    String id;
    /**
     * Menu size. Must be divisible by 9. Alternatively, rows can be
     * specified.
     */
    int size;
    int rows;
    String title = "";
    List<Slot> slots;
    boolean autoSounds;
    Restrictions restrictions;

    public void validate() {
        Objects.requireNonNull(id, "id=null");
        if (size > 0) {
            if (size % 9 != 0) throw new IllegalStateException("size=" + size);
            rows = size % 9;
        } else if (rows > 0) {
            size = rows * 9;
        }
        if (size <= 0) throw new IllegalStateException("size=" + rows);
        if (rows > 6) throw new IllegalStateException("rows=" + rows);
        if (slots == null) slots = Collections.emptyList();
        for (Slot slot : slots) {
            try {
                slot.validate();
            } catch (Exception e) {
                throw new IllegalStateException("menu[" + id + "]", e);
            }
        }
        if (restrictions != null) {
            try {
                restrictions.validate();
            } catch (Exception e) {
                throw new IllegalStateException("menu[" + id + "]", e);
            }
        }
    }

    public Gui createGui(MenuPlugin plugin) {
        Gui gui = new Gui(plugin)
            .menu(this)
            .size(size)
            .title(Text.colorize(title));
        int lastIndex = 0;
        for (Slot slot : slots) {
            ItemStack itemStack;
            try {
                itemStack = slot.toItemStack();
            } catch (Exception e) {
                throw new IllegalStateException("menu[" + id + "]", e);
            }
            int index = slot.getIndex();
            if (index < 0) index = lastIndex++;
            gui.setItem(slot.getIndex(), itemStack, event -> slot.onClick(gui, event));
        }
        if (autoSounds) {
            gui.onOpen(event -> {
                    if (event.getPlayer() instanceof Player) {
                        Effects.open((Player) event.getPlayer());
                    }
                });
            gui.onClose(event -> {
                    if (event.getPlayer() instanceof Player) {
                        Effects.close((Player) event.getPlayer());
                    }
                });
            gui.onMisclick(event -> {
                    if (event.getWhoClicked() instanceof Player) {
                        Effects.fail((Player) event.getWhoClicked());
                    }
                });
        }
        return gui;
    }

    public Gui open(MenuPlugin plugin, Player player) {
        Gui gui = createGui(plugin);
        gui.open(player);
        return gui;
    }

    public void testRestrictions(Player player) throws Restrictions.Error {
        if (restrictions != null) restrictions.test(player);
    }
}
