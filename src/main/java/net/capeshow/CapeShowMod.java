/**
 * CapeShow Mod 主入口类。
 * 负责初始化模组、注册命令和事件监听器。
 * 主要功能包括：
 * - 初始化配置系统和数据库
 * - 注册命令（如查看披风历史、设置动画）
 * - 注册服务器Tick事件以处理披风动画
 */
package net.capeshow;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CapeShowMod implements ModInitializer {
    public static final String MOD_ID = "cape_show";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        net.capeshow.config.ModConfig.init();
        net.capeshow.db.DatabaseHandler.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, env) -> 
            net.capeshow.command.CapeCommands.register(dispatcher));
        ServerTickEvents.START_SERVER_TICK.register(net.capeshow.cape.CapeManager::tickHandler);
    }
}