package me.flail.ThrowableFireballs.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

	/**
	 * Get the configuration file.
	 *
	 * @return config file formatted as {@link FileConfiguration}
	 */
	public FileConfiguration get() {
		FileConfiguration config = new YamlConfiguration();
		File file = new File("plugins/ThrowableFireballs/config.yml");
		try {
			config.load(file);
		} catch (Throwable t) {
		}

		return config;
	}

	/**
	 * Gets a full list of the most recent config options.
	 *
	 * @return the entire list of options, set as strings.
	 */
	public List<String> options() {
		List<String> options = new ArrayList<>();

		options.add("NaturalExplosion");
		options.add("FireballExplosionPower");
		options.add("ImmuneBlocks");
		options.add("ImmuneBlockKeywords");
		options.add("FireballDamage");
		options.add("FireballSetFire");
		options.add("AllowOffhandThrowing");
		options.add("UseFirecharge");
		options.add("MaxJumpHeight");
		options.add("NoThrowZones");
		options.add("NoThrowZoneMessage");
		options.add("Prefix");
		options.add("ReloadMessage");
		options.add("NoPermissionMessage");
		options.add("CooldownMessageEnabled");
		options.add("CooldownMessage");
		options.add("Cooldown");
		options.add("FireballName");
		options.add("Lore");
		options.add("CraftingRecipe");
		options.add("CraftingRecipe.AmountGiven");
		options.add("CraftingRecipe.Pattern.1");
		options.add("CraftingRecipe.Pattern.2");
		options.add("CraftingRecpie.Pattern.3");
		options.add("CraftingRecipe.Materials.A");
		options.add("CraftingRecipe.Materials.B");
		options.add("CraftingRecipe.Materials.C");
		options.add("CraftingRecipe.Materials.D");
		options.add("CraftingRecipe.Materials.E");
		options.add("CraftingRecipe.Materials.F");
		options.add("CraftingRecipe.Materials.G");
		options.add("CraftingRecipe.Materials.H");
		options.add("CraftingRecipe.Materials.I");

		return options;
	}

	public String configurationDatabase() {
		String db = "#-----------------------------------------------------------------\r\n"
				+ "#==================================================================#\r\n"
				+ "#                                                                  #\r\n"
				+ "#                 Plugin by FlailoftheLord.                        #\r\n"
				+ "#        -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                   #\r\n"
				+ "#        For questions please join my discord server:              #\r\n"
				+ "#                https://discord.gg/wuxW5PS                        #\r\n"
				+ "#   ______               __        _____                           #\r\n"
				+ "#   |       |           /  \\         |        |                    #\r\n"
				+ "#   |__     |          /____\\        |        |                    #\r\n"
				+ "#   |       |         /      \\       |        |                    #\r\n"
				+ "#   |       |_____   /        \\    __|__      |______              #\r\n"
				+ "#                                                                  #\r\n"
				+ "#==================================================================#\r\n"
				+ "#-----------------------------------------------------------------\r\n" + "\r\n"
				+ "# Wether the fireball does a natural, Ghast-Initiated explosion.\r\n"
				+ "# if set to true the below values will be ignored.\r\n"
				+ "# if set to false, the plugin will use the below explosion settings \r\n"
				+ "# for creating the explosion manually.\r\n" + "NaturalExplosion: true\r\n" + "\r\n"
				+ "# Set the Explosion Power of the Fireball on impact, set it to 0 to disable\r\n"
				+ "FireballExplosionPower: 3\r\n" + "\r\n" + "ImmuneBlocks:\r\n" + "- \"Chest\"\r\n"
				+ "- \"trapped_chest\"\r\n" + "- \"ender_chest\"\r\n" + "\r\n"
				+ "# This means that any block containing the word 'stone' in its' name will not be blown up.\r\n"
				+ "ImmuneBlockKeywords:\r\n" + "- \"stone\"\r\n" + "\r\n" + "# Probbly doesn't work...\r\n"
				+ "FireballSpeed: 2.0\r\n" + "\r\n"
				+ "# Set the amount of damage the fireball does to an entity or player.\r\n" + "# 1 = one heart\r\n"
				+ "FireballDamage: 3.5\r\n" + "\r\n" + "# Set wether the Fireball does fire on impact or not\r\n"
				+ "FireballSetFire: true\r\n" + "\r\n" + "# Whether you can throw a fireball from your offhand\r\n"
				+ "AllowOffhandThrowing: true\r\n" + "\r\n"
				+ "# Should players be allowed to use regular fire charges like fireballs?\r\n"
				+ "UseFirechrage: false\r\n" + "\r\n"
				+ "# Set the maximum number of blocks in height you can jump with a fireball\r\n"
				+ "MaxJumpHeight: 8\r\n" + "\r\n" + "# List of worlds where throwing fireballs is NOT allowed\r\n"
				+ "NoThrowZones:\r\n" + "- \"world_the_end\"\r\n" + "- \"creative_world\"\r\n" + "\r\n"
				+ "# Message sent if the world is a 'No-Throw-Zone'\r\n"
				+ "NoThrowZoneMessage: \"%prefix% &cYou're not allowed to throw fireballs in this world!\"\r\n" + "\r\n"
				+ "#Plugin prefix for messages\r\n"
				+ "# simply put the placeholder  %prefix% in the message, and it will be replaced by this prefix.\r\n"
				+ "Prefix: \"&8(&6Fireballs&8)\"\r\n" + "\r\n" + "ReloadMessage: |- \r\n"
				+ "    %prefix% &aconfig file successfully reloaded,\r\n"
				+ "     &amake sure to restart the server if changing the fireball recipe!\r\n" + "\r\n"
				+ "#Message to send to player if no permissions\r\n"
				+ "NoPermissionMessage: \"%prefix% &cYou do not have permission to use this\"\r\n" + "\r\n"
				+ "# Whether to show cooldown Messages to player or not\r\n" + "CooldownMessageEnabled: true\r\n"
				+ "\r\n" + "#Cooldown Message this message is sent to player when above verbose: is set to true\r\n"
				+ "# use the placeholder %cooldown% for the cooldown time in seconds\r\n"
				+ "CooldownMessage: \"%prefix% &cYou must wait %cooldown% seconds before throwing this\"\r\n" + "\r\n"
				+ "# Cooldown between uses in seconds set to 0 to disable\r\n" + "Cooldown: 1\r\n" + "\r\n"
				+ "# Custom display name for the Fireball Item\r\n" + "FireballName: \"&6Fireball\"\r\n" + "\r\n"
				+ "# Heres the lore...\r\n" + "# add as many lines as you want!! :>\r\n" + "Lore:\r\n"
				+ "- \"&7right click to throw\"\r\n" + "- \"&7grief extreme ;)\"\r\n" + "\r\n" + "\r\n"
				+ "# Heres the crafting recipe for the Fireballs!\r\n"
				+ "# Set the AmountGiven to any number to set the amount of the item given when you craft it.\r\n"
				+ "# Each line in the Pattern section is one row in the crafting table (3 x 3)\r\n"
				+ "# Change the Letters to anything between 'A' and 'I' then define which material type each\r\n"
				+ "# letter represents in the Materials section below\r\n"
				+ "# You can get a full list of Item names here:\r\n"
				+ "#    https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html\r\n" + "\r\n"
				+ "# NOTE: SOME ITEM NAMES HAVE CHANGED IN THE 1.13 UPDATE!!\r\n"
				+ "# This will cause some items to not work in 1.12 and lower...\r\n"
				+ "# It is recommended that you only use this plugin with version 1.13 +\r\n"
				+ "# The plugin will give you a warning in the console if one of the items is invalid.\r\n"
				+ "CraftingRecipe:\r\n" + "  AmountGiven: 3\r\n" + "  Pattern:\r\n" + "      1: \"ABA\"\r\n"
				+ "      2: \"BCB\"\r\n" + "      3: \"ABA\"\r\n" + "  Materials:\r\n" + "      A: \"GUNPOWDER\"\r\n"
				+ "      B: \"FIREWORK_STAR\"\r\n" + "      C: \"GHAST_TEAR\"\r\n" + "      D: \"\"\r\n"
				+ "      E: \"\"\r\n" + "      F: \"\"\r\n" + "      E: \"\"\r\n" + "      H: \"\"\r\n"
				+ "      I: \"\"\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n" + "";

		return db;

	}

}
