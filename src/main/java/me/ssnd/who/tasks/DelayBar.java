package me.ssnd.who.tasks;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.ssnd.who.config.AbilityConfig;
import me.ssnd.who.utils.Common;

public class DelayBar extends BukkitRunnable {

	Player player;
	String ability;
	String abilityName;
	long cooldown;
	//long timeLeft;

	public DelayBar(Player player, String ability) {
		this.player = player;
		this.ability = ability;
		this.abilityName = AbilityConfig.abilities.getString(ability + ".name");
		this.cooldown = AbilityConfig.abilities.getLong(ability + ".cooldown-in-seconds") * 1000L;
		//this.timeLeft = cooldown;
	}

	@Override
	public void run() {
		BossBar bar = null;
		boolean created = false;

		for (long timeLeft = cooldown; timeLeft >= 0L; timeLeft = timeLeft - 1000L) {
			if (!created) {
				bar = Bukkit.createBossBar(Common.colorize(abilityName + "&r&7 cooling down..."), BarColor.BLUE, BarStyle.SOLID);

				bar.addPlayer(this.player);
				bar.setVisible(true);

				created = true;
			} else {
				bar.setProgress((double) timeLeft / (double) cooldown);
				if (timeLeft == 0L)
					bar.setTitle(Common.colorize(this.abilityName + "&r&a ready!"));
			}

			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {}
		}

		if (created) {
			bar.removeAll();
			bar.setVisible(false);
		}

	}

}
