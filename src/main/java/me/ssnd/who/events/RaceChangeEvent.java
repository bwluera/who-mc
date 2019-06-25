package me.ssnd.who.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.ssnd.who.character.Race;

public class RaceChangeEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final Player player;
	private final Race oldRace;
	private final Race newRace;

	public RaceChangeEvent(Player player, Race oldRace, Race newRace) {
		this.player = player;
		this.oldRace = oldRace;
		this.newRace = newRace;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Player getPlayer() {
		return player;
	}

	public Race getOldRace() {
		return oldRace;
	}

	public Race getNewRace() {
		return newRace;
	}

}
