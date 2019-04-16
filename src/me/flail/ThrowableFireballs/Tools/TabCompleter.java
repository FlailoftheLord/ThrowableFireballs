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
		String[] mainArgs = { "reload", "updateconfig", "give", "get", "about" };

		try {
			if (command.getName().equalsIgnoreCase("throwablefireballs")) {
				switch (args.length) {
				case 1:
					String arg = args[0].toLowerCase();
					for (String s : mainArgs) {
						if (s.startsWith(arg)) {
							this.add(s);
						}
					}

					break;
				case 2:
					arg = args[0].toLowerCase();
					switch (arg) {
					case "get":
						for (int n = 01; n < 65; n++) {
							this.add(n + "");
						}

						break;
					case "give":
						for (Player p : Bukkit.getOnlinePlayers()) {
							this.add(p.getName());
						}

					}

					break;
				case 3:
					arg = args[0].toLowerCase();
					if (arg.contains("give")) {
						for (int n = 01; n < 65; n++) {
							this.add(n + "");
						}
					}

				}

			}
		} catch (Throwable t) {
		}

		return this;
	}


}
