# Blocky Arms (Fabric, Minecraft Java 26.3)

Adds three guns with craftable ammo. All designs and textures are original.

| Gun | Ammo | What it does |
|---|---|---|
| Blocky Pistol | Bullet | Fast, accurate, 2.5 hearts per shot |
| Blocky Shotgun | Shotgun Shell | 8 pellets with spread, slow reload |
| Blocky Rocket Launcher | Rocket | Explodes where it lands (the blast can hurt you too) |

Right-click to fire. Creative mode needs no ammo. Everything is in the Combat creative tab.

## Crafting
- Bullet x8: iron nugget + gunpowder
- Shotgun Shell x4: iron nugget + gunpowder + paper
- Rocket x2: firework rocket + gunpowder + iron ingot
- Pistol: `II_ / IS_` (I = iron ingot, S = stick)
- Shotgun: `III / WI_` (W = any planks)
- Rocket Launcher: `III / GFI` (G = gunpowder, F = firework rocket)

## Getting the .jar (no coding tools needed)
1. Create a free GitHub account and a new empty repository.
2. Upload every file in this folder (drag and drop works, keep the folder structure).
3. Open the **Actions** tab, wait for the "build" run to finish (about 3-5 minutes).
4. Open the finished run and download the **blockyarms-jar** artifact. Unzip it.

## Building on your own computer
1. Install Java 25 (Temurin) and Gradle 9.6.
2. In this folder run: `gradle build`
3. The mod is at `build/libs/blockyarms-1.0.0.jar` (not the `-sources` one).

## Installing
1. Install Java 25, then the Fabric Loader 0.19.5 or newer for Minecraft 26.3 from fabricmc.net.
2. Download Fabric API 0.161.0+26.3 (Modrinth or CurseForge).
3. Put `blockyarms-1.0.0.jar` and the Fabric API jar into your `mods` folder.
4. Launch the Fabric 26.3 profile.

## Tweaking
All gun stats (damage, range, pellets, cooldown, explosion size) are the numbers in
`src/main/java/dev/blockyarms/ModItems.java`.
