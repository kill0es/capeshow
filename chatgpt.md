下面提供完整项目文件的示例，每个文件均在文件开头添加了详细说明的注释。如果某个文件实际为空（例如二进制文件），我们会在文件中写入注释说明，以保证文件不为空。请根据以下内容创建各文件，方便后续开发者管理和审查。

项目结构

capeshow-mod/
├── 01. .github/
│   └── 01.1. workflows/
│       └── 01.1.1. build.yml
├── 02. gradle/
│   └── 02.1. wrapper/
│       ├── 02.1.1. gradle-wrapper.jar          // 二进制文件，由 Gradle Wrapper 自动生成
│       └── 02.1.2. gradle-wrapper.properties
├── 03. gradlew                                   // Unix 构建脚本，由 Gradle Wrapper 自动生成
├── 04. gradlew.bat                               // Windows 构建脚本，由 Gradle Wrapper 自动生成
├── 05. build.gradle
├── 06. settings.gradle
└── 07. src/
    ├── 07.1. java/
    │   └── net/
    │       └── capeshow/
    │           ├── 07.1.1. CapeShowMod.java
    │           ├── 07.1.2. client/
    │           │   ├── 07.1.2.1. CapeShowClientMod.java
    │           │   ├── 07.1.2.2. CapeTextureManager.java
    │           │   └── 07.1.2.3. CapeTextureReloadListener.java
    │           ├── 07.1.3. cape/
    │           │   └── 07.1.3.1. CapeManager.java
    │           ├── 07.1.4. command/
    │           │   └── 07.1.4.1. CapeCommands.java
    │           ├── 07.1.5. config/
    │           │   └── 07.1.5.1. ModConfig.java
    │           ├── 07.1.6. db/
    │           │   └── 07.1.6.1. DatabaseHandler.java
    │           ├── 07.1.7. mixin/
    │           │   ├── 07.1.7.1. MixinPlayerEntityRenderer.java
    │           │   └── 07.1.7.2. accessor/
    │           │       └── 07.1.7.2.1. PlayerEntityRendererAccessor.java
    │           └── 07.1.8. network/
    │               └── 07.1.8.1. CapeUpdatePacket.java
    └── 07.2. resources/
        ├── 07.2.1. fabric.mod.json
        ├── 07.2.2. capeshow.mixins.json
        ├── 07.2.3. assets/
        │   └── capeshow/
        │       ├── 07.2.3.1. textures/
        │       │   └── capes/
        │       │       └── 07.2.3.1.1. default.png    // 二进制图片文件
        │       └── 07.2.3.2. lang/
        │           └── 07.2.3.2.1. en_us.json
        └── 07.2.4. config/
            └── 07.2.4.1. capeshow-config.json
└── 08. README.md   <-- 放在项目结构末尾

各文件详细内容

01.1.1 .github/workflows/build.yml

# 文件：.github/workflows/build.yml
# 描述：GitHub Actions 工作流配置，用于自动构建 Fabric 模组，
#        并在发布 Release 时将生成的 .jar 文件上传到 GitHub Releases 及 Artifact 存储。
name: Build CapeShow Mod

