package me.ssnd.who.commands;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import me.ssnd.who.Who;
import me.ssnd.who.character.Race;
import me.ssnd.who.config.RaceConfig;
import me.ssnd.who.events.RaceSelectionEvent;
import me.ssnd.who.utils.Common;

public class RaceCommand extends PlayerCommand {

	private final PluginManager pm = Who.getInstance().getServer().getPluginManager();

	public RaceCommand() {
		super("race");
	}

	@Override
	protected void run(Player sender, String[] args) {
		if (!sender.hasPermission("who.race")) {
			Common.tell(sender, Who.prefix + "&cYou do not have access to that command!");
			return;
		}

		if (args.length == 1)
			return;
		else if (args.length == 2) // TODO: permissions
			if (args[0].equals("select"))
				if (!Race.isValid(args[1])) {
					Common.tell(sender, Who.prefix + "&cThat is not a valid race!");
					return;
				}
				else {
					final Race race = RaceConfig.fromString(args[1].toLowerCase());
					pm.callEvent(new RaceSelectionEvent(sender, race));
				}

	}

}
