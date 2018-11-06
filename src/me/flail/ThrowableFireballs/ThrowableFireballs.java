package me.flail.ThrowableFireballs;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

public class ThrowableFireballs extends JavaPlugin implements CommandExecutor, Listener {

	private ConsoleCommandSender console = Bukkit.getConsoleSender();

	private String version;

	private Server server = getServer();

	@Override
	public void onEnable() {

		Tools tools = new Tools();

		// Save config files
		saveDefaultConfig();

		registerRecipes();

		// Register Events and Commands
		server.getPluginManager().registerEvents(new FireballThrow(), this);
		server.getPluginManager().registerEvents(new FireballExplosion(), this);
		server.getPluginManager().registerEvents(new FireballDamage(), this);
		server.getPluginManager().registerEvents(new FireballVelocity(), this);

		getCommand("fireball").setExecutor(new Commands());
		getCommand("throwablefireballs").setExecutor(new Commands());
		getCommand("icanhasfireball").setExecutor(new Commands());

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

		console.sendMessage(tools.chat("&6&lFarewell!"));
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