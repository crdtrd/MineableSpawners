package com.drtdrc.mineablespawners;

import net.fabricmc.api.ModInitializer;

public class MineableSpawners implements ModInitializer {

    @Override
    public void onInitialize() {
        com.drtdrc.mineablespawners.loot.SpawnEggDrops.register();
    }
}
