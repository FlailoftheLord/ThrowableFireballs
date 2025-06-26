package me.flail.throwablefireballs.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.flail.throwablefireballs.ThrowableFireballs;
import me.flail.throwablefireballs.tools.Tools;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FireballItem extends Tools {

    private final ThrowableFireballs plugin = ThrowableFireballs.getPlugin(ThrowableFireballs.class);

    public ItemStack fireball() {
        FileConfiguration config = plugin.conf;

        Material fireballType = Material.matchMaterial(Objects.requireNonNull(config.get("FireballItem")).toString().replaceAll("[^A-Za-z_]", ""));

        if (fireballType == null) {
            console("&c the &fFireballItem &coption in your configuration is invalid, please check it's a valid Minecraft Material.");
            fireballType = Material.FIRE_CHARGE;
        }

        String fbName = config.getString("FireballName");

        List<String> fbLoreList = config.getStringList("Lore");

        ArrayList<String> fbLore = new ArrayList<>();

        for (String l : fbLoreList) {
            fbLore.add(chat(l));
        }

        ItemStack fb = new ItemStack(fireballType);

        ItemMeta fbMeta = fb.getItemMeta();

        fbMeta.setLore(fbLore);
        fbMeta.displayName(Component.empty().content(chat(fbName)));
        fbMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        fb.setItemMeta(fbMeta);

        return this.addTag(fb);
    }
}
