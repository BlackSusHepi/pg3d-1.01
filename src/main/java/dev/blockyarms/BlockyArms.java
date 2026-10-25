package dev.blockyarms;

import net.fabricmc.api.ModInitializer;

public class BlockyArms implements ModInitializer {
	public static final String MOD_ID = "blockyarms";

	@Override
	public void onInitialize() {
		ModItems.init();
	}
}
