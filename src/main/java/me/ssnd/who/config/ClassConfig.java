package me.ssnd.who.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ssnd.who.Who;
import me.ssnd.who.utils.Common;

public class ClassConfig {

	private FileConfiguration classes;

	private ClassConfig(String filePath) {
		final File file = new File(filePath);
		try {
			if (file.exists()) {
				classes = YamlConfiguration.loadConfiguration(file);
				classes.load(file);
			}
			else {
				Who.getInstance().saveResource("classes.yml", false);
				classes = YamlConfiguration.loadConfiguration(file);
				Common.log("&9Classes file successfully created.");
			}
		}catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void onLoad() {
	}

	public static void init() {
		new ClassConfig("plugins/Who/classes.yml").onLoad();
	}
}
