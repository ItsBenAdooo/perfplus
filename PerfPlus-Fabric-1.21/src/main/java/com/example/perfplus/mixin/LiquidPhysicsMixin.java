package com.example.perfplus.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({WaterFluid.class, LavaFluid.class})
public class LiquidPhysicsMixin {

    @Inject(method = "flow", at = @At("HEAD"), cancellable = true)
    private void perfplus$optimizeFluidFlow(
        World world, 
        BlockPos pos, 
        BlockState state, 
        Direction direction, 
        FluidState fluidState, 
        CallbackInfo ci
    ) {
        if (!world.isChunkLoaded(pos.offset(direction))) {
            ci.cancel();
            return;
        }

        BlockState targetState = world.getBlockState(pos.offset(direction));
        if (targetState.isOpaqueFullCube(world, pos.offset(direction))) {
            ci.cancel();
        }
    }
}