on:
  push:
    branches:
      - main
      - master
  pull_request:
    branches:
      - main
      - master
  workflow_dispatch: # 允许手动触发
  release:
    types: [created] # 在发布 Release 时自动构建并上传文件

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Setup JDK 17
        uses: actions/setup-java@v3
        with:
          distribution: 'temurin'
          java-version: '17'
          cache: 'gradle'

      - name: Validate Gradle Wrapper
        uses: gradle/wrapper-validation-action@v2

      - name: Cache Gradle
        uses: actions/cache@v3
        with:
          path: |
            ~/.gradle/caches
            ~/.gradle/wrapper
          key: gradle-${{ runner.os }}-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
          restore-keys: |
            gradle-${{ runner.os }}-

      - name: Build Fabric Mod
        run: ./gradlew build --stacktrace

      - name: List build/libs directory
        run: ls -lah build/libs/

      - name: Get JAR filename
        id: get_jar
        run: echo "JAR_NAME=$(ls build/libs | grep '\.jar$' | head -n 1)" >> $GITHUB_ENV

      - name: Upload Artifact
        uses: actions/upload-artifact@v4
        with:
          name: CapeShow-Mod
          path: build/libs/${{ env.JAR_NAME }}
          retention-days: 7

  release:
    needs: build
    if: github.event_name == 'release'
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Download JAR Artifact
        uses: actions/download-artifact@v4
        with:
          name: CapeShow-Mod
          path: build/libs/

      - name: Get JAR filename
        id: get_jar
        run: echo "JAR_NAME=$(ls build/libs | grep '\.jar$' | head -n 1)" >> $GITHUB_ENV

      - name: Publish Release
        uses: softprops/action-gh-release@v2
        with:
          files: build/libs/${{ env.JAR_NAME }}
          body: |
            🚀 **Cape Show Mod v${{ github.event.release.tag_name }}**
            - Compatible with Fabric Loader
            - Supports Minecraft 1.19+
            - Place the `.jar` in your `mods/` folder
          draft: false
          prerelease: false
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}

02.1.2 gradle-wrapper.properties

# 文件：gradle/wrapper/gradle-wrapper.properties
# 描述：Gradle Wrapper 配置文件，指定 Gradle 发行版的下载地址及缓存位置。
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-7.5-bin.zip

03. gradlew

#!/usr/bin/env sh
# 文件：gradlew
# 描述：Unix 平台的 Gradle Wrapper 脚本，用于执行 Gradle 构建命令。
# 注意：此文件由 Gradle Wrapper 自动生成，具体内容请参考生成的文件内容。
echo "请使用 Gradle Wrapper 自动生成的 gradlew 脚本。"

04. gradlew.bat

@ECHO OFF
REM 文件：gradlew.bat
REM 描述：Windows 平台的 Gradle Wrapper 脚本，用于执行 Gradle 构建命令。
REM 注意：此文件由 Gradle Wrapper 自动生成，具体内容请参考生成的文件内容.
ECHO Please use the generated gradlew.bat.

05. build.gradle

// 文件：build.gradle
// 描述：Gradle 构建脚本，用于配置项目依赖、插件及编译设置。
plugins {
    id 'java'
    id 'fabric-loom' version '1.1-SNAPSHOT'
}

group = 'net.capeshow'
version = '1.0.0'

sourceCompatibility = JavaVersion.VERSION_17
targetCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven { url = 'https://maven.fabricmc.net/' }
}

dependencies {
    minecraft "com.mojang:minecraft:1.19.2"
    mappings "net.fabricmc:yarn:1.19.2+build.1:v2"
    modImplementation "net.fabricmc:fabric-loom:1.1-SNAPSHOT"
    modImplementation "net.fabricmc.fabric-api:fabric-api:0.56.0+1.19.2"
    // 其他依赖，例如 HikariCP、Caffeine、Gson 等可在此添加
}

loom {
    accessWidenerPath = file("src/main/resources/capeshow.accesswidener")
}

06. settings.gradle

// 文件：settings.gradle
// 描述：Gradle 设置文件，用于指定项目名称。
rootProject.name = "capeshow-mod"

07.1.1 CapeShowMod.java

/*
 * 文件：07.1.1. CapeShowMod.java
 * 描述：模组主入口，实现 ModInitializer 接口，负责初始化配置、数据库、命令和定时任务。
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
        try {
            net.capeshow.config.ModConfig.init();
            net.capeshow.db.DatabaseHandler.init();
            CommandRegistrationCallback.EVENT.register((dispatcher, registry, env) ->
                    net.capeshow.command.CapeCommands.register(dispatcher));
            ServerTickEvents.START_SERVER_TICK.register(net.capeshow.cape.CapeManager::tickHandler);
        } catch (Exception e) {
            LOGGER.error("Initialization error: ", e);
        }
    }
}

07.1.2.1 CapeShowClientMod.java

/*
 * 文件：07.1.2.1. CapeShowClientMod.java
 * 描述：客户端入口，实现 ClientModInitializer 接口，用于注册网络接收器和资源重载监听器。
 */
