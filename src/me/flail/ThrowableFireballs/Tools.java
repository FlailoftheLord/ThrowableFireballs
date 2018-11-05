package me.flail.ThrowableFireballs;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class Tools {

	private ThrowableFireballs plugin = ThrowableFireballs.getPlugin(ThrowableFireballs.class);

	public String chat(String s) {
		FileConfiguration config = plugin.getConfig();

		String prefix = config.getString("Prefix");

		String result = "";

		try {

			result = ChatColor.translateAlternateColorCodes('&', s.replace("%prefix%", prefix));

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return result;
	}

}