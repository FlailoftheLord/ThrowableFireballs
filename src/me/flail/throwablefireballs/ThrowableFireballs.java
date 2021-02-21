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

package me.flail.throwablefireballs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import me.flail.throwablefireballs.handlers.FireballCraft;
import me.flail.throwablefireballs.handlers.FireballDamage;
import me.flail.throwablefireballs.handlers.FireballExplosion;
import me.flail.throwablefireballs.handlers.FireballItem;
import me.flail.throwablefireballs.handlers.FireballRecipe;
import me.flail.throwablefireballs.handlers.FireballThrow;
import me.flail.throwablefireballs.handlers.WorldGuardHandle;
import me.flail.throwablefireballs.handlers.elytra.PlayerHitListener;
import me.flail.throwablefireballs.tools.TabCompleter;
import me.flail.throwablefireballs.tools.Tools;

public class ThrowableFireballs extends JavaPlugin implements Listener {

	public ConsoleCommandSender console;
	public boolean isWorldGuard;
	public WorldGuardHandle worldguard;
	public FileConfiguration conf;

	public final List<String> immuneBlocks = new ArrayList<>();
	public final List<String> immuneBlockKeys = new ArrayList<>();

	public Server server;
	public BukkitScheduler scheduler;

	@Override
	public void onLoad() {
		server = getServer();
		scheduler = server.getScheduler();
		console = server.getConsoleSender();

		// Update config file
		saveDefaultConfig();
		conf = getConfig();
	}

	@Override
	public void onEnable() {
		Tools tools = new Tools();
		PluginManager m = server.getPluginManager();

		immuneBlocks.addAll(conf.getStringList("ImmuneBlocks"));
		immuneBlockKeys.addAll(conf.getStringList("ImmuneBlockKeys"));

		registerRecipes();

		// Register Events and Commands
		registerEvents();

		registerCommands();

		// Friendly console spam ;)
		String version = getDescription().getVersion();
		tools.console(" ");
		tools.console("&6ThrowableFireballs &7v" + version);
		tools.console("&2  by FlailoftheLord");
		tools.console("&6 Grief Extreme!?!?");
		tools.console(" ");
		if (m.getPlugin("WorldGuard") != null) {
			isWorldGuard = true;

			tools.console("&6WorldGuard found, registering region flags...");
		}

		if (isWorldGuard) {
			worldguard = new WorldGuardHandle();

			worldguard.registerFlags();
		}

	}

	@Override
	public void onDisable() {
		Tools tools = new Tools();

		scheduler.cancelTasks(this);
		tools.console("&6Farewell!");
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player plyer = event.getPlayer();
		if (plyer.hasPermission("fireballs.craft")) {
			if (!plyer.hasDiscoveredRecipe(new FireballRecipe().getNamespace())) {
				plyer.discoverRecipe(new FireballRecipe().getNamespace());
			}
			return;
		}

		plyer.undiscoverRecipe(new FireballRecipe().getNamespace());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return new Commands(sender, command, label, args).run();
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return new TabCompleter(command).construct(args);
	}

	private void registerCommands() {
		for (String string : getDescription().getCommands().keySet()) {
			this.getCommand(string).setExecutor(this);
		}
	}

	private void registerEvents() {
		PluginManager manager = server.getPluginManager();
		manager.registerEvents(new FireballExplosion(), this);
		manager.registerEvents(new FireballDamage(), this);
		manager.registerEvents(new FireballThrow(), this);
		manager.registerEvents(new PlayerHitListener(), this);
		manager.registerEvents(new FireballCraft(), this);
		manager.registerEvents(this, this);

	}

	public void registerRecipes() {

		Tools tools = new Tools();

		FileConfiguration config = getConfig();

		FireballRecipe recipes = new FireballRecipe();

		int yield = config.getInt("AmountGiven");

		ItemStack fb = new FireballItem().fireball();

		fb.setAmount(yield);

		List<Recipe> fbR = server.getRecipesFor(fb);

		fbR.clear();
		tools.console(("&eFireball Recipe has been updated!"));

		server.addRecipe(recipes.fireballRecipe());

	}

}
