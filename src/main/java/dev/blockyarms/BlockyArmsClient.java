package dev.blockyarms;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.player.LocalPlayer;

/** Detects left-clicks while holding a gun and asks the server to fire. */
public class BlockyArmsClient implements ClientModInitializer {
	private boolean wasDown = false;

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
			LocalPlayer player = minecraft.player;
			boolean down = minecraft.options.keyAttack.isDown();

			// Ignore clicks while a menu/chat is open.
			if (player == null || net.minecraft.client.Minecraft.getInstance().currentScreen != null) {
    return;
}

			if (down && player.getMainHandItem().getItem() instanceof GunItem gun) {
				if (!wasDown || gun.isAutomatic()) {
					ClientPlayNetworking.send(new FirePayload());
				}
			}
			wasDown = down;
		});
	}
}
