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

    @Inject(
        method = "shouldRender", 
        at = @At("HEAD"), 
        cancellable = true
    )
    private <E extends Entity> void perfplus$cullRender(
        E entity, 
        Frustum frustum, 
        double x, 
        double y, 
        double z, 
        CallbackInfoReturnable<Boolean> cir
    ) {
        MinecraftClient client = MinecraftClient.getInstance();

        // Kendi karakterimizi asla gizleme
        if (client.player != null && entity == client.player) {
            return;
        }

        // 1.21.1 için doğru bounding box kontrolü
        if (frustum != null && entity != null && entity.getBoundingBox() != null) {
            if (!frustum.isVisible(entity.getBoundingBox())) {
                cir.setReturnValue(false);
            }
        }
    }
}
