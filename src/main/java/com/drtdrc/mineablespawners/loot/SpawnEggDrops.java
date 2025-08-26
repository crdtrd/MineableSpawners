package com.drtdrc.mineablespawners.loot;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.condition.RandomChanceWithEnchantedBonusLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;

import java.util.HashMap;
import java.util.Map;

public final class SpawnEggDrops {
    // tune as you like
    private static final float BASE_CHANCE       = 0.005f;  // 0.5%
    private static final float LOOTING_PER_LEVEL = 0.005f; // +0.5% per Looting level

    // entity loot table key -> its spawn egg item
    private static final Map<RegistryKey<LootTable>, Item> EGG_BY_KEY = new HashMap<>();

    public static void register() {
        // Precompute which entity loot table to attach an egg to
        Registries.ENTITY_TYPE.forEach(type -> {
            SpawnEggItem egg = SpawnEggItem.forEntity(type);
            if (egg == null) return; // no egg for this entity

            // 1.21.8 Yarn: EntityType#getLootTableKey()
            // this also requries the entity have a loot table
            RegistryKey<LootTable> lootKey = type.getLootTableKey().isPresent() ? type.getLootTableKey().get() : null;
            EGG_BY_KEY.put(lootKey, egg);
        });

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            Item egg = EGG_BY_KEY.get(key);
            if (egg == null) return; // not an entity with a spawn egg

            LootPool.Builder pool = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .conditionally(KilledByPlayerLootCondition.builder())
                    // requires RegistryWrapper.WrapperLookup first on 1.21.8
                    .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(
                            registries, BASE_CHANCE, LOOTING_PER_LEVEL
                    ))
                    .with(ItemEntry.builder(egg));

            tableBuilder.pool(pool);
        });
    }

    private SpawnEggDrops() {}
}
