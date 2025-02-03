/**
 * 披风更新网络包。
 * 用于在客户端和服务端之间同步披风数据。
 * 包含玩家UUID和披风哈希值。
 */
package net.capeshow.network;

import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import java.util.UUID;

public record CapeUpdatePacket(UUID playerUuid, String capeHash) implements FabricPacket {
    public static final PacketType<CapeUpdatePacket> TYPE = PacketType.create(
        new Identifier("cape_show", "cape_update"), 
        CapeUpdatePacket::read
    );

    public static CapeUpdatePacket read(PacketByteBuf buf) {
        return new CapeUpdatePacket(
            buf.readUuid(),
            buf.readString()
        );
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeUuid(playerUuid);
        buf.writeString(capeHash);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}