package me.ssnd.who.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ssnd.who.Who;
import me.ssnd.who.utils.Common;

public class AbilityConfig {
	public static FileConfiguration abilities;
	private static File file;

	private AbilityConfig(String filePath) {
		file = new File(filePath);
		try {
			if (file.exists()) {
				abilities = YamlConfiguration.loadConfiguration(file);
				abilities.load(file);
				Common.log("&9Loaded abilities file.");
			}
			else {
				Who.getInstance().saveResource("abilities.yml", false);
				abilities = YamlConfiguration.loadConfiguration(file);
				Common.log("&9Abilities file successfully created.");
			}
		}catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void onLoad() {
	}

	public static void reload() {
		try {
			abilities.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			Common.log("&4Could not load configuration from abilities.yml!");
		}
	}

	public static void init() {
		new AbilityConfig("plugins/Who/abilities.yml").onLoad();
	}
}
