/*
 *
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.flail.ThrowableFireballs.Config.ConfigUpdater;
import me.flail.ThrowableFireballs.Handlers.FireballItem;
import me.flail.ThrowableFireballs.Handlers.FireballThrow;
import me.flail.ThrowableFireballs.Tools.Tools;

public class Commands  extends Tools {
	private Command command;
	private String label;
	private CommandSender sender;
	private String[] args;

	private ThrowableFireballs plugin = ThrowableFireballs.getPlugin(ThrowableFireballs.class);
	private FileConfiguration config = plugin.getConfig();

	public Commands(CommandSender sender, Command command, String label, String[] args) {
		this.sender = sender;
		this.command = command;
		this.args = args;
		this.label = label;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		this.command = command;
		this.label = label;
		config = plugin.getConfig();

		String cmd = command.getName().toLowerCase();


		String reloadMessage = config.getString("ReloadMessage");

		String noPermission = config.getString("NoPermissionMessage");

		Player player = (Player) sender;

		if (cmd.equals("throwablefireballs")) {
			if (args.length == 2) {

				if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("about")) {

					player.sendMessage(chat(
							"%prefix% &6&lThrowableFireballs &7running version &e%version%  &2by FlailoftheLord."));

				} else if (args[0].equalsIgnoreCase("get")) {

					if (player.hasPermission("fireballs.op")) {

						try {

							int amount = Integer.parseInt(args[1]);

							if ((amount > 64) || (amount < 1)) {

								player.sendMessage(chat("%prefix% &cProper usage: &7/%cmd% get [number(1-64)]")
										.replace("%cmd%", cmd));

							} else {

								ItemStack fireball = new FireballItem().fireball();

								Map<Integer, ItemStack> fireballsGiven = new HashMap<>();

								fireballsGiven.put(amount, fireball);

								fireball.setAmount(amount);

								player.getInventory().addItem(fireball);

								player.sendMessage(chat("%prefix% &ayou got %num% fireballs!").replace("%num%",
										amount + ""));
							}

						} catch (NumberFormatException e) {
							player.sendMessage(chat("%prefix% &cProper usage: &7/%cmd% get [number(1-64)]")
									.replace("%cmd%", cmd));
						}

					}

				}

			} else if (args.length == 1) {

				if (player.hasPermission("fireballs.op")) {

					if (args[0].equalsIgnoreCase("reload")) {

						if (player.hasPermission("fireballs.op")) {

							plugin.reloadConfig();

							sender.sendMessage(chat(reloadMessage));
						} else {
							player.sendMessage(chat(noPermission));
						}

					} else if (args[0].equalsIgnoreCase("get")) {

						ItemStack fireball = new FireballItem().fireball();

						fireball.setAmount(1);

						player.getInventory().addItem(fireball);

						player.sendMessage(chat("%prefix% &aYou got a fireball!"));

					} else if (args[0].equalsIgnoreCase("updateconfig")) {
						if (player.hasPermission("fireballs.op")) {

							ConfigUpdater updater = new ConfigUpdater();

							updater.updateConfig(plugin.getConfig());

							player.sendMessage(
									chat("%prefix% &aUpdated your config file to the latest settings!"));
							player.sendMessage(chat(
									"&7 a copy of your old config was saved as &3'old-config.yml' &7in the base ThrowableFireballs folder."));

						}

					} else {

						for (Player p : Bukkit.getOnlinePlayers()) {

							if (p == null) {
								continue;
							}

							if (args[0].equalsIgnoreCase(p.getName()) || p.getName().startsWith(args[0])) {

								new FireballThrow().throwBall(p.launchProjectile(Fireball.class));

								break;
							}

						}
					}

				} else {
					player.sendMessage(chat(noPermission));
				}
			} else if (args.length == 0) {
				if (player.hasPermission("fireballs.op")) {

					new FireballThrow().throwBall(player.launchProjectile(Fireball.class));
					return true;

				} else {
					player.sendMessage(chat(noPermission));
				}
			}

		} else if (cmd.equals("icanhasfireball")) {

			if (player.isOp() || player.hasPermission("lol.fireball.lol")) {
				player.sendMessage(chat("%prefix% &6&o&lYass bb! &4&l<3"));
				player.getInventory().addItem(new FireballItem().fireball());
			} else if ((player.isOp() == false) && !(player.hasPermission("lol.fireball.lol"))) {
				player.sendMessage(chat("%prefix% &elol, don't you wish!"));
			}

		}

		return run();
	}

	protected boolean run() {
		String reloadMessage = config.getString("ReloadMessage");
		String noPermission = config.getString("NoPermissionMessage");
		String defaultMsg = chat("%prefix% &6&lThrowableFireballs &7running version &e%version%  &2by FlailoftheLord.");
		String versionInfo = chat("&7Running on " + plugin.server.getName() + " version " + plugin.server.getVersion());
		String usage = chat("&7Usage&8: &7/" + label + " <help>");
		String[] arguments = { "reload  &oreload the configuration file.", "info  &odump plugin information.",
				"updateconfig  &omanually update your configuration file.", "give <player> [amount]  &ogive a player a fireball.",
		"get [amount]  &oget a fireball." };

		List<String> helpLines = new ArrayList<>();
		helpLines.add(chat("&6ThrowableFireballs &eCommand usage."));
		for (String arg : arguments) {
			helpLines.add(chat("&8-> &7/" + label + " " + arg));
		}

		switch (args.length) {
		case 0:
			sender.sendMessage(defaultMsg);
			if (sender.hasPermission("fireballs.op")) {
				sender.sendMessage(usage);
			}

		case 1:
			switch (args[0].toLowerCase()) {

			case "help":
				for (String line : helpLines) {
					sender.sendMessage(line);
				}
				break;
			case "info":
				sender.sendMessage(defaultMsg);
				if (sender.hasPermission("fireballs.op")) {
					sender.sendMessage(versionInfo);
					sender.sendMessage(usage);
				}
				break;
			case "get":
				if (sender.hasPermission("fireballs.op")&& (sender instanceof Player)) {
					Player player = (Player) sender;
					player.getInventory().addItem(new FireballItem().fireball());
					sender.sendMessage(chat("%prefix% &ayou got a fireball."));
					break;
				}
				sender.sendMessage(chat("You can't get a fireball!"));
				break;
			default:
				sender.sendMessage(usage);

			}

		case 2:

		case 3:

		}


		return sender != null;
	}

}