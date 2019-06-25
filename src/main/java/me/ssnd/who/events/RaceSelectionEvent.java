package me.ssnd.who.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.ssnd.who.character.Race;

public class RaceSelectionEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final Player player;
	private final Race race;

	public RaceSelectionEvent(Player player, Race race) {
		this.player = player;
		this.race = race;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Race getRace() {
		return race;
	}

	public Player getPlayer() {
		return player;
	}

}
