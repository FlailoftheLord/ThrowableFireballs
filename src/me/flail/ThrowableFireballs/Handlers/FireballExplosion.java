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

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.flail.ThrowableFireballs.ThrowableFireballs;
import me.flail.ThrowableFireballs.Tools.Tools;

public class FireballExplosion implements Listener {

	private ThrowableFireballs plugin = JavaPlugin.getPlugin(ThrowableFireballs.class);

	private FileConfiguration config = plugin.getConfig();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void fireballExplode(ProjectileHitEvent event) {

		plugin = JavaPlugin.getPlugin(ThrowableFireballs.class);

		config = plugin.getConfig();

		Entity entity = event.getEntity();

		boolean doesNaturalDamage = config.getBoolean("NaturalExplosion", true);

		if (!doesNaturalDamage) {

			if (entity instanceof Fireball) {



				FileConfiguration config = plugin.getConfig();

				int power = config.getInt("FireballExplosionPower", 0);
				boolean doesFire = config.getBoolean("FireballSetFire");

				Fireball fireball = (Fireball) entity;

				if (!fireball.hasMetadata("HolyBalls")) {
					return;
				}

				plugin.tossed = true;

				Location fbLoc = event.getHitBlock().getLocation();
				World fbWorld = fireball.getWorld();

				if (power > 0) {
					fbWorld.createExplosion(fbLoc, power, doesFire);

				}

				new Tools().setKnockback(fireball, power * 1.2);


				if (!fireball.getPassengers().isEmpty()) {
					List<Entity> passengers = fireball.getPassengers();
					if (passengers.get(0) instanceof ArmorStand) {
						ArmorStand aStand = (ArmorStand) passengers.get(0);

						ItemStack item = aStand.getHelmet();
						aStand.remove();

						Item droppedItem = fbWorld.dropItemNaturally(fbLoc, item);

						droppedItem.setPickupDelay(Integer.MAX_VALUE);
						droppedItem.setCustomName(new Tools().chat(config.getString("FireballName", "&6Fireball")));
						droppedItem.setCustomNameVisible(true);

						plugin.server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {

							droppedItem.remove();
						}, 32L);

					}

				}

			}

		}

	}

	// @EventHandler
	public void blockBoom(EntityExplodeEvent event) {

		Entity e = event.getEntity();

		if (e.hasMetadata("HolyBalls") || ((e.getCustomName() != null) && e.getCustomName().equals("HolyBalls"))) {

			if (e.hasMetadata("Fireballed")) {

				List<String> immuneBlocks = plugin.getConfig().getStringList("ImmuneBlocks");
				List<String> immuneKeys = plugin.getConfig().getStringList("ImmuneBlockKeywords");
				for (String s : immuneBlocks.toArray(new String[] {})) {
					immuneBlocks.remove(s);
					immuneBlocks.add(s.toUpperCase());
				}

				for (Block block : event.blockList().toArray(new Block[] {})) {

					if (block.hasMetadata("Fireballed")) {
						plugin.console.sendMessage("Fireballed Block!");

						String type = block.getType().toString();

						if (immuneBlocks.contains(type)) {
							event.blockList().remove(block);
							continue;
						}

						for (String s : immuneKeys) {
							if (type.contains(s.toUpperCase())) {
								event.blockList().remove(block);
								break;
							}
						}

						continue;
					}

					event.blockList().clear();
					break;
				}

			}

		}

	}


	@EventHandler
	public void blockBoom(BlockExplodeEvent event) {

		List<String> immuneBlocks = plugin.getConfig().getStringList("ImmuneBlocks");
		List<String> immuneKeys = plugin.getConfig().getStringList("ImmuneBlockKeywords");
		for (String s : immuneBlocks.toArray(new String[] {})) {
			immuneBlocks.remove(s);
			immuneBlocks.add(s.toUpperCase());
		}

		for (Block block : event.blockList().toArray(new Block[] {})) {

			String type = block.getType().toString();

			if (immuneBlocks.contains(type)) {
				event.blockList().remove(block);
			}

			for (String s : immuneKeys) {
				if (type.contains(s.toUpperCase())) {
					event.blockList().remove(block);
				}
			}

		}

	}

}
