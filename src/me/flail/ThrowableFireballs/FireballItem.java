package me.flail.ThrowableFireballs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FireballItem {

	private ThrowableFireballs plugin = ThrowableFireballs.getPlugin(ThrowableFireballs.class);

	private Tools tools = new Tools();

	public ItemStack fireball() {

		FileConfiguration config = plugin.getConfig();

		String fbName = config.getString("FireballName");

		List<String> fbLoreList = config.getStringList("Lore");

		ArrayList<String> fbLore = new ArrayList<String>();

		for (String l : fbLoreList) {
			fbLore.add(tools.chat(l));
		}

		ItemStack fb = new ItemStack(Material.FIRE_CHARGE);

		ItemMeta fbMeta = fb.getItemMeta();

		fbMeta.setLore(fbLore);
		fbMeta.setDisplayName(tools.chat(fbName));
		fbMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		fb.setItemMeta(fbMeta);

		return fb;

	}

}