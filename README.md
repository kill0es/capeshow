# Cape Show Mod

**Cape Show Mod** 是一款基于 Fabric 开发的 Minecraft 模组，允许玩家在游戏中自定义披风，并支持动态更换、服务器端同步以及缓存管理。该模组采用 SQLite 数据库存储披风数据，利用 HikariCP 优化数据库连接，并通过 GitHub Actions 实现自动构建和发布，生成可下载的 `.jar` 文件。

---

## 功能特点

- **自定义披风**：玩家可上传、更新自定义披风，并在游戏中实时生效。
- **服务器同步**：披风数据存储在服务器端，玩家登录后自动更新披风显示。
- **数据库存储**：使用 SQLite 持久化存储披风数据，结合 HikariCP 提升数据库连接性能。
- **动态动画**：支持定时披风动画（动画间隔和模式可通过配置文件调整）。
- **权限管理**：仅具备 OP 权限（权限级别 2）的用户可执行披风更新命令。
- **安全增强**：
  - 输入验证（如命令参数仅允许字母和数字）防止 SQL 注入。
  - 异常处理和日志记录确保在错误发生时不会暴露敏感信息。
- **GitHub Actions 自动构建 & 发布**：
  - 每次代码推送、 Pull Request 或发布 GitHub Release 时自动构建模组。
  - 构建生成的 `.jar` 文件会作为 Artifact 上传，并在发布 Release 时上传至 GitHub Releases 供下载。

---

## 安装指南

### 环境要求

- **Minecraft 版本**：1.19.2（可适配其他版本，请参考 [fabric.mod.json](07.2.1.fabric.mod.json) 中的配置）
- **Fabric Loader**：请从 [Fabric 官网](https://fabricmc.net/use/) 下载并安装。
- **Java 版本**：Java 17（推荐）

### 安装步骤

1. **下载模组文件**  
   - 通过 GitHub Releases 页面下载最新版本的 `.jar` 文件，或在 GitHub Actions 构建完成后，从生成的 Artifact 中获取。
   - 将下载的 `capeshow-<version>.jar` 文件复制到 Minecraft 客户端的 `mods/` 文件夹中。

2. **启动游戏**  
   - 启动 Minecraft，确保 Fabric Loader 和 Fabric API 均已安装，并且模组已成功加载。

---

## 使用说明

### 命令指南

- **/capeshow history `<玩家名>`**  
  查询指定玩家的披风历史记录。  
  *输入限制：命令参数仅允许字母和数字。*

- **/capeshow update**  
  更新当前披风，仅限 OP（权限级别 2）的用户使用。  
  *执行成功后，会通知玩家披风已更新。*

### 配置文件

- 配置文件存放在 `config/capeshow-config.json`。  
  示例配置内容：
  ```json
  {
    "animationInterval": 20,
    "mode": "ROTATE"
  }
  ```
   animationInterval：披风动画更新间隔（单位：Tick）。
   mode：披风动画模式，例如 ROTATE（轮播）等。

开发者指南

项目结构

capeshow-mod/
├── .github/
│   └── workflows/
│       └── build.yml         // GitHub Actions 构建配置文件
├── gradle/wrapper/           // Gradle Wrapper 相关文件
├── gradlew                   // Unix 构建脚本
├── gradlew.bat               // Windows 构建脚本
├── build.gradle              // Gradle 构建脚本
├── settings.gradle           // Gradle 配置
├── README.md                 // 本文件
├── src/main/java/net/capeshow
│   ├── CapeShowMod.java      // 模组主入口
│   ├── client/              // 客户端相关代码（如披风纹理管理、重载监听器、客户端入口）
│   ├── cape/                // 披风逻辑处理
│   ├── command/             // 命令处理
│   ├── config/              // 模组配置管理
│   ├── db/                  // 数据库操作
│   ├── mixin/               // Mixin 代码及接口（修改原版披风渲染逻辑）
│   └── network/             // 网络数据包通信
└── src/main/resources/
    ├── fabric.mod.json       // 模组描述文件
    ├── capeshow.mixins.json  // Mixin 配置文件
    ├── assets/              // 资源文件（纹理、语言等）
    └── config/              // 默认配置文件

构建 & 编译
	1.	克隆仓库

  ```json

  git clone https://github.com/your-repo/capeshow-mod.git
  cd capeshow-mod
  
  ```

  2.	使用 Gradle 构建模组

  ```json

  ./gradlew build
  
  ```

构建成功后，生成的 .jar 文件将位于 build/libs/ 目录下。

GitHub Actions 自动构建 & 发布

工作流程概述
	•	自动构建
当代码推送到 main 或 master 分支、创建 Pull Request，或手动触发时，GitHub Actions 将自动执行构建流程。
	•	Artifact 上传
构建生成的 .jar 文件会作为 Artifact 上传，并可供下载。
	•	Release 发布
当创建 GitHub Release 时，工作流程会自动下载构建生成的 .jar 文件，并上传到 Release 页面，方便用户下载。

详细的 GitHub Actions 配置请参阅项目中的 .github/workflows/build.yml 文件。

许可证

Cape Show Mod 遵循 MIT 许可证。你可以自由使用、修改和分发该项目代码。

贡献

如果你希望为此项目贡献代码，请遵循以下流程：
	1.	Fork 本仓库。
	2.	创建新分支进行功能开发或 bug 修复。
	3.	提交 Pull Request。

如有任何问题或建议，请在 GitHub Issues 中反馈.

---

## 总结

以上即为完整项目文件及代码，每个文件均包含头部注释以说明文件的详细信息，且对于空（或二进制）文件均添加了注释说明。请将各文件保存到相应路径后进行整体审查，如有任何问题或需要进一步修改，请随时告知。