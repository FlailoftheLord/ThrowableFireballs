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

package me.flail.throwablefireballs.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.flail.throwablefireballs.ThrowableFireballs;

public class Tools {
	private static final char COLOR_CHAR = ChatColor.COLOR_CHAR;

	protected ThrowableFireballs plugin = ThrowableFireballs.getPlugin(ThrowableFireballs.class);

	private FileConfiguration config = plugin.getConfig();

	String prefix = config.getString("Prefix");
	String version = plugin.getDescription().getVersion();

	public String chat(String s) {

		String result = "";

		try {

			result = ChatColor.translateAlternateColorCodes('&',
					s.replace("%prefix%", prefix).replaceAll("%version%", version));

		} catch (Throwable e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage(
					ChatColor.RED + "ERROR with chat formatting! Send the above error to the plugin's author.");
		}
		return translateHex("&#", "", result);
	}

	public String translateHex(String startTag, String endTag, String message) {
		final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
		Matcher matcher = hexPattern.matcher(message);
		StringBuffer buffer = new StringBuffer(message.length() + (4 * 8));
		while (matcher.find()) {
			String group = matcher.group(1);
			matcher.appendReplacement(buffer, COLOR_CHAR + "x"
					+ COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
					+ COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
					+ COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5));
		}
		return matcher.appendTail(buffer).toString();
	}

	public void console(String msg) {
		plugin.console.sendMessage(chat("&7[ThrowableFireballs] " + msg));
	}

	/**
	 * Knocks all entities within the target radius backwards as naturally as
	 * possible.
	 *
	 * @param center
	 *                   the target entity at the center of the fireball hit.
	 * @param radius
	 *                   at which to check for entities.
	 * @return false if there are no entities nearby, true otherwise.
	 */
	public boolean setKnockback(Entity center, double radius) {

		Location target = center.getLocation();

		int maxHeight = plugin.getConfig().getInt("MaxJumpHeight");

		List<Entity> nearbyEntities = center.getNearbyEntities(radius, radius, radius);

		if ((nearbyEntities == null) || nearbyEntities.isEmpty()) {
			return false;
		}

		List<LivingEntity> validEntities = new ArrayList<>();

		for (Entity entity : nearbyEntities) {
			if (entity.isValid() && (entity instanceof LivingEntity)) {
				validEntities.add((LivingEntity) entity);
			}
		}

		for (LivingEntity entity : validEntities) {

			if ((entity instanceof Player)) {
				Player player = (Player) entity;
				if (player.isConversing() || player.isGliding()) {
					continue;
				}

			}

			/*
			Location ePos = entity.getLocation();
			double x = Math.abs(ePos.getX() - target.getX());
			double y = ePos.getY() - target.getY();
			double z = Math.abs(ePos.getZ() - target.getZ());
			*/

			double distance = (maxHeight - entity.getLocation().distance(target));

			Vector variantVel = entity.getLocation().getDirection().multiply(-1);

			variantVel = variantVel.setY(distance / (Math.PI * 1.67));

			entity.setVelocity(variantVel);

		}

		return true;
	}

	protected String removeChars(String message, String[] chars) {
		String modified = message;
		for (String c : chars) {
			modified = modified.replace(c, "");
		}

		return modified;
	}

}