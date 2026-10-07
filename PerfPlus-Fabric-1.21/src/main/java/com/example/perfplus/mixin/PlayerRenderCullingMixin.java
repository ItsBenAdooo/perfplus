package com.example.perfplus.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerRenderCullingMixin {

    @Inject(
        method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void perfplus$cullPlayerRender(
        AbstractClientPlayerEntity player, 
        float f, 
        float g, 
        MatrixStack matrixStack, 
        VertexConsumerProvider vertexConsumerProvider, 
        int i, 
        CallbackInfo ci
    ) {
        if (player.isMainPlayer()) {
            return;
        }

        if (player.isInvisibleTo(MinecraftClient.getInstance().player)) {
            ci.cancel();
            return;
        }

        double maxPlayerRenderDist = 96.0 * 96.0;
        if (player.squaredDistanceTo(MinecraftClient.getInstance().player) > maxPlayerRenderDist) {
            ci.cancel();
        }
    }
}
