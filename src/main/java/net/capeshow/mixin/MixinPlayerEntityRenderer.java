/**
 * 玩家渲染器Mixin。
 * 用于替换玩家披风纹理。
 * 在渲染玩家时检查并应用自定义披风纹理。
 */
package net.capeshow.mixin;

import net.capeshow.client.CapeTextureManager;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class MixinPlayerEntityRenderer {
    @Inject(
        method = "render",
        at = @At("HEAD")
    )
    private void onRender(PlayerEntity player, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        Identifier customCape = CapeTextureManager.getCapeTexture(player.getUuid());
        if (customCape != null) {
            ((PlayerEntityRendererAccessor) this).setCapeTexture(customCape);
        }
    }
}