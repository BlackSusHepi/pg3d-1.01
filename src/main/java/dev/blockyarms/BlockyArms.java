package dev.blockyarms;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

public class BlockyArms implements ModInitializer {
	public static final String MOD_ID = "blockyarms";

	@Override
	public void onInitialize() {
		ModItems.init();

		// Left-click fire: the client sends this packet, the server does the shooting.
		PayloadTypeRegistry.serverboundPlay().register(FirePayload.TYPE, FirePayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(FirePayload.TYPE, (payload, context) -> {
			ServerPlayer player = context.player();
			if (player.isSpectator()) {
				return;
			}
			if (player.getMainHandItem().getItem() instanceof GunItem gun
					&& player.level() instanceof ServerLevel level) {
				gun.tryFire(level, player, InteractionHand.MAIN_HAND);
			}
		});

		// While holding a gun, left-click must not break blocks or punch things.
		AttackBlockCallback.EVENT.register((player, level, hand, pos, direction) ->
				player.getItemInHand(hand).getItem() instanceof GunItem
						? InteractionResult.FAIL
						: InteractionResult.PASS);

		AttackEntityCallback.EVENT.register((player, level, hand, entity, hitResult) ->
				player.getItemInHand(hand).getItem() instanceof GunItem
						? InteractionResult.FAIL
						: InteractionResult.PASS);
	}
}
