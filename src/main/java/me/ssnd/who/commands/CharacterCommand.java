package me.ssnd.who.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import me.ssnd.who.CharacterCreator;
import me.ssnd.who.PlayerCache;
import me.ssnd.who.Who;
import me.ssnd.who.utils.Common;
import me.ssnd.who.utils.WhoUtils;

public class CharacterCommand extends PlayerCommand {

	private final PluginManager pm = Who.getInstance().getServer().getPluginManager();

	public CharacterCommand() {
		super("character");

		this.setAliases(Arrays.asList("char"));
	}

	@Override
	protected void run(Player sender, String[] args) {
		if (!sender.hasPermission("who.character")) {
			Common.tell(sender, Who.prefix + "&cYou do not have access to that command!");
			return;
		}

		final PlayerCache cache = Who.getCache(sender.getUniqueId());

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("setup")) {

				if (!sender.hasPermission("who.character.setup")) {
					Common.tell(sender, Who.prefix + "&cYou do not have access to that command!");
					return;
				}

				if (cache.getName() == null || cache.getName().isEmpty()) {
					Common.tell(sender, "&8[&7CC&8]&7 Type 'quit' to stop creation at any time.");
					new CharacterCreator(sender);

					return;
				}

				Common.tell(sender, "&8[&7CC&8]&c You have already set up your character! Use /character, /race, and /class to see about changing details.");
				return;

			}
			else if (args[0].equalsIgnoreCase("info")) {

				if (!sender.hasPermission("who.character.info")) {
					Common.tell(sender, Who.prefix + "&cYou do not have access to that command!");
					return;
				}

				WhoUtils.displayCharacterInfo(sender, sender);
			}
		}
		else if (args.length == 2)
			if (args[0].equalsIgnoreCase("info")) {

				final int a[][] = new int[2][3];

				if (!sender.hasPermission("who.character.info")) {
					Common.tell(sender, Who.prefix + "&cYou do not have access to that command!");
					return;
				}

				boolean foundPlayer = false;
				Player found = null;

				for (final Player player : Bukkit.getOnlinePlayers())
					if (player.getName().equalsIgnoreCase(args[1])) {
						foundPlayer = true;
						found = player;
					}


				if (!foundPlayer) {
					Common.tell(sender, Who.prefix + "&cThat player is not online or does not exist.");
					return;
				}

				WhoUtils.displayCharacterInfo(sender, found);
			}

	}

}
