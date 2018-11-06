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

import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class FireballThrow implements Listener {

	private ThrowableFireballs plugin;

	private FileConfiguration config;

	private Tools tools = new Tools();

	private HashMap<String, Long> cooldown = new HashMap<String, Long>();

	@EventHandler
	public void onPlayerLaunch(PlayerInteractEvent event) {

		plugin = ThrowableFireballs.getPlugin(ThrowableFireballs.class);

		config = plugin.getConfig();

		Player player = event.getPlayer();
		Action a = event.getAction();

		if ((a == Action.RIGHT_CLICK_BLOCK) || (a == Action.RIGHT_CLICK_AIR)) {

			int cooldownTime = config.getInt("Cooldown");

			ItemStack fbItem = new FireballItem().fireball();

			if (fbItem != null) {

				ItemStack fb = player.getInventory().getItemInMainHand();
				ItemStack fbo = player.getInventory().getItemInOffHand();
				boolean fboEnabled = config.getBoolean("AllowOffhandThrowing");
				ItemStack i = event.getItem();
				int iA;
				if (i != null) {
					iA = i.getAmount();
					fbItem.setAmount(iA);
				}

				if (fb.equals(fbItem) || (fbo.equals(fbItem) && fboEnabled)) {

					Player p = event.getPlayer();

					boolean isInNoThrowWorld = false;

					String pWorld = p.getWorld().getName().toLowerCase();

					List<String> noThrowWorlds = config.getStringList("NoThrowZones");

					for (String world : noThrowWorlds) {

						if (world.equalsIgnoreCase(pWorld)) {

							isInNoThrowWorld = true;
							break;

						} else {

							isInNoThrowWorld = false;

						}

					}

					if (isInNoThrowWorld) {

						String noThrowingMessage = config.getString("NoThrowZoneMessage");

						p.sendMessage(tools.chat(noThrowingMessage));

					} else {

						Material pb = p.getLocation().add(0, -1, 0).getBlock().getType();
						Material pbx1 = p.getLocation().add(1, -1, 0).getBlock().getType();
						Material pbz1 = p.getLocation().add(0, -1, 1).getBlock().getType();
						Material pbx2 = p.getLocation().add(-1, -1, 0).getBlock().getType();
						Material pbz2 = p.getLocation().add(0, -1, -1).getBlock().getType();
						Material air = Material.AIR;
						Material lookedAtBlock = p.getTargetBlock(null, 5).getType();

						int consume;

						int amount;

						if (p.hasPermission("fireballs.throw")) {

							Material offHandItem = player.getInventory().getItemInOffHand().getType();

							consume = 1;

							if (p.hasPermission("fireballs.infinite")) {
								consume = 0;
							}

							amount = i.getAmount();
							if (amount < consume) {
								return;
							}

							if (amount == consume) {
								p.getInventory().removeItem(new ItemStack[] { i });
							} else {
								i.setAmount(amount - consume);
							}

							if (cooldown.containsKey(p.getName())) {

								long timeLeft = ((cooldown.get(p.getName()) / 1000) + cooldownTime)
										- (System.currentTimeMillis() / 1000);

								boolean sendCooldownMessage = config.getBoolean("CooldownMessageEnabled");

								String cooldownMessage = config.getString("CooldownMessage").replace("%cooldown%",
										timeLeft + "");

								if (timeLeft > 0) {
									if ((offHandItem == null) || (offHandItem == air)) {
										if (sendCooldownMessage) {
											player.sendMessage(tools.chat(cooldownMessage));
										}

									}
									return;
								}
							}

							cooldown.put(player.getName(), System.currentTimeMillis());

							if ((pb != air) || (pbx1 != air) || (pbx2 != air) || (pbz1 != air) || (pbz2 != air)) {
								float pp = p.getLocation().getPitch();

								if ((pp >= 70.0) && (pp <= 80.4) && (lookedAtBlock != air)) {
									doThrow(p.launchProjectile(Fireball.class));
									event.getPlayer()
											.setVelocity(event.getPlayer().getLocation().getDirection().multiply(3.8));
									event.getPlayer().setVelocity(new Vector(event.getPlayer().getVelocity().getX(),
											1.5D, event.getPlayer().getVelocity().getZ()));
								} else if ((pp >= 30.0) && (pp < 70.0) && (lookedAtBlock != air)) {
									doThrow(p.launchProjectile(Fireball.class));
									event.getPlayer()
											.setVelocity(event.getPlayer().getLocation().getDirection().multiply(-0.6));
									event.getPlayer().setVelocity(new Vector(event.getPlayer().getVelocity().getX(),
											1.5D, event.getPlayer().getVelocity().getZ()));
								} else if ((pp >= 80.5) && (lookedAtBlock != air)) {
									doThrow(p.launchProjectile(Fireball.class));
									event.getPlayer()
											.setVelocity(event.getPlayer().getLocation().getDirection().multiply(3.8));
									event.getPlayer().setVelocity(new Vector(event.getPlayer().getVelocity().getX(),
											2.5, event.getPlayer().getVelocity().getZ()));
								} else if ((pp <= 29.0) && (lookedAtBlock != air)) {
									doThrow(p.launchProjectile(Fireball.class));
									event.getPlayer()
											.setVelocity(event.getPlayer().getLocation().getDirection().multiply(-0.2));
									event.getPlayer().setVelocity(new Vector(event.getPlayer().getVelocity().getX(),
											0.2D, event.getPlayer().getVelocity().getZ()));
								} else {
									doThrow(p.launchProjectile(Fireball.class));
								}
							} else {
								doThrow(p.launchProjectile(Fireball.class));
							}

						} else {
							player.sendMessage(tools.chat(config.getString("NoPermissionMessage")));
						}

					}

				}

			} else {
				return;
			}

		}

	}

	private void doThrow(Fireball ball) {

		double fireballVelocity = plugin.getConfig().getDouble("FireballSpeed");

		if (ball.getType() == EntityType.FIREBALL) {
			ball.setIsIncendiary(false);
			ball.setYield(1);
			ball.setCustomName("HolyBalls");
			ball.setCustomNameVisible(false);
		}

		Vector velocity = ball.getVelocity();

		Vector newVelocity = velocity.multiply(fireballVelocity);

		ball.setVelocity(newVelocity);

	}

}
