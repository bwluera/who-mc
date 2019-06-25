package me.ssnd.who.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ssnd.who.Who;
import me.ssnd.who.character.Ability;
import me.ssnd.who.character.Race;
import me.ssnd.who.utils.Common;

public class RaceConfig {
	public static FileConfiguration races;
	private static File file;

	public static List<Race> RACE_LIST = new ArrayList<>();

	private RaceConfig(String filePath) {
		file = new File(filePath);
		try {
			if (file.exists()) {
				races = YamlConfiguration.loadConfiguration(file);
				races.load(file);
			}
			else {
				Who.getInstance().saveResource("races.yml", false);
				races = YamlConfiguration.loadConfiguration(file);
				Common.log("&9Races file successfully created.");
			}
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void onLoad() {
		String raceName;
		List<String> description;
		final List<Ability> abilities = new ArrayList<>();
		final HashMap<String, Object> traits = new HashMap<>();

		for (final String race : races.getKeys(false)) {
			raceName = races.getString(race + ".name");
			description = races.getStringList(race + ".description");
			for (final String ability : races.getStringList(race + ".abilities")) {
				final Ability a = Ability.valueOf(ability);
				abilities.add(a);
			}

			for (final String trait : races.getConfigurationSection(race).getKeys(false))
				traits.put(trait, races.get(race + "." + trait));

			RACE_LIST.add(new Race(race, raceName, description, abilities, traits));
		}
	}

	public static void reload() {
		RACE_LIST.clear();

		try {
			races.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			Common.log("&4Could not load configuration from races.yml while reloading!");
			return;
		}

		String raceName;
		List<String> description;
		final List<Ability> abilities = new ArrayList<>();
		final HashMap<String, Object> traits = new HashMap<>();

		for (final String race : races.getKeys(false)) {
			raceName = races.getString(race + ".name");
			description = races.getStringList(race + ".description");
			for (final String ability : races.getStringList(race + ".abilities")) {
				final Ability a = Ability.valueOf(ability); // TODO: Error message (what if ability doesn't exist or is not named properly by the user?)
				abilities.add(a);
			}

			for (final String trait : races.getConfigurationSection(race).getKeys(false))
				traits.put(trait, races.get(race + "." + trait));

			RACE_LIST.add(new Race(race, raceName, description, abilities, traits));
		}
	}

	public static void init() {
		new RaceConfig("plugins/Who/races.yml").onLoad();
	}

	public static Race fromString(String raceName) {
		for (final Race race : RACE_LIST)
			if (race.getName().equalsIgnoreCase(raceName))
				return race;

		return null;
	}
}
