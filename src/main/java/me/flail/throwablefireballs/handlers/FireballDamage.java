package me.flail.throwablefireballs.handlers;

import me.flail.throwablefireballs.ThrowableFireballs;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class FireballDamage implements Listener {

    private ThrowableFireballs plugin = ThrowableFireballs.getPlugin(ThrowableFireballs.class);

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFireballDamage(EntityDamageByEntityEvent e) {
        e.setDamage(fireballDamage(e.getEntity(), e.getDamager(), e.getCause(), e.getDamage()));
    }

    public double fireballDamage(Entity damaged, Entity damager, DamageCause cause, double baseDamage) {
        FileConfiguration config = plugin.conf;

        boolean doesNaturalDamage = config.getBoolean("NaturalExplosion");

        if (!doesNaturalDamage) {
            if (cause.equals(DamageCause.BLOCK_EXPLOSION) || cause.equals(DamageCause.ENTITY_EXPLOSION) || cause.equals(DamageCause.PROJECTILE)) {
                String fbName = damager.getCustomName();

                if (((fbName != null) && fbName.equals("HolyBalls")) || damager.hasMetadata("HolyBalls")) {
                    if (plugin.isWorldGuard && !plugin.worldguard.canDamageEntity(damaged.getLocation())) {
                        return 0;
                    }

                    return config.getDouble("FireballDamage", 2.0);
                }
            }
        }

        return baseDamage;
    }
}
