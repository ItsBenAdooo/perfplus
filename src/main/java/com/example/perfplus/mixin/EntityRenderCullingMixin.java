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

        // Kendi karakterimizi asla gizleme (1. veya 3. şahıs kamerada görünmeli)
        if (client.player != null && entity == client.player) {
            return;
        }

        // Görüş alanı (Frustum) dışındaki tüm entity ve oyuncuların çizimini iptal et
        if (frustum != null && entity != null && entity.getVisibilityBoundingBox() != null) {
            if (!frustum.isVisible(entity.getVisibilityBoundingBox())) {
                cir.setReturnValue(false);
            }
        }
    }
}
