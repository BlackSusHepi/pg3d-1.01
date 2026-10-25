package dev.blockyarms;

import java.util.function.Supplier;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;

/**
 * Stats for one gun. Tweak the numbers in {@link ModItems} to rebalance.
 *
 * @param damage         damage per bullet/pellet (2.0 = one heart)
 * @param range          how far a shot travels, in blocks
 * @param pellets        bullets per shot (shotgun fires several)
 * @param spread         random sideways spread per pellet (0 = perfectly accurate)
 * @param cooldownTicks  time between shots (20 ticks = 1 second)
 * @param explosionPower 0 for none, otherwise explosion size at the impact point (TNT is 4)
 * @param ammo           the item consumed per shot (not consumed in creative mode)
 * @param sound          sound played when firing
 * @param pitch          pitch of that sound
 */
public record GunSpec(
		float damage,
		double range,
		int pellets,
		double spread,
		int cooldownTicks,
		float explosionPower,
		Supplier<Item> ammo,
		SoundEvent sound,
		float pitch) {
}
