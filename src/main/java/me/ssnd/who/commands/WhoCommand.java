package me.ssnd.who.commands;

import org.bukkit.entity.Player;

import me.ssnd.who.Who;
import me.ssnd.who.utils.Common;

public class WhoCommand extends PlayerCommand {

	public WhoCommand() {
		super("who");
	}

	@Override
	protected void run(Player sender, String[] args) {
		if (!sender.hasPermission("who.who")) {
			Common.tell(sender, Who.prefix + "&cYou do not have access to that command!");
			return;
		}

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("reload")) {
				if (!sender.hasPermission("who.reload"))
					Common.tell(sender, Who.prefix + "&cYou do not have access to that command!");

				Who.getInstance().reload();
			}
			return;
		}

	}

}
