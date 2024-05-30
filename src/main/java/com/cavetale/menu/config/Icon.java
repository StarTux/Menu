package com.cavetale.menu.config;

import com.cavetale.menu.util.Text;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Data
public final class Icon {
    String serialized;
    Material material;
    int amount = 1;
    boolean glow;
    boolean hide;
    String tooltip;
    List<String> tooltipLines;
    int loreWidth;

    public void validate() {
        if (amount < 1) amount = 1;
        if (serialized == null && material == null) {
            throw new IllegalStateException("material=null");
        }
        if (loreWidth == 0) loreWidth = Text.ITEM_LORE_WIDTH;
    }

    public ItemStack toItemStack() throws Exception {
        ItemStack itemStack;
        if (serialized != null) {
            byte[] bytes = Base64.getDecoder().decode(serialized);
            itemStack = ItemStack.deserializeBytes(bytes);
        } else if (material != null) {
            itemStack = new ItemStack(material, amount);
        } else {
            throw new IllegalStateException("material=null");
        }
        ItemMeta meta = itemStack.getItemMeta();
        if (glow) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, true); // ignoreLevelRestriction=true
        }
        if (hide) {
            meta.addItemFlags(ItemFlag.values());
        }
        if (tooltipLines != null && !tooltipLines.isEmpty()) {
            List<String> lines = tooltipLines.stream()
                .map(Text::colorize)
                .collect(Collectors.toList());
            meta.displayName(Component.text(lines.get(0)));
            meta.lore(lines.subList(1, lines.size()).stream()
                      .map(Component::text)
                      .collect(Collectors.toList()));
        } else if (tooltip != null) {
            String[] toks = tooltip.split("\n", 2);
            meta.displayName(Component.text(Text.colorize(toks[0])));
            if (toks.length == 2) {
                List<Component> lines = Text.wrapMultiline(Text.colorize(toks[1]), loreWidth).stream()
                    .map(Component::text)
                    .collect(Collectors.toList());
                meta.lore(lines);
            }
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
