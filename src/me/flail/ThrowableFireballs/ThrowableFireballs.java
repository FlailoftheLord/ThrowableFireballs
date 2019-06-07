/*
 *  Copyright (C) 2018-2019 FlailoftheLord
 *
 *  This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package me.flail.ThrowableFireballs;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import me.flail.ThrowableFireballs.Handlers.FireballDamage;
import me.flail.ThrowableFireballs.Handlers.FireballExplosion;
import me.flail.ThrowableFireballs.Handlers.FireballItem;
import me.flail.ThrowableFireballs.Handlers.FireballRecipe;
import me.flail.ThrowableFireballs.Handlers.FireballThrow;
import me.flail.ThrowableFireballs.Handlers.FireballVelocity;
import me.flail.ThrowableFireballs.Handlers.elytra.PlayerHitListener;
import me.flail.ThrowableFireballs.Tools.TabCompleter;
import me.flail.ThrowableFireballs.Tools.Tools;

public class ThrowableFireballs extends JavaPlugin implements CommandExecutor, Listener {

	private ConsoleCommandSender console = Bukkit.getConsoleSender();
	public boolean tossed = false;

	private String version;

	public Server server = getServer();
	public BukkitScheduler scheduler = server.getScheduler();

	@Override
	public void onEnable() {
		Tools tools = new Tools();

		// Save config files
		saveDefaultConfig();

		registerRecipes();

		// Register Events and Commands
		registerEvents();

		registerCommands();

		// Friendly console spam ;)
		version = getDescription().getVersion();
		console.sendMessage(" ");
		console.sendMessage(tools.chat("&6ThrowableFireballs &7v" + version));
		console.sendMessage(tools.chat("&2  by FlailoftheLord"));
		console.sendMessage(tools.chat("&6 Grief Extreme!?!?"));
		console.sendMessage(" ");

	}

	@Override
	public void onDisable() {
		Tools tools = new Tools();

		scheduler.cancelTasks(this);
		console.sendMessage(tools.chat("&6&lFarewell!"));
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
		manager.registerEvents(new FireballVelocity(), this);
		manager.registerEvents(new PlayerHitListener(), this);

	}

	public void registerRecipes() {

		Tools tools = new Tools();

		FileConfiguration config = getConfig();

		FireballRecipe recipes = new FireballRecipe();

		int yield = config.getInt("AmountGiven");

		ItemStack fb = new FireballItem().fireball();

		fb.setAmount(yield);

		List<Recipe> fbR = server.getRecipesFor(fb);

		if (fbR != null) {

			fbR.clear();

			server.addRecipe(recipes.fireballRecipe());

			console.sendMessage(tools.chat("&eFireball Recipe has been updated!"));

		} else {

			server.addRecipe(recipes.fireballRecipe());

		}

	}

}
