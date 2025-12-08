<p align="center">
  <img src="docs/images/logo.png" alt="肉包 Logo" width="120" height="120">
</p>

<h1 align="center">肉包 Roubao</h1>

<p align="center">
  <strong>首款无需电脑的开源 AI 手机自动化助手</strong>
</p>

<p align="center">
  <a href="src/branch/main/README_EN.md">English</a> | 简体中文
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform">
  <img src="https://img.shields.io/badge/Min%20SDK-26-blue.svg" alt="Min SDK">
  <img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="License">
  <img src="https://img.shields.io/badge/Language-Kotlin-purple.svg" alt="Language">
</p>

---

## 项目背景

2025 年 12 月，字节跳动联合中兴发布了「豆包手机助手」，一款能够自动操作手机完成复杂任务的 AI 助手。它能帮你比价下单、批量投简历、刷视频，甚至代打游戏。

首批 3 万台工程机定价 3499 元，上线当天即告售罄，二手市场一度炒到 5000+。

**买不到？那就自己做一个。**

于是有了肉包——一个完全开源的 AI 手机自动化助手。

为什么叫「肉包」？因为作者不爱吃素。🥟

---

## 与同类项目的对比

| 特性 | 肉包 | 豆包手机 | 其他开源方案 |
|------|------|----------|--------------|
| 需要电脑 | ❌ 不需要 | ❌ 不需要 | ✅ 大多需要 |
| 需要购买硬件 | ❌ 不需要 | ✅ 需要 3499+ | ❌ 不需要 |
| 开源 | ✅ MIT | ❌ 闭源 | ✅ 开源 |
| UI 设计 | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ |
| 自定义模型 | ✅ 支持 | ❌ 仅豆包 | ✅ 部分支持 |

### 我们解决了什么问题？

**传统的手机自动化方案痛点：**

- 必须连接电脑运行 ADB 命令
- 需要部署 Python 环境和各种依赖
- 只能在电脑端操作，手机必须通过数据线连接
- 技术门槛高，普通用户难以使用

**肉包的解决方案：**

一个 App，装上就能用。无需电脑、无需数据线、无需任何技术背景。

打开 App → 配置 API Key → 说出你想做的事 → 完成。

---

## 核心特性

### 🤖 智能 AI Agent

- 基于先进的视觉语言模型（VLM），能够"看懂"屏幕内容
- 自然语言指令，说人话就能操作手机
- 智能决策，根据屏幕状态自动规划下一步操作

### 🎨 精心设计的 UI

**这可能是所有手机自动化开源项目中 UI 做得最好看的。**

- 现代化 Material 3 设计语言
- 流畅的动画效果
- 深色/浅色主题自适应
- 精心设计的首次使用引导
- 完整的中英文双语支持

### 🔒 安全保护

- 检测到支付、密码等敏感页面自动停止
- 任务执行全程可视，悬浮窗显示进度
- 随时可以手动停止任务

### 🔧 高度可定制

- 支持多种 VLM：阿里云通义千问、OpenAI GPT-4V、Claude 等
- 可配置自定义 API 端点
- 可添加自定义模型

---

## 快速开始

### 前置要求

1. **Android 8.0 (API 26)** 或更高版本
2. **Shizuku** - 用于获取系统级控制权限
3. **VLM API Key** - 需要视觉语言模型的 API 密钥

### 安装步骤

#### 1. 安装并启动 Shizuku

Shizuku 是一个开源工具，可以让普通应用获得 ADB 权限，无需 Root。

