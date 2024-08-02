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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
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

import com.sk89q.worldguard.WorldGuard;

import me.flail.throwablefireballs.config.Config;
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

	public NamespacedKey namespace = new NamespacedKey(this, "throwable_fireballs");

	public ConsoleCommandSender console;
	public boolean isWorldGuard;
	public WorldGuardHandle worldguard;
	public FileConfiguration conf;
	public Config configDB;
	Tools tools;

	public final List<String> immuneBlocks = new ArrayList<>();
	public final List<String> immuneBlockKeys = new ArrayList<>();
	public Set<GameMode> disabledGamemodes = new HashSet<>();

	public Server server;
	public BukkitScheduler scheduler;
	public PluginManager m;

	String WORLD_GUARD = "WorldGuard";
	public WorldGuard wg = null;
	int wgregisterattempts = 0;

	@Override
	public void onLoad() {
		tools = new Tools();

		server = getServer();
		scheduler = server.getScheduler();
		console = server.getConsoleSender();
		m = server.getPluginManager();

		// Update config file
		saveDefaultConfig();

		configDB = new Config();
		configDB.setup();

	}

	@Override
	public void onEnable() {

		this.doReload(null);

		registerRecipes();

		// Register Events and Commands
		registerEvents();

		registerCommands();

		// Friendly console spam ;)
		String version = getDescription().getVersion();
		tools.console(" ");
		tools.console("&6ThrowableFireballs &7v" + version);
		tools.console("&2  by FlailoftheLord");
		tools.console("&6 NANI KORE~!?!?");
		tools.console(" ");

		if (m.getPlugin(WORLD_GUARD) != null) {// m.getPlugin(WORLD_GUARD).isEnabled())
			tools.console("&6WorldGuard found, registering region flags...");

			initiateWorldGuard();
		}

	}

	@Override
	public void onDisable() {

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

	public void doReload(CommandSender op) {
		this.conf = configDB.get();

		immuneBlocks.addAll(conf.getStringList("ImmuneBlocks"));
		immuneBlockKeys.addAll(conf.getStringList("ImmuneBlockKeys"));
		this.registerGamemodes();

		if (op != null)
			op.sendMessage(tools.chat(conf.getString("ReloadMessage")));
	}

	void initiateWorldGuard() {
		wg = WorldGuard.getInstance();
		if (wg != null) {

			worldguard = new WorldGuardHandle();
			boolean loaded = worldguard.registerFlags();
			if (!loaded)
				reInitWg();

		} else
			reInitWg();

	}

	void reInitWg() {
		if (wgregisterattempts > 5) {
			tools.console("&7WorldGuard flag registry failed.");
			return;
		}
		wgregisterattempts += 1;
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
			initiateWorldGuard();
		}, 100L);

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

	private void registerGamemodes() {
		for (String s : new String[] { "Adventure", "Creative", "Spectator", "Survival" }) {
			if (!conf.getBoolean("Gamemodes." + s, true))
				this.disabledGamemodes.add(GameMode.valueOf(s.toUpperCase()));

		}
	}

}
