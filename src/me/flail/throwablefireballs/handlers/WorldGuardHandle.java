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

import org.bukkit.Location;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.flail.throwablefireballs.tools.Tools;

public class WorldGuardHandle extends Tools {

	WorldGuard wg = WorldGuard.getInstance();

	public static final StateFlag FIREBALL_EXPLOSION = new StateFlag("fireballs-block-damage", true);
	public static final StateFlag FIREBALL_DAMAGE = new StateFlag("fireballs-entity-damage", true);

	public boolean registerFlags() {
		try {
			FlagRegistry flags = wg.getFlagRegistry();

			flags.register(FIREBALL_EXPLOSION);
			console("Registered custom WorldGuard flag  &eFIREBALLS-BLOCK-DAMAGE");
			flags.register(FIREBALL_DAMAGE);
			console("Registered custom WorldGuard flag  &eFIREBALLS-ENTITY-DAMAGE");
			console(" ");

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			console("&cERROR while registering flags to WorldGuard.");
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
		RegionQuery query = wg.getPlatform().getRegionContainer().createQuery();
		ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(location));

		return set.testState(null, flag);
	}

}
