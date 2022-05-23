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

package me.flail.throwablefireballs.config;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.flail.throwablefireballs.tools.Tools;

public class Config extends Tools {

	/**
	 * Get the configuration file.
	 *
	 * @return config file formatted as {@link FileConfiguration}
	 */
	public static FileConfiguration get() {
		FileConfiguration config = new YamlConfiguration();
		File file = new File("plugins/ThrowableFireballs/config.yml");
		try {
			config.load(file);
		} catch (Throwable t) {
		}

		return config;
	}

	public String getHeader() {
		return confHeader;
	}

	public Map<String, Map<String, Object>> getConfigurationOptions() {
		return new LinkedHashMap<String, Map<String, Object>>(configurationMappings);
	}

	private String confHeader = "-----------------------------------------------------------------\r\n" +
			"==================================================================#\r\n" +
			"                                                                  #\r\n" +
			"                 Plugin by FlailoftheLord.                        #\r\n" +
			"        -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                   #\r\n" +
			"   ______               __        _____                           #\r\n" +
			"   |       |           /  \\         |        |                    #\r\n" +
			"   |__     |          /____\\        |        |                    #\r\n" +
			"   |       |         /      \\       |        |                    #\r\n" +
			"   |       |_____   /        \\    __|__      |______              #\r\n" +
			"                                                                  #\r\n" +
			"==================================================================#\r\n" +
			"-----------------------------------------------------------------\r\n" +
			"\r\n";

	private Map<String, Map<String, Object>> configurationMappings = new LinkedHashMap<>();

	public void updateMappings() {
		for (int i = 0; i <= options().size(); i++) {

		}
	}

	/**
	 * Gets a list of comments (aligned numerically with {@link #options()}
	 * 
	 * @return the entire list of comments as strings
	 */
	private List<String> comments() {
		List<String> list = new LinkedList<>();

		list.add(" Wether the fireball does a natural, Ghast-Initiated explosion.\r\n"
				+ " NOTE: this will not cause as much knockback and may not cancel block damage.\r\n"
				+ " if set to true the below values will be ignored.\r\n"
				+ " if set to false, the plugin will use the below explosion settings \r\n"
				+ " for creating the explosion manually.");
		list.add("Sets the Explosion power of fireballs on impact.");
		list.add("Blocks that are immune to explosions caused by custom fireballs.");
		list.add("Keywords for Immune blocks, all material types containing these words will be whitelisted.");
		list.add("Damage dealt to entities in explosion radius");
		list.add("Whether the explosion sets blocks on fire");
		list.add("Enables/Disables throwing fireballs from the offhand slot");
		list.add("Use vanilla Fire Charges?");
		list.add("Soft-limit height that entities are thrown when caught in an explosion");
		list.add("Fireball throwing is enabled/disabled (true/false) in the following gamemodes");
		list.add("Worlds in which fireballs cannot be used.");
		list.add("Message sent when using in a NoThrowZone.");
		list.add("Plugin messaging prefix for customization.");
		list.add("Sent when reloading the plugin via command.");
		list.add("Sent when command sender doesn't have permission to execute a command.");
		list.add("If Enabled, notifies the player when the fireball they're trying to throw is on cooldown.");
		list.add("Cooldown Message this message is sent to player when above verbose: is set to true\r\n"
				+ " use the placeholder %cooldown% for the cooldown time in seconds");
		list.add("Cooldown (in seconds) between throws; set to 0 to disable.");
		list.add("Display item used for the custom fireball recipe.");
		list.add("Name of the custom fireball.");
		list.add("Set as many lines as you'd like :)");
		list.add(" - - -\r\n"
				+ " Heres the crafting recipe for the Fireballs!\r\n"
				+ " Set the AmountGiven to any number to set the amount of the item given when you craft it.\r\n"
				+ " Each line in the Pattern section is one row in the crafting table (3 x 3)\r\n"
				+ " Change the Letters to anything between 'A' and 'I' then define which material type each\r\n"
				+ " letter represents in the Materials section below\r\n"
				+ " You can get a full list of Item names here:\r\n"
				+ "    https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html\r\n"
				+ " - - -\r\n"
				+ " The plugin will give you a warning in the console if one of the items is invalid.");

		return list;
	}

