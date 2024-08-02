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

package me.flail.throwablefireballs.handlers;

import java.util.HashMap;
import java.util.List;

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

import me.flail.throwablefireballs.ThrowableFireballs;
import me.flail.throwablefireballs.tools.Tools;

public class FireballThrow extends Tools implements Listener {

	private ThrowableFireballs plugin = JavaPlugin.getPlugin(ThrowableFireballs.class);

	private FileConfiguration config;

	private Tools tools = new Tools();

	private HashMap<String, Long> cooldown = new HashMap<>();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerThrow(PlayerInteractEvent event) {

		config = plugin.conf;

		Player player = event.getPlayer();
		Action a = event.getAction();

		if ((a == Action.RIGHT_CLICK_BLOCK) || (a == Action.RIGHT_CLICK_AIR)) {

			if (plugin.disabledGamemodes.contains(player.getGameMode()))
				return;

			ItemStack fb = player.getInventory().getItemInMainHand();
			ItemStack fbo = player.getInventory().getItemInOffHand();
			ItemStack item = event.getItem();

			boolean allowFirecharge = config.getBoolean("UseFirecharge");
			boolean fboEnabled = config.getBoolean("AllowOffhandThrowing");

			double cooldownTime = config.getDouble("Cooldown");

			if ((allowFirecharge && fb.getType().equals(Material.FIRE_CHARGE))
					|| (allowFirecharge && fbo.getType().equals(Material.FIRE_CHARGE)) && fboEnabled || this.isFireball(fb)
					|| (this.isFireball(fbo) && fboEnabled)) {

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

							double timeLeft = Math.ceil((cooldown.get(player.getName()) / 1000D + cooldownTime)
									- (System.currentTimeMillis() / 1000D));

							boolean sendCooldownMessage = config.getBoolean("CooldownMessageEnabled", false);

							String cooldownMessage = config.getString("CooldownMessage").replace("%cooldown%",
									timeLeft + "");

							if (timeLeft > 0) {
								if (((onHandItem != null))
										|| ((offHandItem == null))) {
									if (sendCooldownMessage) {
										player.sendMessage(tools.chat(cooldownMessage));
										consume = 0;
									}

								}
								return;
							}

						}

						if (player.hasPermission("fireballs.infinite"))
							consume = 0;

						if (item != null && item.getAmount() < consume)
							return;

						if (item != null && item.getAmount() == consume)
							player.getInventory().removeItem(new ItemStack(item));
						else if (item != null)
							item.setAmount(item.getAmount() - consume);

						cooldown.put(player.getName(), System.currentTimeMillis());

						this.throwBall(player);

					} else
						player.sendMessage(tools.chat(
								config.getString("NoPermissionMessage", "%prefix% &cYou don't have permission!")));

				}

			}

		}

	}

	public Fireball throwBall(Player player) {
		Fireball fireball = player.launchProjectile(Fireball.class);

		World world = fireball.getWorld();

		config = plugin.conf;

		boolean doesNaturalDamage = config.getBoolean("NaturalExplosion", true);


		fireball.setIsIncendiary(doesNaturalDamage);
		if (!doesNaturalDamage) {
			fireball.setYield(0F);
		}
		fireball.setCustomName("HolyBalls");
		fireball.setMetadata("HolyBalls", new FixedMetadataValue(plugin, "fireball"));
		fireball.setCustomNameVisible(false);

		String itemType = config.getString("FireballItem", "FIRE_CHARGE");
		if (!itemType.equalsIgnoreCase("fire_charge")) {
			ArmorStand aStand = (ArmorStand) fireball.getWorld().spawnEntity(fireball.getLocation(), EntityType.ARMOR_STAND);

			aStand.setGravity(false);
			aStand.setBasePlate(false);
			aStand.setInvulnerable(true);
			aStand.setVisible(false);
			aStand.getEquipment().setHelmet(new ItemStack(Material.matchMaterial(itemType)));

			aStand.setRemoveWhenFarAway(true);

			fireball.addPassenger(aStand);
		}

		return fireball;
	}

}