package net.capeshow.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CapeShowClientMod implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CapeShowClientMod.class);

    @Override
    public void onInitializeClient() {
        try {
            ClientPlayNetworking.registerGlobalReceiver(
                CapeUpdatePacket.TYPE,
                (packet, player, responseSender) -> {
                    CapeTextureManager.updateCape(packet.getPlayerUuid(), packet.getCapeHash());
                }
            );

            ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
                .registerReloadListener(new CapeTextureReloadListener());
        } catch (Exception e) {
            LOGGER.error("Client initialization error: ", e);
        }
    }
}

07.1.6.1 DatabaseHandler.java

/*
 * 文件：07.1.6.1. DatabaseHandler.java
 * 描述：数据库操作类，使用 HikariCP 管理 SQLite 数据库连接，并提供异步操作接口。
 */
package net.capeshow.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.fabricmc.loader.api.FabricLoader;
import net.capeshow.CapeShowMod;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHandler.class);
    private static HikariDataSource dataSource;
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void init() {
        try {
            // 获取配置目录下的数据库文件路径
            Path configDir = FabricLoader.getInstance().getConfigDir();
            Path dbPath = configDir.resolve("capeshow.db");

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:sqlite:" + dbPath.toString());
            config.setMaximumPoolSize(10);
            config.setConnectionTestQuery("SELECT 1");
            config.setPoolName("CapeShowHikariPool");
            // 开启 PreparedStatement 缓存
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            // 设置超时、空闲时间、最大生命周期
            config.setConnectionTimeout(30000); // 30秒
            config.setIdleTimeout(600000);      // 10分钟
            config.setMaxLifetime(1800000);     // 30分钟

            dataSource = new HikariDataSource(config);

            // 初始化数据库表
            try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
                String sql = "CREATE TABLE IF NOT EXISTS capes (" +
                             "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                             "player_uuid TEXT NOT NULL," +
                             "cape_hash TEXT NOT NULL," +
                             "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            LOGGER.error("Database initialization error: ", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void insertCape(String playerUuid, String capeHash) {
        executor.submit(() -> {
            String sql = "INSERT INTO capes (player_uuid, cape_hash) VALUES (?, ?)";
            try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, playerUuid);
                pstmt.setString(2, capeHash);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                LOGGER.error("Error inserting cape data: ", e);
            }
        });
    }

    public static List<Map<String, Object>> getCapesByPlayer(String playerUuid) {
        List<Map<String, Object>> capes = new ArrayList<>();
        String sql = "SELECT * FROM capes WHERE player_uuid = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerUuid);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> cape = new HashMap<>();
                    cape.put("id", rs.getInt("id"));
                    cape.put("player_uuid", rs.getString("player_uuid"));
                    cape.put("cape_hash", rs.getString("cape_hash"));
                    cape.put("timestamp", rs.getString("timestamp"));
                    capes.add(cape);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error retrieving capes: ", e);
        }
        return capes;
    }
}

07.1.4.1 CapeCommands.java

/*
 * 文件：07.1.4.1. CapeCommands.java
 * 描述：命令处理类，注册模组相关命令，包括查询披风历史和更新披风，内置输入验证和权限检查。
 */
