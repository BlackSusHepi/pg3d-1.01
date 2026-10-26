package dev.blockyarms;

import java.util.Optional;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/** A hitscan gun: right-click to fire instantly along your crosshair. */
public class GunItem extends Item {
	private final GunSpec spec;

	public GunItem(Properties properties, GunSpec spec) {
		super(properties);
		this.spec = spec;
	}

	/** Right-click no longer fires; shooting is triggered by left-click (see BlockyArmsClient). */
	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		return InteractionResult.PASS;
	}

	/** Fast guns keep firing while the button is held; the others fire once per click. */
	public boolean isAutomatic() {
		return spec.cooldownTicks() <= 4;
	}

	/** Called on the server when the player left-clicks while holding this gun. */
	public void tryFire(ServerLevel level, Player player, InteractionHand hand) {
		ItemStack gun = player.getItemInHand(hand);

		if (player.getCooldowns().isOnCooldown(gun)) {
			return;
		}

		if (!hasAmmo(player)) {
			level.playSound(null, player.blockPosition(), SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 0.8f, 1.4f);
			player.getCooldowns().addCooldown(gun, 10);
			return;
		}

		consumeAmmo(player);
		fire(level, player);
		player.swing(hand, true);
		player.getCooldowns().addCooldown(gun, spec.cooldownTicks());
	}

	private boolean hasAmmo(Player player) {
		if (player.hasInfiniteMaterials()) {
			return true;
		}
		Inventory inventory = player.getInventory();
		Item ammo = spec.ammo().get();
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			if (inventory.getItem(i).is(ammo)) {
				return true;
			}
		}
		return false;
	}

	private void consumeAmmo(Player player) {
		if (player.hasInfiniteMaterials()) {
			return;
		}
		Inventory inventory = player.getInventory();
		Item ammo = spec.ammo().get();
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (stack.is(ammo)) {
				stack.shrink(1);
				return;
			}
		}
	}

	private void fire(ServerLevel level, Player player) {
		level.playSound(null, player.blockPosition(), spec.sound(), SoundSource.PLAYERS, 1.0f, spec.pitch());

		RandomSource random = player.getRandom();
		Vec3 eye = player.getEyePosition();

		for (int i = 0; i < spec.pellets(); i++) {
			Vec3 direction = player.getLookAngle();
			if (spec.spread() > 0) {
				direction = direction.add(
						random.triangle(0.0, spec.spread()),
						random.triangle(0.0, spec.spread()),
						random.triangle(0.0, spec.spread())).normalize();
			}
			shootRay(level, player, eye, direction);
		}
	}

	private void shootRay(ServerLevel level, Player player, Vec3 eye, Vec3 direction) {
		Vec3 end = eye.add(direction.scale(spec.range()));

		// 1) Where does the shot hit a wall (or run out of range)?
		BlockHitResult blockHit = level.clip(
				new ClipContext(eye, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
		Vec3 stop = blockHit.getType() == HitResult.Type.MISS ? end : blockHit.getLocation();

		// 2) Is there a creature in the way before that?
		Entity target = null;
		double bestDistance = eye.distanceToSqr(stop);
		AABB searchBox = new AABB(eye, stop).inflate(1.0);
		for (Entity candidate : level.getEntities(player, searchBox, e -> e.isPickable() && !e.isSpectator())) {
			Optional<Vec3> hit = candidate.getBoundingBox().inflate(0.1).clip(eye, stop);
			if (hit.isPresent()) {
				double distance = eye.distanceToSqr(hit.get());
				if (distance < bestDistance) {
					bestDistance = distance;
					target = candidate;
					stop = hit.get();
				}
			}
		}

		if (target != null) {
			target.hurtServer(level, level.damageSources().playerAttack(player), spec.damage());
		}

		// 3) Draw the bullet trail.
		double length = eye.distanceTo(stop);
		for (double d = 1.0; d < length; d += 0.75) {
			Vec3 p = eye.add(direction.scale(d));
			level.sendParticles(ParticleTypes.CRIT, p.x, p.y, p.z, 1, 0.0, 0.0, 0.0, 0.0);
		}

		// 4) Rockets explode where they land.
		if (spec.explosionPower() > 0) {
			Vec3 boom = stop.subtract(direction.scale(0.2));
			level.explode(player, boom.x, boom.y, boom.z, spec.explosionPower(), Level.ExplosionInteraction.MOB);
		}
	}
}
