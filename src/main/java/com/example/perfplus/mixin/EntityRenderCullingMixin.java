package com.example.perfplus.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderCullingMixin {

    // 64 Blok mesafe sınırı (64 * 64 = 4096)
    private static final double MAX_CULL_DISTANCE_SQ = 64.0 * 64.0;

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void perfplus$advancedEntityCulling(
        E entity, 
        Frustum frustum, 
        double x, 
        double y, 
        double z, 
        CallbackInfoReturnable<Boolean> cir
    ) {
        MinecraftClient client = MinecraftClient.getInstance();

        // Oyuncunun kendisini render culling dışı bırak
        if (entity == client.player) {
            return;
        }

        // 1. Görüş Alanı (Frustum) Kontrolü: Kamera açısında değilse çizmeyi iptal et
        if (!frustum.isVisible(entity.getVisibilityBoundingBox())) {
            cir.setReturnValue(false);
            return;
        }

        // 2. Mesafe Kontrolü: 64 bloktan uzak Mob ve Armor Stand'leri çizme
        if (entity instanceof MobEntity || entity instanceof ArmorStandEntity) {
            double distanceSq = entity.squaredDistanceTo(x, y, z);
            if (distanceSq > MAX_CULL_DISTANCE_SQ) {
                cir.setReturnValue(false);
            }
        }
    }
}
