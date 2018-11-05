package me.flail.ThrowableFireballs;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class FireballVelocity implements Listener {

	private ThrowableFireballs plugin = ThrowableFireballs.getPlugin(ThrowableFireballs.class);

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
