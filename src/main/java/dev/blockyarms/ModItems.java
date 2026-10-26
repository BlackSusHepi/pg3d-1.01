package dev.blockyarms;

import java.util.function.Function;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public final class ModItems {
	// ---- Ammo ----
	public static final Item BULLET = register("bullet", Item::new, new Item.Properties());
	public static final Item SHELL = register("shell", Item::new, new Item.Properties());
	public static final Item ROCKET = register("rocket", Item::new, new Item.Properties());

	// ---- Guns (edit the numbers here to rebalance) ----
	//                                       dmg  range pellets spread cooldown boom  ammo            sound                                pitch
	public static final Item PISTOL = gun("pistol",
			new GunSpec(5.0f, 60, 1, 0.0, 8, 0.0f, () -> BULLET, SoundEvents.CROSSBOW_SHOOT, 1.6f));

	public static final Item SHOTGUN = gun("shotgun",
			new GunSpec(3.0f, 24, 8, 0.07, 22, 0.0f, () -> SHELL, SoundEvents.FIREWORK_ROCKET_BLAST, 0.8f));

	public static final Item ROCKET_LAUNCHER = gun("rocket_launcher",
			new GunSpec(6.0f, 80, 1, 0.0, 50, 3.0f, () -> ROCKET, SoundEvents.FIREWORK_ROCKET_LAUNCH, 0.7f));

	// ---- Extra guns, tuned by damage: 10 = 5 hearts, 3 = 1.5 hearts, 20 = 10 hearts ----
	public static final Item ENERGY_CELL = register("energy_cell", Item::new, new Item.Properties());

	public static final Item CRIMSON_MAGNUM = gun("crimson_magnum",
			new GunSpec(10.0f, 70, 1, 0.0, 16, 0.0f, () -> BULLET, SoundEvents.CROSSBOW_SHOOT, 0.9f));

	public static final Item BRASS_BUZZER = gun("brass_buzzer",
			new GunSpec(3.0f, 50, 1, 0.02, 3, 0.0f, () -> BULLET, SoundEvents.CROSSBOW_SHOOT, 1.9f));

	public static final Item ARC_LANCE = gun("arc_lance",
			new GunSpec(20.0f, 120, 1, 0.0, 40, 0.0f, () -> ENERGY_CELL, SoundEvents.FIREWORK_ROCKET_BLAST, 1.8f));

	private ModItems() {
	}

	private static Item gun(String name, GunSpec spec) {
		return register(name, props -> new GunItem(props, spec), new Item.Properties().stacksTo(1));
	}

	private static <T extends Item> T register(String name, Function<Item.Properties, T> factory, Item.Properties properties) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(BlockyArms.MOD_ID, name));
		T item = factory.apply(properties.setId(key));
		return Registry.register(BuiltInRegistries.ITEM, key, item);
	}

	public static void init() {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(entries -> {
			entries.accept(PISTOL);
			entries.accept(SHOTGUN);
			entries.accept(ROCKET_LAUNCHER);
			entries.accept(CRIMSON_MAGNUM);
			entries.accept(BRASS_BUZZER);
			entries.accept(ARC_LANCE);
			entries.accept(ENERGY_CELL);
			entries.accept(BULLET);
			entries.accept(SHELL);
			entries.accept(ROCKET);
		});
	}
}
