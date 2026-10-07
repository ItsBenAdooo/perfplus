package com.example.perfplus.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityRenderCullingMixin {

    @Inject(
        method = "shouldRender(DDD)Z", 
        at = @At("HEAD"), 
        cancellable = true
    )
    private void perfplus$cullEntityRender(double cameraX, double cameraY, double cameraZ, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        MinecraftClient client = MinecraftClient.getInstance();

        // Kendi karakterimizi asla culling'e sokma
        if (client.player != null && entity == client.player) {
            return;
        }

        // WorldRenderer'ın mevcut Frustum (Kamera Açı) nesnesine erişim
        if (client.worldRenderer != null) {
            Frustum frustum = client.worldRenderer.getFrustum();
            if (frustum != null && entity.getBoundingBox() != null) {
                // Eğer entity'nin kutusu görüş alanımızın dışındaysa çizimi tamamen engelle
                if (!frustum.isVisible(entity.getBoundingBox())) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
