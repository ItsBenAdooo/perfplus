package com.example.perfplus.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderCullingMixin {

    private static final double MAX_CULL_DISTANCE_SQ = 128.0 * 128.0;

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void perfplus$cullEntities(E entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (entity == MinecraftClient.getInstance().player) {
            return;
        }

        double distanceSq = entity.squaredDistanceTo(x, y, z);
        if (distanceSq > MAX_CULL_DISTANCE_SQ) {
            cir.setReturnValue(false);
            return;
        }

        if (!frustum.isVisible(entity.getVisibilityBoundingBox())) {
            cir.setReturnValue(false);
        }
    }
}
