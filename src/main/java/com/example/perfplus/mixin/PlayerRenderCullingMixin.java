package com.example.perfplus.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class PlayerRenderCullingMixin {

    @Inject(
        method = "shouldRender(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/Frustum;DDD)Z", 
        at = @At("HEAD"), 
        cancellable = true
    )
    private void perfplus$cullPlayerRender(
        AbstractClientPlayerEntity player, 
        Frustum frustum, 
        double x, 
        double y, 
        double z, 
        CallbackInfoReturnable<Boolean> cir
    ) {
        MinecraftClient client = MinecraftClient.getInstance();

        // Kendi karakterimizi asla gizleme
        if (player == client.player) {
            return;
        }

        // Görüş alanı dışındaki diğer oyuncuları çizmeyi iptal et
        if (!frustum.isVisible(player.getVisibilityBoundingBox())) {
            cir.setReturnValue(false);
        }
    }
}
