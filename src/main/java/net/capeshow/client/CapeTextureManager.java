
/**
 * 披风纹理管理器。
 * 负责管理玩家披风纹理的加载和缓存。
 * 主要功能包括：
 * - 将披风哈希值映射到纹理标识符
 * - 缓存玩家当前的披风纹理
 * - 支持纹理重载
 */
package net.capeshow.client;

import net.minecraft.util.Identifier;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CapeTextureManager {
    private static final Map<UUID, Identifier> PLAYER_CAPES = new HashMap<>();
    private static final Map<String, Identifier> HASH_MAP = new HashMap<>();

    public static void updateCapeTexture(UUID uuid, String hash) {
        if (!HASH_MAP.containsKey(hash)) {
            Identifier texture = new Identifier("capeshow", "capes/" + hash);
            HASH_MAP.put(hash, texture);
        }
        PLAYER_CAPES.put(uuid, HASH_MAP.get(hash));
    }

    public static Identifier getCapeTexture(UUID uuid) {
        return PLAYER_CAPES.getOrDefault(uuid, null);
    }
    
    public static void reloadTextures() {
        HASH_MAP.clear();
    }
}