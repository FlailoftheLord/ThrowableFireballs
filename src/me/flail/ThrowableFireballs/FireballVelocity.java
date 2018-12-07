/*
 *  Copyright (C) 2018 FlailoftheLord
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

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class FireballVelocity implements Listener {

	private ThrowableFireballs plugin = ThrowableFireballs.getPlugin(ThrowableFireballs.class);

	// this isn't working...
	@EventHandler
	public void onFireballToss(Projectile event) {

		ProjectileSource shooter = event.getShooter();

		if (shooter instanceof Player) {

			EntityType projectile = event.getType();

			if (projectile.equals(EntityType.FIREBALL)) {

				double fireballVelocity = plugin.getConfig().getDouble("FireballSpeed");

				Vector velocity = event.getVelocity();

				Vector newVelocity = velocity.multiply(fireballVelocity);

				event.setVelocity(newVelocity);

			}

		}

	}

}
