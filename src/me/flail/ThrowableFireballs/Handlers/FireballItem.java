/*
 * Copyright (C) 2018 FlailoftheLord
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.flail.ThrowableFireballs.Handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.flail.ThrowableFireballs.ThrowableFireballs;
import me.flail.ThrowableFireballs.Tools.Tools;

public class FireballItem {

	private ThrowableFireballs plugin = ThrowableFireballs.getPlugin(ThrowableFireballs.class);

	private Tools tools = new Tools();

	public ItemStack fireball() {

		FileConfiguration config = plugin.getConfig();

		Material fireballType = Material
				.matchMaterial(config.get("FireballItem", "FIRE_CHARGE").toString().toUpperCase().replaceAll("[^A-Z\\_]", ""));
		String fbName = config.getString("FireballName");

		List<String> fbLoreList = config.getStringList("Lore");

		ArrayList<String> fbLore = new ArrayList<>();

		for (String l : fbLoreList) {
			fbLore.add(tools.chat(l));
		}

		ItemStack fb = new ItemStack(fireballType);

		ItemMeta fbMeta = fb.getItemMeta();

		fbMeta.setLore(fbLore);
		fbMeta.setDisplayName(tools.chat(fbName));
		fbMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		fb.setItemMeta(fbMeta);

		return fb;

	}

}