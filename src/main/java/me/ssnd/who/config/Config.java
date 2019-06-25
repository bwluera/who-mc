package me.ssnd.who.config;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ssnd.who.Who;
import me.ssnd.who.utils.Common;

public class Config {

	public static FileConfiguration config;
	private static File file;

	private final Who pl;

	public Config(String fileName) {
		file = new File(fileName);
		pl = Who.getInstance();

		try {
			if (file.exists()) {
				config = YamlConfiguration.loadConfiguration(file);
				config.load(file);
				pl.reloadConfig();
				pl.saveDefaultConfig();
			}
			else {
				pl.saveResource("config.yml", false);
				config = YamlConfiguration.loadConfiguration(file);
				Common.log("Configuration file successfully created.");
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void onLoad() {

	}


	public static void init() {
		new Config("plugins/Who/config.yml").onLoad();
	}
}
