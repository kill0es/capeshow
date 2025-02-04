# CapeShow Mod

![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/yourname/capeshow-mod/build.yml)
![Mod Version](https://img.shields.io/badge/version-1.1.0-blue)

一个高级披风管理模组，支持动态切换动画、历史记录追踪和跨客户端同步。

## 功能特性

- 🧥 **披风历史记录**：自动记录玩家使用过的所有披风
- 🎞️ **动画系统**：可配置轮播/随机切换模式（默认1秒间隔）
- 🔒 **数据安全**：使用SQLite数据库存储，通过预处理语句防止注入
- 🌐 **网络同步**：实时同步披风状态至所有客户端
- ⚙️ **热重载配置**：修改 `capeshow-config.json` 后无需重启
- 🛠️ **开发者友好**：提供清晰的API和Mixin扩展点

## 安装

1. 下载最新版本 [capeshow-1.1.0.jar](https://github.com/yourname/capeshow-mod/releases)
2. 将文件放入 Minecraft 客户端的 `mods` 文件夹
3. 确保已安装 [Fabric Loader](https://fabricmc.net/use/)

## 命令用法

| 命令                          | 权限等级 | 描述                  |
|------------------------------|----------|----------------------|
| `/capeshow history`          | 2 (OP)   | 查看披风使用历史        |
| `/capeshow set-animation <间隔>` | 2 (OP) | 设置切换间隔（单位：tick）|
| `/capeshow update <哈希>`     | 2 (OP)   | 手动更新当前披风        |

## 配置

编辑 `config/capeshow-config.json`:
```json
{
  "animationInterval": 20,  // 切换间隔（20 ticks = 1秒）
  "mode": "ROTATE"          // 模式：ROTATE（轮播）或 RANDOM（随机）
}