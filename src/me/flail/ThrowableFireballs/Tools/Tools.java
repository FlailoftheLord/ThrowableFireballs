/*
 *  Copyright (C) 2018-2019 FlailoftheLord
 *
 *  This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package me.flail.ThrowableFireballs.Tools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.flail.ThrowableFireballs.ThrowableFireballs;

public class Tools {

	private ThrowableFireballs plugin = ThrowableFireballs.getPlugin(ThrowableFireballs.class);

	private FileConfiguration config = plugin.getConfig();

	public String chat(String s) {

		String prefix = config.getString("Prefix");

		String version = plugin.getDescription().getVersion();

		String result = "";

		try {

			result = ChatColor.translateAlternateColorCodes('&',
					s.replace("%prefix%", prefix).replaceAll("%version%", version));

		} catch (Throwable e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage(
					ChatColor.RED + "ERROR with chat formatting! Send the above error to the plugin's author.");
		}
		return result;
	}

	/**
	 * Knocks all entities within the target radius backwards as naturally as
	 * possible.
	 *
	 * @param center the target entity at the center of the fireball hit.
	 * @param radius at which to check for entities.
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
				if (player.isFlying()) {
					continue;
				}
				if (player.isConversing()) {
					continue;
				}
				if (player.isGliding()) {
					continue;
				}
			}

			double distance = (maxHeight - entity.getLocation().distance(target));

			double TWO_PI = 1.76 * Math.PI;

			Vector variantVel = entity.getLocation().getDirection().multiply(-1);
			variantVel = variantVel.setY(distance / TWO_PI);

			entity.setVelocity(variantVel);

		}

		return true;
	}

}