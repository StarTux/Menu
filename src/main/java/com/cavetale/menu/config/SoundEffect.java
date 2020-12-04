package com.cavetale.menu.config;

import lombok.Data;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

@Data
public final class SoundEffect {
    Sound sound;
    SoundCategory category = SoundCategory.MASTER;
    float volume = 1.0f;
    float pitch = 1.0f;

    public void play(Player player) {
        player.playSound(player.getLocation(), sound, category, volume, pitch);
    }

    public void validate() {
        if (sound == null) throw new IllegalStateException("sound=null");
        if (category == null) category = SoundCategory.MASTER;
        if (volume < 0f) volume = 0f;
        if (volume > 1f) volume = 1f;
        if (pitch < 0f) pitch = 0f;
        if (pitch > 2f) pitch = 2f;
    }
}
