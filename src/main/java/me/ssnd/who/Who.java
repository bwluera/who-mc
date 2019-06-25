package me.ssnd.who;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import me.ssnd.who.commands.CharacterCommand;
import me.ssnd.who.config.AbilityConfig;
import me.ssnd.who.config.Config;
import me.ssnd.who.config.RaceConfig;
import me.ssnd.who.listeners.DoubleJumpAbility;
import me.ssnd.who.listeners.GrandSlamAbility;
import me.ssnd.who.listeners.PlayerListener;
import me.ssnd.who.listeners.RaceListener;
import me.ssnd.who.utils.Common;

public class Who extends JavaPlugin {

	public static final String prefix = "&f[&9Who&l?&r&f]&r ";

	private static final Cache<UUID, PlayerCache> playerCache = CacheBuilder.newBuilder()
			.maximumSize(10_000)
			.expireAfterWrite(240, TimeUnit.MINUTES)
			.build();

	private static Who instance;

	// TODO: ClassSelect / ClassChange events

	@Override
	public void onEnable() {
		instance = this;

		registerFiles();

		Common.registerEvents(this, new PlayerListener(),
				new RaceListener(),
				new DoubleJumpAbility(),
				new GrandSlamAbility());

		Common.registerCommand(new CharacterCommand());

		Common.log("Initialized Who, v" + this.getDescription().getVersion());
	}

	@Override
	public void onDisable() {
		instance = null;
	}

	public void reload() {
		RaceConfig.reload();
		AbilityConfig.reload();
		DoubleJumpAbility.reload();
	}

	public static PlayerCache getCache(UUID id) {
		PlayerCache cache = playerCache.getIfPresent(id);

		if (cache == null) {
			cache = new PlayerCache(id);

			playerCache.put(id, cache);
		}

		return cache;
	}

	private void registerFiles() {
		Config.init();
		RaceConfig.init();
		AbilityConfig.init();
	}

	public static Who getInstance() {
		return instance;
	}
}
