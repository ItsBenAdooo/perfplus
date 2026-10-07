package com.example.perfplus.mixin;

import com.example.perfplus.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
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
        if (!ModConfig.enabled) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || entity == client.player) {
            return;
        }

        // 1. Mesafe Kontrolü
        if (client.player.squaredDistanceTo(entity) > (ModConfig.maxRenderDistance * ModConfig.maxRenderDistance)) {
            cir.setReturnValue(false);
            return;
        }

        // 2. Frustum (Görüş Açısı) Kontrolü
        if (frustum != null && entity.getBoundingBox() != null) {
            if (!frustum.isVisible(entity.getBoundingBox())) {
                cir.setReturnValue(false);
                return;
            }
        }

        // 3. Duvar Arkası (Occlusion / Görüş Hattı) Kontrolü
        if (ModConfig.checkWallOcclusion && client.world != null) {
            Vec3d cameraPos = client.gameRenderer.getCamera().getPos();
            Vec3d entityPos = entity.getEyePos();

            HitResult hitResult = client.world.raycast(new RaycastContext(
                cameraPos,
                entityPos,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                client.player
            ));

            // Oyuncu ile Entity arasında saydam olmayan bir blok varsa çizimi engelle
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                cir.setReturnValue(false);
            }
        }
    }
}
