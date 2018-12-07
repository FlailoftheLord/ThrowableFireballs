/*
 *
 *  Copyright (C) 2018 FlailoftheLord
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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Commands implements CommandExecutor {

	private ThrowableFireballs plugin = ThrowableFireballs.getPlugin(ThrowableFireballs.class);

	private Tools tools = new Tools();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		String cmd = command.getName().toLowerCase(Locale.ENGLISH);

		FileConfiguration config = plugin.getConfig();

		String reloadMessage = config.getString("ReloadMessage");

		String noPermission = config.getString("NoPermissionMessage");

		Player player = (Player) sender;

		if (cmd.equals("throwablefireballs")) {
			if (args.length == 2) {

				if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("about")) {

					player.sendMessage(tools.chat(
							"%prefix% &6&lThrowableFireballs &7running version &e%version%  &2by FlailoftheLord."));

				} else if (args[0].equalsIgnoreCase("get")) {

					if (player.hasPermission("fireballs.op")) {

						try {

							int amount = Integer.parseInt(args[1]);

							if ((amount > 64) || (amount < 1)) {

								player.sendMessage(tools.chat("%prefix% &cProper usage: &7/%cmd% get [number(1-64)]")
										.replace("%cmd%", cmd));

							} else {

								ItemStack fireball = new FireballItem().fireball();

								Map<Integer, ItemStack> fireballsGiven = new HashMap<>();

								fireballsGiven.put(amount, fireball);

								fireball.setAmount(amount);

								player.getInventory().addItem(fireball);

								player.sendMessage(tools.chat("%prefix% &ayou got %num% fireballs!").replace("%num%",
										amount + ""));
							}

						} catch (NumberFormatException e) {
							player.sendMessage(tools.chat("%prefix% &cProper usage: &7/%cmd% get [number(1-64)]")
									.replace("%cmd%", cmd));
						}

					}

				}

			} else if (args.length == 1) {

				if (player.hasPermission("fireballs.op")) {

					if (args[0].equalsIgnoreCase("reload")) {

						if (player.hasPermission("fireballs.op")) {

							plugin.reloadConfig();

							sender.sendMessage(tools.chat(reloadMessage));
						} else {
							player.sendMessage(tools.chat(noPermission));
						}

					} else if (args[0].equalsIgnoreCase("get")) {

						ItemStack fireball = new FireballItem().fireball();

						fireball.setAmount(1);

						player.getInventory().addItem(fireball);

						player.sendMessage(tools.chat("%prefix% &aYou got a fireball!"));

					} else {

						for (Player p : Bukkit.getOnlinePlayers()) {

							if (p == null) {
								continue;
							}

							if (args[0].equalsIgnoreCase(p.getName()) || p.getName().startsWith(args[0])) {

								new FireballThrow().doThrow(p.launchProjectile(Fireball.class));

								break;
							}

						}
					}

				} else {
					player.sendMessage(tools.chat(noPermission));
				}
			} else if (args.length == 0) {
				if (player.hasPermission("fireballs.op")) {

					new FireballThrow().doThrow(player.launchProjectile(Fireball.class));
					return true;

				} else {
					player.sendMessage(tools.chat(noPermission));
				}
			}

		} else if (cmd.equals("icanhasfireball")) {

			if (player.isOp() || player.hasPermission("lol.fireball.lol")) {
				player.sendMessage(tools.chat("%prefix% &6&o&lYass bb! &4&l<3"));
			} else if ((player.isOp() == false) && !(player.hasPermission("lol.fireball.lol"))) {
				player.sendMessage(tools.chat("%prefix% &elol, don't you wish!"));
			}

		}

		return true;
	}

}