package me.flail.ThrowableFireballs;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class FireballExplosion implements Listener {

	private ThrowableFireballs plugin;

	private FileConfiguration config;

	@EventHandler
	public void fireballExplode(ProjectileHitEvent event) {

		plugin = ThrowableFireballs.getPlugin(ThrowableFireballs.class);

		config = plugin.getConfig();

		Entity entity = event.getEntity();

		if (entity instanceof Fireball) {

			String fbName = entity.getCustomName();

			if (fbName.equals("HolyBalls")) {

				float power = config.getLong("FireballExplosionPower");

				boolean doesFire = config.getBoolean("FireballSetFire");

				Fireball fireball = (Fireball) entity;

				Location fbLoc = fireball.getLocation();

				World fbWorld = fireball.getWorld();

				fbWorld.createExplosion(fbLoc, power, doesFire);

			}

		}

	}

}
