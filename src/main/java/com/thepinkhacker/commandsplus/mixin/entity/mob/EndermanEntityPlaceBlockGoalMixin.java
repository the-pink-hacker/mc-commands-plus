package com.thepinkhacker.commandsplus.mixin.entity.mob;

import com.thepinkhacker.commandsplus.world.GameRuleManager;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.PlaceBlockGoal.class)
public class EndermanEntityPlaceBlockGoalMixin {
        @Shadow
        @Final
        private EndermanEntity enderman;

    @Inject(
            method = "canStart()Z",
            at = @At("RETURN"),
            cancellable = true
    )
    private void commandsplus_gamerule_check(CallbackInfoReturnable<Boolean> cir) {
        if (this.enderman.getWorld() instanceof ServerWorld world) {
            if (!world.getGameRules().getBoolean(GameRuleManager.DO_ENDERMAN_PLACE)) {
                cir.setReturnValue(false);
            }
        }
    }
}
