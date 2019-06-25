package me.ssnd.who.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import me.ssnd.who.PlayerCache;
import me.ssnd.who.Who;
import me.ssnd.who.character.Race;
import me.ssnd.who.events.RaceSelectionEvent;

public class RaceListener implements Listener {

	@EventHandler
	public void onRaceSelect(RaceSelectionEvent e) {
		final Player player = e.getPlayer();
		final Race race = e.getRace();
		final PlayerCache cache = Who.getCache(player.getUniqueId());

		cache.setRace(race);
		cache.save();
	}

	@EventHandler
	public void onHealthRegen(EntityRegainHealthEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;

		final Player player = (Player) e.getEntity();
		final PlayerCache cache = Who.getCache(player.getUniqueId());
		final double regenRate = cache.getRace().getHealthRegenRate();

		if (regenRate == 1.0d)
			return;

		e.setAmount(e.getAmount() * regenRate);
	}

}
