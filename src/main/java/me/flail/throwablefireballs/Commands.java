package me.flail.throwablefireballs;

import java.util.ArrayList;
import java.util.List;
import me.flail.throwablefireballs.handlers.FireballItem;
import me.flail.throwablefireballs.handlers.FireballThrow;
import me.flail.throwablefireballs.tools.Tools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Commands extends Tools {

    private final Command command;
    private final String label;
    private final CommandSender sender;
    private final String[] args;

    private final FileConfiguration config = plugin.conf;

    public Commands(CommandSender sender, Command command, String label, String[] args) {
        this.sender = sender;
        this.command = command;
        this.args = args;
        this.label = label;
    }

    protected boolean run() {
        if (command.getName().equals("icanhasfireball")) {
            if (sender.hasPermission("lol.fireball.lol") || sender.isOp()) {
                sender.sendMessage(chat("%prefix% &6&o&lYass bb! &4&l<3"));
                if (sender instanceof Player player) {
                    player.getInventory().addItem(new FireballItem().fireball());
                }
            } else if (!sender.isOp() && !(sender.hasPermission("lol.fireball.lol"))) {
                sender.sendMessage(chat("%prefix% &elol, don't you wish!"));
            }
            return true;
        } else if (command.getName().equals("throwablefireballs")) {
            String noPermission = chat(config.getString("NoPermissionMessage"));
            String defaultMsg = chat("%prefix% &6&lThrowableFireballs &7version &e%version%  &2by FlailoftheLord.");
            String versionInfo = chat("&7Running on " + plugin.server.getName() + " version " + plugin.server.getVersion());
            String usage = chat("&7Usage&8: &7/" + label + " <help>");
            String[] arguments = {
                "reload  &oreload the configuration file.",
                "info  &odump plugin information.",
                "updateconfig  &omanually update your configuration file.",
                "give <player> [amount]  &ogive a player a fireball.",
                "get [amount]  &oget a fireball.",
            };

            List<String> helpLines = new ArrayList<>();
            helpLines.add(chat("&6ThrowableFireballs &eCommand usage."));
            for (String arg : arguments) {
                helpLines.add(chat("&8-> &7/" + label + " " + arg));
            }
            String arg;
            String plural = "";

            if (args.length > 0) {
                arg = args[0].toLowerCase();
                switch (arg) {
                    case "reload":
                        if (sender.hasPermission("fireballs.op")) {
                            plugin.doReload(sender);

                            return true;
                        }
                        sender.sendMessage(noPermission);
                        return true;
                    case "updateconfig":
                        if (sender.hasPermission("fireballs.op")) {
                            plugin.configDB.setup();
                            sender.sendMessage(chat("%prefix% &6Configuration file has been updated and any broken values fixed."));
                        } else sender.sendMessage(noPermission);
                        return true;
                }
            }

            if (args.length == 0) {
                if (label.equalsIgnoreCase("fireball") && (sender instanceof Player player)) {
                    if (player.hasPermission("fireballs.commandthrow")) {
                        new FireballThrow().throwBall(player);
                        return true;
                    }
                }

                sender.sendMessage(defaultMsg);
                if (sender.hasPermission("fireballs.op")) sender.sendMessage(usage);

                return true;
            } else if (args.length == 1) {
                arg = args[0].toLowerCase();
                switch (arg) {
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
                        if (sender.hasPermission("fireballs.op") && (sender instanceof Player player)) {
                            givePlayerFireball(player, 1);
                            sender.sendMessage(chat("%prefix% &eyou got a fireball."));
                            break;
                        }
                        sender.sendMessage(noPermission);
                        break;
                    default:
                        sender.sendMessage(usage);
                }

                return true;
            } else if (args.length == 2) {
                arg = args[0].toLowerCase();
                switch (arg) {
                    case "get":
                        String value = args[1].replaceAll("[^0-9]", "");
                        if (value.isEmpty()) {
                            sender.sendMessage(chat("%prefix% &cInvalid number! &f" + args[1] + " &cis not a real number."));
                            return true;
                        }
                        int amount = Integer.parseInt(value);
                        if (amount > 1) plural = "s";
                        if (sender.hasPermission("fireballs.op") && (sender instanceof Player player)) {
                            givePlayerFireball(player, amount);
                            player.sendMessage(chat("%prefix% &eYou got " + amount + " fireball" + plural));
                            break;
                        }
                        sender.sendMessage(noPermission);

                        break;
                    case "give":
                        if (sender.hasPermission("fireballs.op")) {
                            boolean isValidPlayer = false;
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (p.getName().startsWith(args[1])) {
                                    givePlayerFireball(p, 1);
                                    sender.sendMessage(chat("%prefix% &aYou gave one fireball to " + args[1]));
                                    isValidPlayer = true;
                                    break;
                                }
                            }
                            if (!isValidPlayer) {
                                sender.sendMessage(chat("%prefix% &cThat player is not online!"));
                            }
                            break;
                        }
                        sender.sendMessage(noPermission);
                }

                return true;
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("give") && (sender.hasPermission("fireballs.op"))) {
                    Player found;
                    found = null;
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getName().equalsIgnoreCase(args[1])) {
                            found = p;
                            break;
                        }
                    }

                    if (found == null) sender.sendMessage(chat("%prefix% &cThat player is not online!"));
                    else {
                        String value = args[2].replaceAll("[^0-9]", "");
                        if (value.isEmpty()) {
                            sender.sendMessage(chat("%prefix% &cInvalid number! &f" + args[2] + " &cis not a real number."));
                            return true;
                        }
                        int intval = Integer.parseInt(value);
                        if (intval > 1) plural = "s";
                        givePlayerFireball(found, intval);
                        sender.sendMessage(chat("%prefix% &aYou gave " + intval + " fireball" + plural + " to " + found.getName()));
                        return true;
                    }
                }

                return true;
            }
        }

        return false;
    }

    protected void givePlayerFireball(Player player, int amount) {
        ItemStack fireball = new FireballItem().fireball();
        fireball.setAmount(Math.abs(amount));
        if (amount > 64) {
            for (int i = 65; i <= amount; i++) {
                fireball.setAmount(1);
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(fireball);
                } else {
                    player.getWorld().dropItemNaturally(player.getLocation(), fireball);
                }
            }
            return;
        }

        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(fireball);
        } else {
            player.getWorld().dropItemNaturally(player.getLocation(), fireball);
        }
    }
}
