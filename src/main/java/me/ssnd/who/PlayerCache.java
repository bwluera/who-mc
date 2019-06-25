package me.ssnd.who;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ssnd.who.character.Ability;
import me.ssnd.who.character.Class;
import me.ssnd.who.character.Race;
import me.ssnd.who.config.RaceConfig;
import me.ssnd.who.utils.Common;

public class PlayerCache {
	private final FileConfiguration cache;
	private final File file = new File("plugins/Who/cache.dat");

	// RPG Elements
	private Race playerRace;
	private Class playerClass;



	// Character Elements
	private String name;
	private int age;
	private Sex sex;
	private int height;
	private String description;

	public static enum Sex {
		MALE,
		FEMALE
	}

	public HashMap<Ability, Long> abilityDelays = new HashMap<>();
	public HashMap<Ability, Boolean> abilityReady = new HashMap<>();

	private float GrandSlamCharge = 3.0f;

	private final UUID uuid;

	public PlayerCache(UUID id) {
		if (file.exists()) {
			cache = YamlConfiguration.loadConfiguration(file);

			try {
				cache.load(file);

			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
		else {
			Who.getInstance().saveResource("cache.dat", false);
			cache = YamlConfiguration.loadConfiguration(file);
			Common.log("&2Cache.dat file successfully created.");
		}

		this.uuid = id;

		onLoad();
	}

	private void onLoad() {
		setRace(cache.getString(uuid + ".race") == null ? null : RaceConfig.fromString(cache.getString(uuid + ".race")));
		// TODO: setClass

		setName(cache.getString(uuid + ".name", ""));
		setAge(cache.getInt(uuid + ".age", 0));
		setSex(cache.isSet(uuid + ".sex") ? Sex.valueOf(cache.getString(uuid + ".sex")) : null);
		setHeight(cache.getInt(uuid + ".height", 0));
		setDescription(cache.getString(uuid + ".description", ""));
	}

	public void save() {
		if (getRace() != null)
			cache.set(uuid + ".race", getRace().getName());
		if (getName() != "")
			cache.set(uuid + ".name", getName());
		if (getAge() != 0)
			cache.set(uuid + ".age", getAge());
		if (getSex() != null)
			cache.set(uuid + ".sex", getSex().toString());
		if (getHeight() != 0)
			cache.set(uuid + ".height", getHeight());
		if (getDescription() != "")
			cache.set(uuid + ".description", getDescription());

		try {
			cache.save(file);
			cache.load(file);
		} catch (final IOException | InvalidConfigurationException e) {
			Common.log("&4Failed to save or load player cache from " + file);
			e.printStackTrace();
		}
	}

	public Race getRace() {
		return playerRace;
	}

	public void setRace(Race playerRace) {
		this.playerRace = playerRace;
	}

	public Class getPlayerClass() {
		return playerClass;
	}

	public void setPlayerClass(Class playerClass) {
		this.playerClass = playerClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAbilityUseTime(Ability ability) {
		abilityDelays.put(ability, System.currentTimeMillis());
	}

	public long getAbilityUseTime(Ability ability, boolean minusCurrentTime) {
		if (minusCurrentTime)
			return System.currentTimeMillis() - abilityDelays.get(ability);

		return abilityDelays.get(ability);
	}

	public void setAbilityReady(Ability ability, boolean isReady) {
		abilityReady.put(ability, isReady);
	}

	public boolean getAbilityIsReady(Ability ability) {
		return abilityReady.get(ability);
	}

	public float getGrandSlamCharge() {
		return GrandSlamCharge;
	}

	public void setGrandSlamCharge(float grandSlamCharge) {
		GrandSlamCharge = grandSlamCharge;
	}

}