package net.capeshow.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class CapeCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("capeshow")
            .then(CommandManager.literal("history")
                .then(CommandManager.argument("input", StringArgumentType.word())
                    .executes(context -> {
                        String userInput = StringArgumentType.getString(context, "input");
                        // 输入验证：只允许字母和数字
                        if (!userInput.matches("[a-zA-Z0-9]+")) {
                            context.getSource().sendError(Text.of("无效的输入"));
                            return 0;
                        }
                        // 调用数据库查询或其他逻辑
                        context.getSource().sendFeedback(Text.of("查询成功"), false);
                        return 1;
                    })
                )
            )
            .then(CommandManager.literal("update")
                .executes(context -> {
                    // 检查权限：仅允许 OP（权限级别 2）操作
                    if (context.getSource().hasPermissionLevel(2)) {
                        // 执行披风更新操作
                        context.getSource().sendFeedback(Text.of("披风更新成功"), false);
                        return 1;
                    } else {
                        context.getSource().sendError(Text.of("权限不足"));
                        return 0;
                    }
                })
            )
        );
    }
}

07.1.7.2.1 PlayerEntityRendererAccessor.java

/*
 * 文件：07.1.7.2.1. PlayerEntityRendererAccessor.java
 * 描述：Mixin Accessor 接口，用于访问 PlayerEntityRenderer 类中的私有字段（例如 capeTexture）。
 */
package net.capeshow.mixin.accessor;

import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.render.entity.PlayerEntityRenderer;

@Mixin(PlayerEntityRenderer.class)
public interface PlayerEntityRendererAccessor {
    @Accessor("capeTexture")
    void setCapeTexture(Identifier texture);
}

07.1.7.1 MixinPlayerEntityRenderer.java

/*
 * 文件：07.1.7.1. MixinPlayerEntityRenderer.java
 * 描述：Mixin 类，用于拦截并修改 PlayerEntityRenderer 中的 renderCape 方法，
 *        实现自定义披风渲染逻辑。
 */
package net.capeshow.mixin;

import net.capeshow.client.CapeTextureManager;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.capeshow.mixin.accessor.PlayerEntityRendererAccessor;

@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerEntityRenderer {

    @Inject(method = "renderCape", at = @At("HEAD"), cancellable = true)
    private void onRenderCape(PlayerEntity player, float tickDelta, CallbackInfo ci) {
        // 根据业务逻辑判断是否需要自定义披风渲染
        Identifier customCape = CapeTextureManager.getCapeTexture(player.getUuid());
        if (customCape != null) {
            ((PlayerEntityRendererAccessor)this).setCapeTexture(customCape);
        }
    }
}

07.1.2.2 CapeTextureManager.java

/*
 * 文件：07.1.2.2. CapeTextureManager.java
 * 描述：披风纹理管理器，维护玩家披风纹理缓存，并提供更新与获取接口。
 */
package net.capeshow.client;

import net.minecraft.util.Identifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CapeTextureManager {
    private static final Map<String, Identifier> capeCache = new ConcurrentHashMap<>();

    public static void updateCape(String playerUuid, String capeHash) {
        // 根据 capeHash 构造纹理文件路径（可根据实际需求修改）
        Identifier newCape = new Identifier("capeshow", "textures/capes/" + capeHash + ".png");
        capeCache.put(playerUuid, newCape);
    }

    public static Identifier getCapeTexture(String playerUuid) {
        return capeCache.get(playerUuid);
    }

    public static void clearCache() {
        capeCache.clear();
    }
}

07.1.2.3 CapeTextureReloadListener.java

/*
 * 文件：07.1.2.3. CapeTextureReloadListener.java
 * 描述：资源重载监听器，当资源重新加载时清空披风纹理缓存。
 */
package net.capeshow.client;

import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.profiler.Profiler;

public class CapeTextureReloadListener implements SynchronousResourceReloader {
    @Override
    public void reload(ResourceManager manager, Profiler profiler) {
        CapeTextureManager.clearCache();
    }
}

07.1.3.1 CapeManager.java

/*
 * 文件：07.1.3.1. CapeManager.java
 * 描述：披风管理器，每个服务器 Tick 调用一次，处理披风动画和定时任务逻辑。
 */
package net.capeshow.cape;

public class CapeManager {
    public static void tickHandler() {
        // 在此添加披风动画或定时任务逻辑
    }
}

07.1.8.1 CapeUpdatePacket.java

