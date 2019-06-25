package me.ssnd.who.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.ssnd.who.PlayerCache;
import me.ssnd.who.Who;

public class PlayerListener implements Listener {

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e) { // automatically register cache on join
		final Player player = e.getPlayer();
		final PlayerCache cache = Who.getCache(player.getUniqueId());

		if (!cache.getName().isEmpty()) // TODO: add config that disable name completely
			player.setDisplayName(cache.getName());

	}

}