- [Google Play](https://play.google.com/store/apps/details?id=moe.shizuku.privileged.api)
- [GitHub Releases](https://github.com/RikkaApps/Shizuku/releases)

**启动方式（二选一）：**

**无线调试（推荐，需 Android 11+）**
1. 进入 `设置 > 开发者选项 > 无线调试`
2. 开启无线调试
3. 在 Shizuku App 中选择"无线调试"方式启动

**电脑 ADB**
1. 手机连接电脑，开启 USB 调试
2. 执行：`adb shell sh /storage/emulated/0/Android/data/moe.shizuku.privileged.api/start.sh`

#### 2. 安装肉包

从 [Releases](../../releases) 页面下载最新 APK 安装。

#### 3. 授权与配置

1. 打开肉包 App
2. 在 Shizuku 中授权肉包
3. **⚠️ 重要：进入设置页面，配置你的 API Key**

### 获取 API Key

**阿里云通义千问（推荐国内用户）**
1. 访问 [阿里云百炼平台](https://bailian.console.aliyun.com/)
2. 开通 DashScope 服务
3. 在 API-KEY 管理中创建密钥

**OpenAI（需要代理）**
1. 访问 [OpenAI Platform](https://platform.openai.com/)
2. 创建 API Key

---

## 使用示例

```
帮我点个附近好吃的汉堡
打开网易云音乐播放每日推荐
帮我把最后一张照片发送到微博
帮我在美团点一份猪脚饭
打开B站看热门视频
```

---

## 技术架构

```
┌─────────────────────────────────────────────────────────┐
│                      肉包 App                            │
├─────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
│  │   UI 层     │  │  Agent 层   │  │  控制层     │     │
│  │  Compose    │  │ MobileAgent │  │ Controller  │     │
│  └─────────────┘  └─────────────┘  └─────────────┘     │
│         │                │                │             │
│         └────────────────┼────────────────┘             │
│                          │                              │
│  ┌───────────────────────▼───────────────────────────┐ │
│  │                  VLM Client                        │ │
│  │          (Qwen-VL / GPT-4V / Claude)              │ │
│  └───────────────────────────────────────────────────┘ │
│                          │                              │
├──────────────────────────┼──────────────────────────────┤
│                          ▼                              │
│  ┌───────────────────────────────────────────────────┐ │
│  │                   Shizuku                          │ │
│  │           (System-level Control)                   │ │
│  └───────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────┘
```

### 工作流程

1. **截图** - 通过 Shizuku 获取当前屏幕截图
2. **分析** - 将截图和指令发送给 VLM，获取操作建议
3. **执行** - 通过 Shizuku 执行点击、滑动、输入等操作
4. **循环** - 重复上述步骤直到任务完成或达到安全限制

### 项目结构

```
app/src/main/java/com/example/autopilot/
├── agent/              # AI Agent 核心逻辑
│   └── MobileAgent.kt  # 主要的 Agent 实现
├── controller/         # 设备控制
│   ├── DeviceController.kt   # Shizuku 控制器
│   └── AppScanner.kt         # 应用扫描器
├── data/               # 数据层
│   ├── SettingsManager.kt    # 设置管理
│   └── ExecutionRepository.kt # 执行记录存储
├── ui/                 # 用户界面
│   ├── screens/        # 各个页面
│   ├── theme/          # 主题定义
│   └── OverlayService.kt     # 悬浮窗服务
├── vlm/                # VLM 客户端
│   └── VLMClient.kt    # API 调用封装
└── MainActivity.kt     # 主 Activity
```

---

## 路线图

### 近期计划

- [ ] **Skills 技能系统** - 预定义常用操作流程，提高执行效率和准确性
- [ ] **MCP (Model Context Protocol)** - 接入更多能力扩展，如日历、邮件、文件管理等
- [ ] **执行录屏** - 保存任务执行过程视频，方便回顾和调试

### 中期计划

- [ ] **无障碍模式** - 无需 Shizuku，通过 Android 无障碍服务控制，进一步降低使用门槛
- [ ] **更多设备支持** - 适配更多 Android 设备和定制系统（MIUI、ColorOS、HarmonyOS 等）
- [ ] **本地模型** - 支持在设备端运行小型 VLM，实现离线使用
- [ ] **任务模板** - 保存和分享常用任务

### 长期愿景

- [ ] **多应用协作** - 跨 App 联动完成复杂工作流
- [ ] **智能学习** - 从用户操作习惯中学习，优化执行策略
- [ ] **语音控制** - 语音唤醒和语音指令

---

## 开发

### 环境要求

- Android Studio Hedgehog 或更高版本
- JDK 17
- Android SDK 34

### 构建

```bash
# 克隆仓库
git clone https://github.com/yourusername/roubao.git
cd roubao

# 构建 Debug 版本
./gradlew assembleDebug

# 安装到设备
./gradlew installDebug
```

---

## 贡献

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 开启 Pull Request

---

## 许可证

本项目基于 MIT 许可证开源。详见 [LICENSE](LICENSE) 文件。

---

## 致谢

- [MobileAgent](https://github.com/X-PLUG/MobileAgent) - 阿里达摩院 X-PLUG 团队开源的移动端 Agent 框架，为本项目提供了重要的技术参考
- [Shizuku](https://github.com/RikkaApps/Shizuku) - 优秀的 Android 权限管理框架

---

<p align="center">
  Made with ❤️ by Roubao Team
</p>
