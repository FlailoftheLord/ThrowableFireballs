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

package me.flail.throwablefireballs.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.bukkit.configuration.file.FileConfiguration;

import me.flail.throwablefireballs.tools.Tools;

public class ConfigUpdater extends Tools {

	public boolean updateConfig(FileConfiguration config) {
		Config confDb = plugin.configDB;

		File configFile = new File("plugins/ThrowableFireballs/config.yml");

		File backup = new File("plugins/ThrowableFireballs/old-config.yml");

		for (String option : confDb.getConfigurationOptions().keySet()) {
			if (!config.contains(option)) {

				try {
					BufferedReader reader = new BufferedReader(new FileReader(configFile));
					BufferedWriter writer = new BufferedWriter(new FileWriter(backup));

					for (Object obj : reader.lines().toArray()) {
						String line = obj.toString();
						writer.append(line + "\n");

					}

					writer.flush();
					writer.close();
					reader.close();

					return this.forceUpdate(confDb, configFile);

				} catch (Throwable t) {
					return false;
				}

			}

		}

		return true;
	}

	private boolean forceUpdate(Config confDb, File file) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));

			writer.write(confDb.configurationDatabase());

			writer.close();

			return true;
		} catch (Throwable t) {
			return false;
		}

	}

}
