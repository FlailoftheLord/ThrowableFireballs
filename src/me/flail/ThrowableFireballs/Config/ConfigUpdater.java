package me.flail.ThrowableFireballs.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigUpdater {

	public boolean updateConfig(FileConfiguration config) {
		Config confDb = new Config();

		File configFile = new File("plugins/ThrowableFireballs/config.yml");

		File backup = new File("plugins/ThrowableFireballs/old-config.yml");

		for (String option : confDb.options()) {
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
