package me.flail.ThrowableFireballs.Tools;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class TabCompleter extends ArrayList<String> {
	private Command command;
	/**
	 * UID
	 */
	private static final long serialVersionUID = 8202587163765471021L;

	public TabCompleter(Command command) {
		this.command = command;
	}

	public TabCompleter construct(String[] args) {
		try {
			String[] arguments = command.getUsage().split(" ");
			for (String line : this.parseArgs(arguments[args.length])) {
				if (line.startsWith(args[args.length - 1])
						|| (line.contains("%") && line.split("%")[1].startsWith(args[args.length - 1]))) {
					if (line.contains("%")) {
						if ((args.length > 1) && line.split("%")[0].equalsIgnoreCase(args[args.length - 2])) {
							String finalArg = line.split("%")[1];

							switch (finalArg) {
							case "player-name":
								for (Player p : Bukkit.getOnlinePlayers()) {
									this.add(p.getName());
								}
								break;
							case "amount":
								for (int a = 1; a < 65; a++) {
									this.add("" + a);
								}
								break;
							default:
								this.add(finalArg);
							}
						}

						continue;
					}

					this.add(line);
				}

			}
		} catch (Throwable t) {
		}

		return this;
	}

	protected String[] parseArgs(String line) {
		String[] chars = { "<", ">", "[", "]" };

		return removeChars(line, chars).split(":");
	}

	protected String removeChars(String message, String[] chars) {
		String modified = message;
		for (String c : chars) {
			modified = modified.replace(c, "");
		}

		return modified;
	}

}
