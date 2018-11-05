package me.flail.ThrowableFireballs;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Commands implements CommandExecutor {

	private ThrowableFireballs plugin = ThrowableFireballs.getPlugin(ThrowableFireballs.class);

	private Tools tools = new Tools();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		String cmd = command.getName().toLowerCase();

		FileConfiguration config = plugin.getConfig();

		String version = plugin.getDescription().getVersion();

		String reloadMessage = config.getString("ReloadMessage");

		String noPermission = config.getString("NoPermissionMessage");

		Player player = (Player) sender;

		if (cmd.equals("fireball")) {
			if (args.length == 2) {

				if (args[0].equalsIgnoreCase("get")) {

					if (player.hasPermission("fireballs.op")) {

						try {

							int amount = Integer.parseInt(args[1]);

							if ((amount > 64) || (amount < 1)) {

								player.sendMessage(tools.chat("%prefix% &cProper usage: &7/%cmd% get [number(1-64)]")
										.replace("%cmd%", cmd));

							} else {

								ItemStack fireball = new FireballItem().fireball();

								Map<Integer, ItemStack> fireballsGiven = new HashMap<Integer, ItemStack>();

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
						plugin.reloadConfig();
						sender.sendMessage(tools.chat(reloadMessage));

					} else if (args[0].equalsIgnoreCase("get")) {

						ItemStack fireball = new FireballItem().fireball();

						fireball.setAmount(1);

						player.getInventory().addItem(fireball);

						player.sendMessage(tools.chat("%prefix% &aYou got a fireball!"));

					} else {

						for (Player p : Bukkit.getOnlinePlayers()) {

							if (args[0].equalsIgnoreCase(p.getName()) || p.getName().startsWith(args[0])) {

								doThrow(p.launchProjectile(Fireball.class));

								break;
							}

						}
					}

				} else {
					player.sendMessage(tools.chat(noPermission));
				}
			} else if (args.length == 0) {
				if (player.hasPermission("fireballs.op")) {

					doThrow(player.launchProjectile(Fireball.class));
					return true;

				} else {
					player.sendMessage(tools.chat(noPermission));
				}
			}

		} else if (cmd.equals("throwablefireballs")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {

					if (player.hasPermission("fireballs.op")) {

						plugin.reloadConfig();

						sender.sendMessage(tools.chat(reloadMessage));
					} else {
						player.sendMessage(tools.chat(noPermission));
					}

				} else {
					sender.sendMessage(ChatColor.GOLD + "ThrowableFireballs " + ChatColor.GRAY + "v" + version
							+ ChatColor.GOLD + " by FlailoftheLord ");
				}
			} else {
				sender.sendMessage(ChatColor.GOLD + "ThrowableFireballs " + ChatColor.GRAY + "v" + version
						+ ChatColor.GOLD + " by FlailoftheLord ");
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

	private void doThrow(Fireball ball) {

		FileConfiguration config = plugin.getConfig();

		double speed = config.getDouble("FireballSpeed");

		if (ball.getType().toString().equals("FIREBALL")) {
			Vector bVel = ball.getVelocity().multiply(speed);
			ball.setIsIncendiary(false);
			ball.setVelocity(bVel);
			ball.setYield(1);
            ball.setCustomName("HolyBalls");
            ball.setCustomNameVisible(false);
		}

	}

}