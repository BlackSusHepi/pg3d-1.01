package dev.blockyarms;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** Sent from the client to the server when the player left-clicks with a gun. Carries no data. */
public record FirePayload() implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<FirePayload> TYPE =
			new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(BlockyArms.MOD_ID, "fire"));

	public static final StreamCodec<RegistryFriendlyByteBuf, FirePayload> CODEC =
			StreamCodec.unit(new FirePayload());

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
