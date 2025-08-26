package com.drtdrc.mineablespawners.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(SpawnerBlock.class)
public class SpawnerBlockMixin {

    @ModifyVariable(method = "onStacksDropped",
            at = @At("HEAD"),
            index = 5,
            argsOnly = true)
    boolean checkTool(boolean dropExperience, BlockState state, ServerWorld world, BlockPos pos, ItemStack tool) {

        ItemEnchantmentsComponent comp = tool.get(DataComponentTypes.ENCHANTMENTS);
        return comp != null
                && comp.getEnchantments().stream()
                .anyMatch(entry -> entry.matchesKey(Enchantments.SILK_TOUCH) && comp.getLevel(entry) > 0);
    }

}
