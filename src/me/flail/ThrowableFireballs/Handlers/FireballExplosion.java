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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.flail.ThrowableFireballs.ThrowableFireballs;
import me.flail.ThrowableFireballs.Tools.Tools;

public class FireballExplosion implements Listener {

	private ThrowableFireballs plugin = JavaPlugin.getPlugin(ThrowableFireballs.class);

	private FileConfiguration config = plugin.getConfig();

	@EventHandler
	public void fireballExplode(ProjectileHitEvent event) {

		plugin = JavaPlugin.getPlugin(ThrowableFireballs.class);

		config = plugin.getConfig();

		Entity entity = event.getEntity();

		boolean doesNaturalDamage = config.getBoolean("NaturalExplosion");

		if (doesNaturalDamage != true) {

			if (entity instanceof Fireball) {

				String fbName = entity.getCustomName();

				if ((fbName != null) && (event.getHitBlock() != null) && fbName.equals("HolyBalls")) {

					FileConfiguration config = plugin.getConfig();

					float power = config.getLong("FireballExplosionPower");
					boolean doesFire = config.getBoolean("FireballSetFire");

					Fireball fireball = (Fireball) entity;

					Location fbLoc = event.getHitBlock().getLocation();

					World fbWorld = fireball.getWorld();

					plugin.tossed = true;
					fbWorld.createExplosion(fbLoc, power, doesFire);

					new Tools().setKnockback(fireball, power * 1.2);

					plugin.scheduler.scheduleSyncDelayedTask(plugin, () -> {
						plugin.tossed = false;
					}, 4);
				}

			}

		}

	}

	@EventHandler
	public void blockBoom(EntityExplodeEvent event) {

		Entity e = event.getEntity();

		List<Block> newList = new ArrayList<>();

		if ((e != null) && plugin.tossed) {
			if (e.hasMetadata("HolyBalls") || ((e.getCustomName() != null) && e.getCustomName().equals("HolyBalls"))) {

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
					} else {
						newList.add(block);
					}

					for (String s : immuneKeys) {
						if (type.contains(s.toUpperCase())) {
							event.blockList().remove(block);
						} else {
							newList.add(block);
						}
					}

				}

				event.blockList().clear();
				event.blockList().addAll(newList);

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
