package com.cavetale.menu;

import com.cavetale.core.font.DefaultFont;
import com.cavetale.core.font.GuiOverlay;
import com.cavetale.core.menu.MenuItemClickEvent;
import com.cavetale.core.menu.MenuItemEntry;
import com.cavetale.core.menu.MenuItemEvent;
import com.cavetale.mytems.util.Gui;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.TextColor.color;

@RequiredArgsConstructor
public final class Menu {
    private static final int SIZE = 6 * 9;
    private static final int[] HOTBAR_SLOTS = {4, 3, 5, 2, 6, 1, 7, 0, 8};
    private static final int[] MENU_SLOTS = new int[5 * 9];
    private final Player player;
    private List<MenuItemEntry> entries = new ArrayList<>();

    static {
        for (int i = 0; i < MENU_SLOTS.length; i += 1) {
            MENU_SLOTS[i] = i + 9;
        }
    }

    public static Menu openMenu(Player player) {
        final Menu menu = new Menu(player);
        menu.load();
        menu.open();
        return menu;
    }

    private void load() {
        final MenuItemEvent event = new MenuItemEvent(player);
        event.callEvent();
        entries.addAll(event.getEntries());
    }

    public void load(List<MenuItemEntry> list) {
        entries.addAll(list);
    }

    protected void open() {
        Collections.sort(entries);
        int topBarIndex = 0;
        int menuIndex = 0;
        final Gui gui = new Gui()
            .size(SIZE)
            .title(textOfChildren(DefaultFont.CAVETALE, text(" Main Menu")))
            .layer(GuiOverlay.BLANK, color(0x003056))
            .layer(GuiOverlay.TOP_BAR, color(0x15151D));
        for (MenuItemEntry entry : entries) {
            final int index;
            final int slot;
            switch (entry.getPriority()) {
            case SERVER:
            case HOTBAR:
            case NOTIFICATION: {
                index = topBarIndex++;
                if (index >= HOTBAR_SLOTS.length) {
                    continue;
                }
                slot = HOTBAR_SLOTS[index];
                break;
            }
            default:
                index = menuIndex++;
                if (index >= MENU_SLOTS.length) {
                    continue;
                }
                slot = MENU_SLOTS[index];
            }
            gui.setItem(slot, entry.getIcon(), click -> onClick(click, entry));
            if (entry.hasHighlightColor()) {
                gui.highlight(slot, entry.getHighlightColor());
            }
        }
        gui.open(player);
    }

    private void onClick(InventoryClickEvent click, MenuItemEntry entry) {
        if (!click.isLeftClick()) {
            return;
        }
        if (entry.hasCommand()) {
            player.performCommand(entry.getCommand());
        }
        new MenuItemClickEvent(player, entry).callEvent();
    }
}
