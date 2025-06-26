package me.flail.throwablefireballs.handlers.elytra;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class PlayerHitListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerHitByFireball(ProjectileHitEvent event) {
        Entity target = event.getHitEntity();
        if ((target != null) && (target instanceof Player)) {
            Player player = (Player) target;
            Entity projectile = event.getEntity();
            if (player.isGliding()) {
                if (((projectile.getCustomName() != null) && projectile.getCustomName().equals("HolyBalls")) || projectile.hasMetadata("HolyBalls")) {
                    player.setGliding(false);
                }
            }
        }
    }
}
