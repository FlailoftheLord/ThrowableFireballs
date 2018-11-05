package me.flail.ThrowableFireballs;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FireballDamage implements Listener {

	private ThrowableFireballs plugin = ThrowableFireballs.getPlugin(ThrowableFireballs.class);

	@EventHandler
	public void onFireballDamage(EntityDamageByEntityEvent event) {

		FileConfiguration config = plugin.getConfig();

		Entity damager = event.getDamager();

		if (damager instanceof Fireball) {

			String fbName = damager.getCustomName();

			if (fbName.equals("HolyBalls")) {

				double damage = config.getDouble("FireballDamage") * 2;

				event.setDamage(damage);

			}

		}

	}

}