	/**
	 * Gets a full list of the most recent config options.
	 *
	 * @return the entire list of options, set as strings.
	 */
	private Map<String, Object> options() {
		Map<String, Object> list = new LinkedHashMap<>();

		list.put("NaturalExplosion", Boolean.FALSE);
		list.put("FireballExplosionPower", 2);
		list.put("ImmuneBlocks", new String[] { "chest", "trapped_chest" });
		list.put("ImmuneBlockKeywords", new String[] { "stone" });
		list.put("FireballDamage", 3.5);
		list.put("FireballSetFire", Boolean.TRUE);
		list.put("AllowOffhandThrowing", Boolean.FALSE);
		list.put("UseFirecharge", Boolean.FALSE);
		list.put("MaxJumpHeight", 8);
		list.put("Gamemodes.Adventure", Boolean.TRUE);
		list.put("Gamemodes.Creative", Boolean.TRUE);
		list.put("Gamemodes.Spectator", Boolean.FALSE);
		list.put("Gamemodes.Survival", Boolean.TRUE);
		list.put("NoThrowZones", new String[] { "world_the_end", "example_world" });
		list.put("NoThrowZoneMessage", "%prefix% &cYou're not allowed to throw fireballs in this world!");
		list.put("Prefix", "&8(&6Fireballs&8)");
		list.put("ReloadMessage", "|- \r\n"
				+ "    %prefix% &aconfig file successfully reloaded,\r\n"
				+ "     &amake sure to restart the server if changing the fireball recipe!");
		list.put("NoPermissionMessage", "%prefix% &cYou do not have permission to use this");
		list.put("NoCraftPermission", "%prefix% &cYou don't have permission to craft this.");
		list.put("CooldownMessageEnabled", Boolean.TRUE);
		list.put("CooldownMessage", "%prefix% &cYou must wait %cooldown% seconds before throwing this");
		list.put("Cooldown", 1);
		list.put("FireballItem", "fire_charge");
		list.put("FireballName", "&6Fireball");
		list.put("Lore", new String[] { "&7right click to throw", "&7grief extreme ;)" });
		list.put("CraftingRecipe.AmountGiven", 3);
		list.put("CraftingRecipe.Pattern.1", "ABA");
		list.put("CraftingRecipe.Pattern.2", "BCB");
		list.put("CraftingRecpie.Pattern.3", "ABA");
		list.put("CraftingRecipe.Materials.A", "GUNPOWDER");
		list.put("CraftingRecipe.Materials.B", "FIREWORK_STAR");
		list.put("CraftingRecipe.Materials.C", "GHAST_TEAR");
		list.put("CraftingRecipe.Materials.D", " ");
		list.put("CraftingRecipe.Materials.E", " ");
		list.put("CraftingRecipe.Materials.F", " ");
		list.put("CraftingRecipe.Materials.G", " ");
		list.put("CraftingRecipe.Materials.H", " ");
		list.put("CraftingRecipe.Materials.I", " ");

		return list;
	}

	private List<Object> values() {
		List<Object> list = new LinkedList<>();

		list.add(Boolean.FALSE);
		list.add(2);
		list.add(new String[] { "chest", "trapped_chest" });
		list.add(new String[] { "stone" });
		list.add(3.5);
		list.add(Boolean.TRUE);
		list.add(Boolean.TRUE);
		list.add(Boolean.FALSE);
		list.add(8);
		list.add(Boolean.TRUE);
		list.add(Boolean.TRUE);
		list.add(Boolean.FALSE);
		list.add(Boolean.TRUE);
		list.add(new String[] { "world_the_end", "example_world" });
		list.add("%prefix% &cYou're not allowed to throw fireballs in this world!");
		list.add("&8(&6Fireballs&8)");
		list.add("|- \r\n"
				+ "    %prefix% &aconfig file successfully reloaded,\r\n"
				+ "     &amake sure to restart the server if changing the fireball recipe!");
		list.add("%prefix% &cYou do not have permission to use this");
		list.add("%prefix% &cYou don't have permission to craft this.");
		list.add(Boolean.TRUE);
		list.add("%prefix% &cYou must wait %cooldown% seconds before throwing this");
		list.add(1);
		list.add("fire_charge");
		list.add("&6Fireball");
		list.add(new String[] { "&7right click to throw", "&7grief extreme ;)" });
		list.add(3);
		list.add("ABA");
		list.add("BCB");
		list.add("ABA");
		list.add("GUNPOWDER");
		list.add("FIREWORK_STAR");
		list.add("GHAST_TEAR");
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		list.add("");

		return list;
	}

