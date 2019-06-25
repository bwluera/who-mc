package me.ssnd.who.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ssnd.who.utils.Common;

public abstract class PlayerCommand extends Command {

	protected PlayerCommand(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			Common.tell(sender, "&4You must be a player to use this command!");
			return true;
		}

		run((Player) sender, args);
		return true;
	}

	protected abstract void run(Player sender, String[] args);
}
