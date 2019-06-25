package me.ssnd.who.utils;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import me.ssnd.who.Who;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Common {

	public static void sendActionBar(Player player, String title) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(colorize(title)));
	}

	public static void tell(CommandSender sender, String message) {
		sender.sendMessage(colorize(message));
	}

	public static void tell(CommandSender sender, String... messages) {
		for (final String message : messages)
			sender.sendMessage(colorize(message));
	}

	public static String colorize(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static void log(String message) {
		tell(Bukkit.getConsoleSender(), "[" + Who.getInstance().getName() + "] " + message);
	}

	public static void registerEvents(Who plugin, Listener... listeners) {
		final PluginManager pm = plugin.getServer().getPluginManager();

		for (final Listener lis : listeners)
			pm.registerEvents(lis, plugin);
	}

	public static void registerCommand(Command command) {
		try {
			final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);

			final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
			commandMap.register(command.getLabel(), command);
		} catch (final Exception e) {
			e.printStackTrace();
			log("&4Could not register command: " + command.getName());
		}
	}

	public static void registerCommands(Command...commands) {
		for (final Command command : commands)
			registerCommand(command);
	}
}
