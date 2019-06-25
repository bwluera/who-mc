package me.ssnd.who.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.ssnd.who.PlayerCache;
import me.ssnd.who.Who;
import me.ssnd.who.character.Ability;
import me.ssnd.who.config.AbilityConfig;
import me.ssnd.who.tasks.DelayBar;
import me.ssnd.who.tasks.GrandSlamSound;
import me.ssnd.who.utils.Common;

public class GrandSlamAbility implements Listener {

	private static long abilityDelay = AbilityConfig.abilities.getInt("grand-slam.cooldown-in-seconds") * 1000L;

	public static void reload() {
		abilityDelay = AbilityConfig.abilities.getInt("grand-slam.cooldown-in-seconds") * 1000L;
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player))
			return;

		final Player attacker = (Player) event.getDamager();
		final PlayerCache cache = Who.getCache(attacker.getUniqueId());

		if (cache.getRace() == null)
			return;

		if (!cache.getRace().hasAbility(Ability.GRAND_SLAM))
			return;

		final long currentTime = System.currentTimeMillis();
		final long abilityUseTime = !cache.abilityDelays.containsKey(Ability.GRAND_SLAM) ? 0L : cache.abilityDelays.get(Ability.GRAND_SLAM);
		final boolean abilityReady = !cache.abilityReady.containsKey(Ability.GRAND_SLAM) ? false : cache.abilityReady.get(Ability.GRAND_SLAM);

		if ((abilityUseTime == 0L || currentTime - abilityUseTime > abilityDelay) && abilityReady) {

			LivingEntity ent;
			Location loc;

			for (final Entity entity : event.getEntity().getLocation().getChunk().getEntities()) {
				final double maxDistance = cache.getGrandSlamCharge();
				if (event.getEntity().getLocation().distance(entity.getLocation()) <= maxDistance && entity.getType().isAlive() && !entity.equals(attacker)) {
					ent = (LivingEntity) entity;
					loc = entity.getLocation();

					ent.damage(event.getFinalDamage());
					loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 30, 0.0, 1.5, 0.0, 0.0, null);
				}
			}

			Common.sendActionBar(attacker, "&a&l...and bring it down on your enemies!");

			cache.abilityDelays.put(Ability.GRAND_SLAM, System.currentTimeMillis());
			cache.abilityReady.put(Ability.GRAND_SLAM, false);

			new DelayBar(attacker, "grand-slam").runTaskAsynchronously(Who.getInstance());
		}
	}

	@EventHandler
	public void onPlayerClick(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		final PlayerCache cache = Who.getCache(player.getUniqueId());

		final Action action = event.getAction();

		final long abilityUseTime = !cache.abilityDelays.containsKey(Ability.GRAND_SLAM) ? 0L : cache.getAbilityUseTime(Ability.GRAND_SLAM, true);
		final boolean abilityReady = !cache.abilityReady.containsKey(Ability.GRAND_SLAM) ? false : cache.abilityReady.get(Ability.GRAND_SLAM);

		if (cache.getRace() == null)
			return;

		if ((action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) && !(player.getInventory().getItemInOffHand().getType() == Material.SHIELD))

			if (player.getInventory().getItemInMainHand().toString().toLowerCase().contains("sword") && cache.getRace().hasAbility(Ability.GRAND_SLAM)) // TODO: check for item with meta title hammer

				if ((abilityUseTime == 0L || abilityUseTime > abilityDelay) && !abilityReady) {

					Common.sendActionBar(player, "&aYou raise your weapon...");

					new GrandSlamSound(player).runTaskAsynchronously(Who.getInstance());

					cache.setAbilityReady(Ability.GRAND_SLAM, true);

					Bukkit.getScheduler().scheduleSyncDelayedTask(Who.getInstance(), () -> {
						if (cache.getAbilityIsReady(Ability.GRAND_SLAM)) {
							Common.sendActionBar(player, "&7You lower your weapon...");
							cache.setAbilityReady(Ability.GRAND_SLAM, false);
						}
					}, 60L);

				}

	}
}
