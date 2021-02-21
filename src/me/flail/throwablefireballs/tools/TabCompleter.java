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

package me.flail.throwablefireballs.tools;

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