	public String configurationDatabase() {
		String db = "#-----------------------------------------------------------------\r\n" +
				"#==================================================================#\r\n" +
				"#                                                                  #\r\n" +
				"#                 Plugin by FlailoftheLord.                        #\r\n" +
				"#        -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                   #\r\n" +
				"#   ______               __        _____                           #\r\n" +
				"#   |       |           /  \\         |        |                    #\r\n" +
				"#   |__     |          /____\\        |        |                    #\r\n" +
				"#   |       |         /      \\       |        |                    #\r\n" +
				"#   |       |_____   /        \\    __|__      |______              #\r\n" +
				"#                                                                  #\r\n" +
				"#==================================================================#\r\n" +
				"#-----------------------------------------------------------------\r\n" +
				"\r\n" +
				"# Wether the fireball does a natural, Ghast-Initiated explosion.\r\n" +
				"# NOTE: this will not cause as much knockback and may not cancel block damage.\r\n" +
				"# if set to true the below values will be ignored.\r\n" +
				"# if set to false, the plugin will use the below explosion settings \r\n" +
				"# for creating the explosion manually.\r\n" +
				"NaturalExplosion: false\r\n" +
				"\r\n" +
				"# Set the Explosion Power of the Fireball on impact, set it to 0 to disable\r\n" +
				"FireballExplosionPower: 3\r\n" +
				"\r\n" +
				"ImmuneBlocks:\r\n" +
				"- \"Chest\"\r\n" +
				"- \"trapped_chest\"\r\n" +
				"- \"ender_chest\"\r\n" +
				"\r\n" +
				"# This means that any block containing the word 'stone' in its' name will not be blown up.\r\n" +
				"ImmuneBlockKeywords:\r\n" +
				"- \"stone\"\r\n" +
				"\r\n" +
				"# Set the amount of damage the fireball does to an entity or player.\r\n" +
				"# 1 = one heart\r\n" +
				"FireballDamage: 3.5\r\n" +
				"\r\n" +
				"# Set wether the Fireball does fire on impact or not\r\n" +
				"FireballSetFire: true\r\n" +
				"\r\n" +
				"# Whether you can throw a fireball from your offhand\r\n" +
				"AllowOffhandThrowing: true\r\n" +
				"\r\n" +
				"# Should players be allowed to use regular fire charges like fireballs?\r\n" +
				"UseFirecharge: false\r\n" +
				"\r\n" +
				"# Set the maximum number of blocks in height you can jump with a fireball\r\n" +
				"MaxJumpHeight: 8\r\n" +
				"\r\n" +
				"# List of worlds where throwing fireballs is NOT allowed\r\n" +
				"NoThrowZones:\r\n" +
				"- \"world_the_end\"\r\n" +
				"- \"creative_world\"\r\n" +
				"\r\n" +
				"# Message sent if the world is a 'No-Throw-Zone'\r\n" +
				"NoThrowZoneMessage: \"%prefix% &cYou're not allowed to throw fireballs in this world!\"\r\n" +
				"\r\n" +
				"#Plugin prefix for messages\r\n" +
				"# simply put the placeholder  %prefix% in the message, and it will be replaced by this prefix.\r\n" +
				"Prefix: \"&8(&6Fireballs&8)\"\r\n" +
				"\r\n" +
				"ReloadMessage: |- \r\n" +
				"    %prefix% &aconfig file successfully reloaded,\r\n" +
				"     &amake sure to restart the server if changing the fireball recipe!\r\n" +
				"\r\n" +
				"#Message to send to player if no permissions\r\n" +
				"NoPermissionMessage: \"%prefix% &cYou do not have permission to use this\"\r\n" +
				"\r\n" +
				"# Whether to show cooldown Messages to player or not\r\n" +
				"CooldownMessageEnabled: true\r\n" +
				"\r\n" +
				"#Cooldown Message this message is sent to player when above verbose: is set to true\r\n" +
				"# use the placeholder %cooldown% for the cooldown time in seconds\r\n" +
				"CooldownMessage: \"%prefix% &cYou must wait %cooldown% seconds before throwing this\"\r\n" +
				"\r\n" +
				"# Cooldown between uses in seconds set to 0 to disable\r\n" +
				"Cooldown: 1\r\n" +
				"\r\n" +
				"# Don't want it to look like a FireCharge?\r\n" +
				"FireballItem: \"fire_charge\"\r\n" +
				"\r\n" +
				"# Custom display name for the Fireball Item\r\n" +
				"FireballName: \"&6Fireball\"\r\n" +
				"\r\n" +
				"# Heres the lore...\r\n" +
				"# add as many lines as you want!! :>\r\n" +
				"Lore:\r\n" +
				"- \"&7right click to throw\"\r\n" +
				"- \"&7grief extreme ;)\"\r\n" +
				"\r\n" +
				"# - - -\r\n" +
				"# Heres the crafting recipe for the Fireballs!\r\n" +
				"# Set the AmountGiven to any number to set the amount of the item given when you craft it.\r\n" +
				"# Each line in the Pattern section is one row in the crafting table (3 x 3)\r\n" +
				"# Change the Letters to anything between 'A' and 'I' then define which material type each\r\n" +
				"# letter represents in the Materials section below\r\n" +
				"# You can get a full list of Item names here:\r\n" +
				"#    https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html\r\n" +
				"# - - -\r\n" +
				"# The plugin will give you a warning in the console if one of the items is invalid.\r\n" +
				"CraftingRecipe:\r\n" +
				"  AmountGiven: 3\r\n" +
				"  Pattern:\r\n" +
				"      1: \"ABA\"\r\n" +
				"      2: \"BCB\"\r\n" +
				"      3: \"ABA\"\r\n" +
				"  Materials:\r\n" +
				"      A: \"GUNPOWDER\"\r\n" +
				"      B: \"FIREWORK_STAR\"\r\n" +
				"      C: \"GHAST_TEAR\"\r\n" +
				"      D: \"\"\r\n" +
				"      E: \"\"\r\n" +
				"      F: \"\"\r\n" +
				"      E: \"\"\r\n" +
				"      H: \"\"\r\n" +
				"      I: \"\"\r\n" +
				"\r\n" +
				"";

		return db;
	}

}