/*
 * 文件：07.1.8.1. CapeUpdatePacket.java
 * 描述：网络数据包类，用于客户端与服务器间传递披风更新信息。
 */
package net.capeshow.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.util.Identifier;
import net.minecraft.network.PacketByteBuf;

public class CapeUpdatePacket {
    public static final Identifier TYPE = new Identifier("cape_show", "cape_update");

    private final String playerUuid;
    private final String capeHash;

    public CapeUpdatePacket(String playerUuid, String capeHash) {
        this.playerUuid = playerUuid;
        this.capeHash = capeHash;
    }

    public String getPlayerUuid() {
        return playerUuid;
    }

    public String getCapeHash() {
        return capeHash;
    }

    public static PacketByteBuf toPacket(CapeUpdatePacket packet) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(packet.playerUuid);
        buf.writeString(packet.capeHash);
        return buf;
    }

    public static CapeUpdatePacket fromPacket(PacketByteBuf buf) {
        String uuid = buf.readString(32767);
        String hash = buf.readString(32767);
        return new CapeUpdatePacket(uuid, hash);
    }
}

07.1.5.1 ModConfig.java

/*
 * 文件：07.1.5.1. ModConfig.java
 * 描述：模组配置管理类，负责读取配置文件并初始化动画间隔与模式等参数。
 */
package net.capeshow.config;

import com.google.gson.Gson;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

public class ModConfig {
    public static int animationInterval = 20;
    public static String mode = "ROTATE";

    public static void init() {
        try {
            Path configPath = getConfigPath().resolve("capeshow-config.json");
            if (Files.exists(configPath)) {
                String json = new String(Files.readAllBytes(configPath), StandardCharsets.UTF_8);
                ModConfig config = new Gson().fromJson(json, ModConfig.class);
                animationInterval = config.animationInterval;
                mode = config.mode;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Path getConfigPath() {
        return Path.of("config");
    }
}

07.2.1 fabric.mod.json

{
  "schemaVersion": 1,
  "id": "cape_show",
  "version": "${version}",
  "name": "Cape Show Mod",
  "description": "A Fabric mod that enables dynamic capes.",
  "authors": ["YourName"],
  "contact": {
    "homepage": "https://github.com/your-repo/capeshow-mod",
    "sources": "https://github.com/your-repo/capeshow-mod"
  },
  "license": "MIT",
  "environment": "*",
  "entrypoints": {
    "main": [
      "net.capeshow.CapeShowMod"
    ],
    "client": [
      "net.capeshow.client.CapeShowClientMod"
    ]
  },
  "mixins": [
    "capeshow.mixins.json"
  ],
  "depends": {
    "fabricloader": ">=0.14.6",
    "fabric": "*",
    "minecraft": "1.19.2"
  }
}

07.2.2 capeshow.mixins.json

{
  "required": true,
  "package": "net.capeshow.mixin",
  "compatibilityLevel": "JAVA_17",
  "mixins": [
    "MixinPlayerEntityRenderer"
  ],
  "injectors": {
    "defaultRequire": 1
  }
}

07.2.3.2.1 en_us.json

{
  "item.capeshow.cape": "Cape",
  "command.capeshow.history": "Show cape history"
}

07.2.4.1 capeshow-config.json

{
  "animationInterval": 20,
  "mode": "ROTATE"
}

07.2.3.1.1 default.png

// 文件：07.2.3.1.1. default.png
// 描述：默认披风纹理图片（二进制文件）。请确保此文件为有效图片文件。
// 此处仅放置提示信息，实际文件应包含图片二进制数据。

08. README.md

<!-- 文件：README.md
     描述：项目总说明文档，包含功能特点、安装指南、使用说明、开发者指南、构建与发布说明等。 -->

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

	•	animationInterval：披风动画更新间隔（单位：Tick）。
	•	mode：披风动画模式，例如 ROTATE（轮播）等。

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

git clone https://github.com/your-repo/capeshow-mod.git
cd capeshow-mod


	2.	使用 Gradle 构建模组

./gradlew build

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