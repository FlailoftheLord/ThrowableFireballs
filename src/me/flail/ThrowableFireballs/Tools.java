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

package me.flail.ThrowableFireballs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

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
			Bukkit.getConsoleSender()
					.sendMessage(ChatColor.RED + "ERROR Translating chat codes in plugin: ThrowableFireballs!!");
		}
		return result;
	}

	/**
	 * Knocks all entities within the target radius backwards as naturally as
	 * possible.
	 *
	 * @param target the target location where the fireball hit.
	 * @return false if there are no entities nearby, true otherwise.
	 */
	public boolean setKnockback(Location target) {

		int maxHeight = plugin.getConfig().getInt("MaxJumpHeight");

		Entity testSubject = target.getWorld().spawnEntity(target, EntityType.SILVERFISH);

		List<Entity> nearbyEntities = testSubject.getNearbyEntities(maxHeight, maxHeight, maxHeight);

		if (testSubject.isValid()) {
			LivingEntity e = (LivingEntity) testSubject;
			e.setAI(false);
			e.damage(69);
		}

		if ((nearbyEntities == null) || nearbyEntities.isEmpty()) {
			return false;
		}

		List<LivingEntity> validEntities = new ArrayList<>();

		for (Entity entity : nearbyEntities) {
			if (entity.isValid()) {
				validEntities.add((LivingEntity) entity);
			}
		}

		for (LivingEntity entity : validEntities) {

			double distance = (maxHeight - target.distance(entity.getLocation())) * -1;

			Vector newVelocity = (entity.getLocation().toVector().subtract(target.toVector())).multiply(distance);
			entity.setVelocity(newVelocity);

		}

		return true;
	}

}