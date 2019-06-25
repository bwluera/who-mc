package me.ssnd.who.character;

import java.util.HashMap;
import java.util.List;

import me.ssnd.who.config.RaceConfig;

public class Race {

	private String path;
	private String name;
	private List<String> description;
	private List<Ability> abilities;
	private HashMap<String, Object> traits;

	public Race(String path, String name, List<String> description, List<Ability> abilities, HashMap<String, Object> traits) {
		this.setPath(path);
		this.setName(name);
		this.setDescription(description);
		this.setAbilities(abilities);
		this.setTraits(traits);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getDescription() {
		return description;
	}

	public void setDescription(List<String> description) {
		this.description = description;
	}

	public List<Ability> getAbilities() {
		return abilities;
	}

	public void setAbilities(List<Ability> abilities) {
		this.abilities = abilities;
	}

	public HashMap<String, Object> getTraits() {
		return traits;
	}

	public void setTraits(HashMap<String, Object> traits) {
		this.traits = traits;
	}

	public boolean hasAbility(Ability a) {
		return abilities.contains(a);
	}

	public double getStartingHP() {
		return RaceConfig.races.getDouble(path + ".starting-hp");
	}

	public double getHealthRegenRate() {
		return RaceConfig.races.getDouble(path + ".regen-rate");
	}

	public static boolean isValid(String toValidate) {
		if (RaceConfig.fromString(toValidate) == null)
			return false;

		return true;
	}




}
