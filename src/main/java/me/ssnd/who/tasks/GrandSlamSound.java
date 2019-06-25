package me.ssnd.who.tasks;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.ssnd.who.PlayerCache;
import me.ssnd.who.Who;
import me.ssnd.who.character.Ability;
import me.ssnd.who.utils.Common;

public class GrandSlamSound extends BukkitRunnable {

	Player player;
	PlayerCache cache;
	Location loc;
	World world;

	public GrandSlamSound(Player player) {
		this.player = player;
		this.loc = player.getLocation();
		this.world = loc.getWorld();
		this.cache = Who.getCache(player.getUniqueId());
	}

	@Override
	public void run() {
		float pitch = 0.50f;

		boolean usedAbility = false;

		for (long time = 0L; time < 3000L; time = time + 100L) {

			if (time % 800L == 0L && time != 0L) {
				cache.setGrandSlamCharge(cache.getGrandSlamCharge() + 1.0f);
				world.playSound(loc, Sound.BLOCK_STONE_PRESSUREPLATE_CLICK_ON, 0.75f, pitch);
			}

			if (!cache.getAbilityIsReady(Ability.GRAND_SLAM)) {

				world.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_BLAST_FAR, 2.0f, 0.50f);

				usedAbility = true;

				break;
			}


			world.playSound(loc, Sound.BLOCK_NOTE_BASS, 0.75f, pitch);

			pitch = pitch + 0.050f;

			Common.log(cache.getGrandSlamCharge() + "");

			try {
				Thread.sleep(100L);
			} catch (final InterruptedException e) {}
		}

		if (!usedAbility) {
			world.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 2.0f, 0.40f);
			cache.setGrandSlamCharge(3.0f);
			return;
		}

		world.spawnParticle(Particle.FALLING_DUST, player.getLocation(), 20, 0.3, 1.3, 0.0, 0, null);
		world.spawnParticle(Particle.FALLING_DUST, player.getLocation(), 20, 0.3, 1.3, 0.3, 0, null);
		world.spawnParticle(Particle.FALLING_DUST, player.getLocation(), 20, 0.0, 1.3, 0.3, 0, null);
		world.spawnParticle(Particle.FALLING_DUST, player.getLocation(), 20, 0.0, 0.3, 0.0, 0, null);
		world.spawnParticle(Particle.FALLING_DUST, player.getLocation(), 20, -0.3, 1.3, -0.3, 0, null);
		world.spawnParticle(Particle.FALLING_DUST, player.getLocation(), 20, -0.3, 1.3, 0.0, 0, null);
		world.spawnParticle(Particle.FALLING_DUST, player.getLocation(), 20, 0.0, 1.3, -0.3, 0, null);

		cache.setGrandSlamCharge(3.0f);

	}

}