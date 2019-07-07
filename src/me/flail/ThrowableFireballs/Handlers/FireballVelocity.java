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

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

public class FireballVelocity implements Listener {

	@EventHandler
	public void playerThrow(ProjectileLaunchEvent event) {
		Entity entity = event.getEntity();

		if ((entity instanceof Fireball) && entity.isValid()) {

			LivingEntity ball = (LivingEntity) entity;

			if (ball.getCustomName() != null) {
				String name = ball.getCustomName();
				if (name.equals("HolyBalls")) {

					for (Entity e : ball.getNearbyEntities(1, 1, 1)) {
						if (e instanceof Player) {

							List<Block> lineofsight = ((Player) e).getLineOfSight(null, 1);

							Vector vel = ball.getVelocity();

							ball.teleport(lineofsight.get(0).getLocation());

							ball.setVelocity(vel);

							break;
						}

					}

				}

			}

		}
	}

}
