package me.flail.throwablefireballs.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import me.flail.throwablefireballs.ThrowableFireballs;
import me.flail.throwablefireballs.tools.Tools;
import net.kyori.adventure.text.Component;
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

public class FireballExplosion extends Tools implements Listener {

    ThrowableFireballs plugin;

    private static final String fbMetadata = "HolyBalls";

    @EventHandler(priority = EventPriority.HIGHEST)
    public void fireballExplode(ProjectileHitEvent event) {
        if (Objects.isNull(event.getHitBlock())) return;

        Entity entity = event.getEntity();

        if (entity instanceof Fireball fireball) {
            plugin = JavaPlugin.getPlugin(ThrowableFireballs.class);
            FileConfiguration config = plugin.conf;

            boolean doesNaturalDamage = config.getBoolean("NaturalExplosion", true);

            if (!doesNaturalDamage) {
                int power = config.getInt("FireballExplosionPower", 0);
                boolean doesFire = config.getBoolean("FireballSetFire");

                if (!fireball.hasMetadata(fbMetadata)) {
                    return;
                }

                fireball.setYield(0);

                new Tools().setKnockback(fireball, power * 1.4);

                if (plugin.isWorldGuard && !plugin.worldguard.canDamageBlock(event.getHitBlock().getLocation())) {
                    fireball.setIsIncendiary(false);
                    return;
                }

                Location fbLoc = event.getHitBlock().getLocation();
                World fbWorld = fireball.getWorld();

                if (power > 0) {
                    fbWorld.createExplosion(fbLoc, power, doesFire, true, fireball);
                }

                if (!fireball.getPassengers().isEmpty()) {
                    List<Entity> passengers = fireball.getPassengers();
                    if (passengers.getFirst() instanceof ArmorStand aStand) {
                        ItemStack item = aStand.getEquipment().getHelmet();
                        aStand.remove();

                        Item droppedItem = fbWorld.dropItemNaturally(fbLoc, item);

                        droppedItem.setPickupDelay(Integer.MAX_VALUE);
                        droppedItem.customName(Component.empty().content(new Tools().chat(config.getString("FireballName", "&6Fireball"))));
                        droppedItem.customName();
                        droppedItem.setCustomNameVisible(true);

                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, droppedItem::remove, 32L);
                    }
                }
            }
        }
    }

    @EventHandler
    public void blockBoom(EntityExplodeEvent event) {
        Entity e = event.getEntity();
        String custom_name = Objects.requireNonNull(e.customName()).toString();

        if (e.hasMetadata(fbMetadata) || (custom_name.equals(fbMetadata)) || e.hasMetadata("Fireballed")) {
            List<String> immuneBlocks = new ArrayList<>(plugin.immuneBlocks);
            List<String> immuneKeys = new ArrayList<>(plugin.immuneBlockKeys);
            for (String s : immuneBlocks.toArray(new String[] {})) {
                immuneBlocks.remove(s);
                immuneBlocks.add(s.toUpperCase());
            }

            for (Block block : event.blockList().toArray(new Block[] {})) {
                String type = block.getType().toString();

                if (immuneBlocks.contains(type)) {
                    event.blockList().remove(block);
                } else {
                    for (String s : immuneKeys) {
                        if (type.contains(s.toUpperCase())) {
                            event.blockList().remove(block);
                            break;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void blockBoom(BlockExplodeEvent event) {
        List<String> immuneBlocks = plugin.immuneBlocks;
        List<String> immuneKeys = plugin.immuneBlockKeys;
        for (String s : immuneBlocks.toArray(new String[] {})) {
            immuneBlocks.remove(s);
            immuneBlocks.add(s.toUpperCase());
        }

        for (Block block : event.blockList().toArray(new Block[] {})) {
            String type = block.getType().toString().toUpperCase();

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
