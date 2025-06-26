package me.flail.throwablefireballs.handlers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.SimpleFlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.flail.throwablefireballs.tools.Tools;
import org.bukkit.Location;

public class WorldGuardHandle extends Tools {

    public static final StateFlag FIREBALL_EXPLOSION = new StateFlag("fireballs-block-damage", true);
    public static final StateFlag FIREBALL_DAMAGE = new StateFlag("fireballs-entity-damage", true);

    public boolean registerFlags() {
        try {
            SimpleFlagRegistry flags = (SimpleFlagRegistry) plugin.wg.getFlagRegistry();
            flags.setInitialized(false);

            flags.register(FIREBALL_EXPLOSION);
            console("Registered custom WorldGuard flag  &eFIREBALLS-BLOCK-DAMAGE");
            flags.register(FIREBALL_DAMAGE);
            console("Registered custom WorldGuard flag  &eFIREBALLS-ENTITY-DAMAGE");
            console(" ");

            return true;
        } catch (Exception e) {
            // e.printStackTrace();
            console("&cError occured while registering flags to WorldGuard.");
            console("&cThis is most likely due to WorldGuard not loading in time.");
            console("&7Trying again in 5 seconds...");
            return false;
        }
    }

    public boolean canDamageBlock(Location location) {
        return check(location, FIREBALL_EXPLOSION);
    }

    public boolean canDamageEntity(Location location) {
        return check(location, FIREBALL_DAMAGE);
    }

    protected boolean check(Location location, StateFlag flag) {
        WorldGuard wg = WorldGuard.getInstance();
        RegionQuery query = wg.getPlatform().getRegionContainer().createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(location));

        return set.testState(null, flag);
    }
}
