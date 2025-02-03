/**
 * 模组使用说明文件。
 * 包含安装、命令、配置和编译说明。
 */
# CapeShow Mod

## 功能
- 记录玩家披风使用历史
- 动态披风切换动画
- 自定义动画配置
- 数据库优化存储

## 安装
1. 将编译后的jar文件放入`mods`文件夹
2. 确保已安装Fabric Loader

## 命令
- `/capeshow history` 查看历史
- `/capeshow set-animation <间隔ticks>` 设置动画
- `/capeshow update <hash>` 更新披风

## 配置
编辑 `config/capeshow-config.json`:
```json
{
  "animationInterval": 20,
  "mode": "ROTATE"
}