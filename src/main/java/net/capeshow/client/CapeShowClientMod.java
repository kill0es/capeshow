
/**
 * CapeShow Mod 客户端入口类。
 * 负责初始化客户端相关功能，包括：
 * - 注册网络包监听器以接收披风更新
 * - 注册资源重载监听器以加载披风纹理
 */
package net.capeshow.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class CapeShowClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(
            CapeUpdatePacket.TYPE,
            (packet, player, responseSender) -> {
                CapeTextureManager.updateCapeTexture(
                    packet.getPlayerUuid(),
                    packet.getCapeHash()
                );
            }
        );
        
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
            .registerReloadListener(new CapeTextureReloadListener());
    }
}