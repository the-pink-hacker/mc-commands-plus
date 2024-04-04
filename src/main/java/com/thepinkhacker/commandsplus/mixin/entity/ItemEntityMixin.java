package com.thepinkhacker.commandsplus.mixin.entity;

import com.thepinkhacker.commandsplus.world.GameRuleManager;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @ModifyConstant(
            method = "tick()V",
            constant = @Constant(intValue = 6_000)
    )
    private int despawnAgeTick(int age) {
        return getDespawnAge();
    }

    @ModifyConstant(
            method = "canMerge()Z",
            constant = @Constant(intValue = 6_000)
    )
    private int despawnAgeCanMerge(int age) {
        return getDespawnAge();
    }

    @ModifyConstant(
            method = "setCovetedItem()V",
            constant = @Constant(intValue = -6_000)
    )
    private int despawnAgeConvertedItem(int age) {
        return -getDespawnAge();
    }

    @ModifyConstant(
            method = "setDespawnImmediately()V",
            constant = @Constant(intValue = 5_999)
    )
    private int despawnAgeDespawnImmediately(int age) {
        return getDespawnAge() - 1;
    }

    private int getDespawnAge() {
        return ((ItemEntity)(Object)this).getWorld().getGameRules().getInt(GameRuleManager.ITEM_DESPAWN_AGE);
    }
}
