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

package me.flail.ThrowableFireballs.Handlers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.flail.ThrowableFireballs.ThrowableFireballs;

public class FireballDamage implements Listener {

	private ThrowableFireballs plugin = ThrowableFireballs.getPlugin(ThrowableFireballs.class);

	@EventHandler(priority = EventPriority.HIGH)
	public void onFireballDamage(EntityDamageByEntityEvent event) {

		FileConfiguration config = plugin.getConfig();

		boolean doesNaturalDamage = config.getBoolean("NaturalExplosion");

		if (doesNaturalDamage != true) {

			DamageCause damager = event.getCause();

			if (damager.equals(DamageCause.BLOCK_EXPLOSION) || damager.equals(DamageCause.ENTITY_EXPLOSION)) {

				String fbName = event.getDamager().getCustomName();

				if ((fbName != null) && fbName.equals("HolyBalls")) {

					double damage = config.getDouble("FireballDamage") * 2;

					event.setDamage(damage);

				}

			}

		}

	}

}
