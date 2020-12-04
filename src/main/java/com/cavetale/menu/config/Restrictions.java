package com.cavetale.menu.config;

import org.bukkit.entity.Player;

/**
 *
 */
public final class Restrictions {
    String permission;

    public static final class Error extends Exception {
        Error(final String msg) {
            super(msg);
        }
    }

    public void validate() { }

    public boolean test(Player player) throws Error {
        if (permission != null && !player.hasPermission(permission)) {
            throw new Error("Permission denied");
        }
        return true;
    }
}
