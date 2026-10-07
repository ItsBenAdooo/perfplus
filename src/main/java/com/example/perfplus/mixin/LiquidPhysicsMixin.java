package com.example.perfplus.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({WaterFluid.class, LavaFluid.class})
public class LiquidPhysicsMixin {

    @Inject(
        method = "flow", 
        at = @At("HEAD"), 
        cancellable = true
    )
    private void perfplus$optimizeFluidFlow(
        WorldAccess world, 
        BlockPos pos, 
        BlockState state, 
        Direction direction, 
        FluidState fluidState, 
        CallbackInfo ci
    ) {
        // Hedef blok konumu
        BlockPos targetPos = pos.offset(direction);

        // Çevredeki blok tam dolu / opak bir bloksa gereksiz sıvı hesaplamasını atla
        BlockState targetState = world.getBlockState(targetPos);
        if (targetState.isOpaque()) {
            ci.cancel();
        }
    }
}
