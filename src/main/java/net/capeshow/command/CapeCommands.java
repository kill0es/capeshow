/**
 * 命令注册类。
 * 负责注册和管理模组的命令，包括：
 * - 查看披风历史
 * - 设置动画切换间隔
 * - 手动更新披风
 */
package net.capeshow.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import static net.minecraft.server.command.CommandManager.*;

public class CapeCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("capeshow")
            .then(literal("history")
                .executes(ctx -> showHistory(ctx.getSource())))
            .then(literal("set-animation")
                .then(argument("interval", IntegerArgumentType.integer(1))
                    .executes(ctx -> setAnimation(ctx.getSource(), 
                        IntegerArgumentType.getInteger(ctx, "interval")))))
            .then(literal("update")
                .then(argument("hash", StringArgumentType.string())
                    .executes(ctx -> updateCape(ctx.getSource(), 
                        StringArgumentType.getString(ctx, "hash")))))
        );
    }
    
    private static int showHistory(ServerCommandSource source) {
        UUID uuid = source.getPlayer().getUuid();
        List<String> history = DatabaseHandler.getHistory(uuid);
        source.sendMessage(Text.literal("Cape History: " + String.join(" → ", history)));
        return 1;
    }
    
    // 其他命令方法实现...
}