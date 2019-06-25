package me.ssnd.who.listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import me.ssnd.who.PlayerCache;
import me.ssnd.who.Who;
import me.ssnd.who.character.Ability;
import me.ssnd.who.config.AbilityConfig;
import me.ssnd.who.tasks.DelayBar;
import me.ssnd.who.utils.Common;

public class DoubleJumpAbility implements Listener {

	private static long abilityDelay = AbilityConfig.abilities.getInt("double-jump.cooldown-in-seconds") * 1000L;

	public static void reload() {
		abilityDelay = AbilityConfig.abilities.getInt("double-jump.cooldown-in-seconds") * 1000L;
	}

	private void groundUpdate(Player player) {
		Location location = player.getPlayer().getLocation();
		location = location.subtract(0, 1, 0);

		final Block block = location.getBlock();

		final PlayerCache cache = Who.getCache(player.getUniqueId());

		if (!block.getType().isSolid())
			return;

		if (cache.getRace() == null)
			return;

		if (cache.getRace().hasAbility(Ability.DOUBLE_JUMP))
			player.setAllowFlight(true);

	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		final PlayerCache cache = Who.getCache(player.getUniqueId());


		if (cache.getRace() == null)
			return;

		final float currentFall = event.getPlayer().getFallDistance();
		if (currentFall > 10.0f && cache.getRace().hasAbility(Ability.DOUBLE_JUMP) && player.getLocation().subtract(0,1,0).getBlock().getType().isSolid())
			event.getPlayer().damage(player.getFallDistance() - 3.0);

		if (!(currentFall > 1.0))
			groundUpdate(player);
	}

	@EventHandler
	public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE || event.getPlayer().getGameMode() == GameMode.SPECTATOR)
			return;

		final Player player = event.getPlayer();
		final PlayerCache cache = Who.getCache(player.getUniqueId());

		if (cache.getRace() == null)
			return;

		if (cache.getRace().hasAbility(Ability.DOUBLE_JUMP)) {
			event.setCancelled(true);
			DoubleJump(player);
		}
	}

	public void useDoubleJump(Player player, boolean isSprinting) {
		if (isSprinting)
			player.setVelocity(player.getLocation().getDirection().multiply(0.20d).setY(0.45d));
		else
			player.setVelocity(player.getLocation().getDirection().multiply(0.135d).setY(0.45d));

		final PlayerCache cache = Who.getCache(player.getUniqueId());

		if (cache.getRace() == null)
			return;

		cache.abilityDelays.put(Ability.DOUBLE_JUMP, System.currentTimeMillis());

		Common.sendActionBar(player, "&aUsed " + AbilityConfig.abilities.getString("double-jump.name"));
	}

	public void DoubleJump(Player player) {
		player.setAllowFlight(false);

		final PlayerCache cache = Who.getCache(player.getUniqueId());
		final long currentTime = System.currentTimeMillis();
		final long abilityUseTime = !cache.abilityDelays.containsKey(Ability.DOUBLE_JUMP) ? 0L : cache.abilityDelays.get(Ability.DOUBLE_JUMP);

		if (cache.getRace() == null)
			return;

		if ((abilityUseTime == 0L) || (currentTime - abilityUseTime > abilityDelay)) {
			// TODO: set ability cooldown in config not hardcoded / perk for reduced cooldown?
			if (!(player.getFallDistance() > 0.0)) {
				useDoubleJump(player, player.isSprinting());
				new DelayBar(player, "double-jump").runTaskAsynchronously(Who.getInstance());
				return;
			}
		}
		else if (currentTime - abilityUseTime < abilityDelay)
			return;
	}


}
