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

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import me.flail.ThrowableFireballs.ThrowableFireballs;
import me.flail.ThrowableFireballs.Tools.Tools;

public class FireballThrow implements Listener {

	private ThrowableFireballs plugin = JavaPlugin.getPlugin(ThrowableFireballs.class);

	private FileConfiguration config;

	private Tools tools = new Tools();

	private HashMap<String, Long> cooldown = new HashMap<>();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerThrow(PlayerInteractEvent event) {

		config = plugin.getConfig();

		Player player = event.getPlayer();
		Action a = event.getAction();

		if ((a == Action.RIGHT_CLICK_BLOCK) || (a == Action.RIGHT_CLICK_AIR)) {

			boolean allowFirecharge = config.getBoolean("UseFirecharge");
			boolean fboEnabled = config.getBoolean("AllowOffhandThrowing");

			double cooldownTime = config.getDouble("Cooldown");

			ItemStack fbItem = new FireballItem().fireball();

			if (fbItem != null) {

				ItemStack fb = player.getInventory().getItemInMainHand();
				ItemStack fbo = player.getInventory().getItemInOffHand();

				ItemStack item = event.getItem();
				if (item != null) {
					fbItem.setAmount(item.getAmount());
				}

				if ((allowFirecharge && fb.getType().equals(Material.FIRE_CHARGE))
						|| (allowFirecharge && fbo.getType().equals(Material.FIRE_CHARGE)) || fb.equals(fbItem)
						|| (fbo.equals(fbItem) && fboEnabled)) {

					boolean isInNoThrowWorld = false;

					event.setCancelled(true);

					String pWorld = player.getWorld().getName().toLowerCase();
					List<String> noThrowWorlds = config.getStringList("NoThrowZones");

					for (String world : noThrowWorlds) {

						if (world.equalsIgnoreCase(pWorld)) {

							isInNoThrowWorld = true;
							break;

						}

					}

					if (isInNoThrowWorld) {

						String noThrowingMessage = config.getString("NoThrowZoneMessage");

						player.sendMessage(tools.chat(noThrowingMessage));

					} else {
						int consume = 1;

						if (player.hasPermission("fireballs.throw")) {

							Material onHandItem = fb.getType();
							Material offHandItem = fbo.getType();

							if (cooldown.containsKey(player.getName()) && !player.hasPermission("fireballs.bypass")) {

								double timeLeft = ((cooldown.get(player.getName()) / 1000) + cooldownTime)
										- (System.currentTimeMillis() / 1000);

								boolean sendCooldownMessage = config.getBoolean("CooldownMessageEnabled", false);

								String cooldownMessage = config.getString("CooldownMessage").replace("%cooldown%",
										timeLeft + "");

								if (timeLeft > 0) {
									if (((offHandItem == null) && (onHandItem != null))
											|| ((onHandItem == null) && (offHandItem == null))) {
										if (sendCooldownMessage) {
											player.sendMessage(tools.chat(cooldownMessage));
											consume = 0;
										}

									}
									return;
								}

							}

							if (player.hasPermission("fireballs.infinite")) {
								consume = 0;
							}

							if (item.getAmount() < consume) {
								return;
							}

							if (item.getAmount() == consume) {
								player.getInventory().removeItem(new ItemStack(item));
							} else {
								item.setAmount(item.getAmount() - consume);
							}

							cooldown.put(player.getName(), System.currentTimeMillis());

							this.throwBall(player);

						} else {
							player.sendMessage(tools.chat(
									config.getString("NoPermissionMessage", "%prefix% &cYou don't have permission!")));
						}

					}

				}

			}

		}

	}

	public Fireball throwBall(Player player) {
		Fireball fireball = player.launchProjectile(Fireball.class);

		fireball.setYield(0F);
		World world = fireball.getWorld();
		Vector velocity = fireball.getVelocity();
		fireball.remove();

		config = plugin.getConfig();

		double fireballVelocity = config.getDouble("FireballSpeed", 1.6);
		boolean doesNaturalDamage = config.getBoolean("NaturalExplosion", false);

		Vector newVelocity = velocity.multiply(fireballVelocity);

		Location newLocation = player.getLineOfSight(null, 2).get(1).getLocation()
				.setDirection(player.getLocation().getDirection());

		Fireball ball = world.spawn(newLocation, fireball.getClass());

		ball.setIsIncendiary(doesNaturalDamage);
		ball.setYield(0F);
		ball.setCustomName("HolyBalls");
		ball.setMetadata("HolyBalls", new FixedMetadataValue(plugin, "fireball"));
		ball.setCustomNameVisible(false);

		// fireball.setDirection(player.getLocation().getDirection());
		fireball.setVelocity(newVelocity);


		String itemType = config.getString("FireballItem", "FIRE_CHARGE");
		if (!itemType.equalsIgnoreCase("fire_charge")) {
			ArmorStand aStand = (ArmorStand) ball.getWorld().spawnEntity(ball.getLocation(), EntityType.ARMOR_STAND);

			aStand.setGravity(false);
			aStand.setBasePlate(false);
			aStand.setInvulnerable(true);
			aStand.setVisible(false);
			aStand.setHelmet(new ItemStack(Material.matchMaterial(itemType)));

			aStand.setRemoveWhenFarAway(true);

			ball.addPassenger(aStand);
		}

		return ball;
	}

}
