package me.ssnd.who.utils;

import org.bukkit.entity.Player;

import me.ssnd.who.PlayerCache;
import me.ssnd.who.Who;

public class WhoUtils {
	public static void displayCharacterInfo(Player requester, Player character) {
		final PlayerCache cache = Who.getCache(character.getUniqueId());

		if (cache.getName() == null || cache.getName().isEmpty()) {
			Common.tell(requester, Who.prefix + "&cPlayer &4" + character.getName() + " &chas not set up their character yet.");
			return;
		}

		//TODO: add class

		//TODO: add level?

		final String height = (cache.getHeight() / 12) + "'" + (cache.getHeight() % 12) + "\"";

		Common.tell(requester,
				"&8&m--------&r " + Who.prefix + " &8&m--------",
				"&3User&8: &9" + character.getName(),
				"&3Name&8: &b" + cache.getName(),
				"&3Race&8: &b" + cache.getRace().getName(),
				"&3Age&8: &b" + cache.getAge(),
				"&3Height&8: &b" + height,
				"&3Description&8: &b" + cache.getDescription(),
				"&8&m-----------------------");
	}
}
