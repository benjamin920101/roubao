<p align="center">
  <img src="docs/images/logo.png" alt="Roubao Logo" width="120" height="120">
</p>

<h1 align="center">Roubao (肉包)</h1>

<p align="center">
  <strong>The First Open-Source AI Phone Automation Assistant That Doesn't Need a Computer</strong>
</p>

<p align="center">
  English | <a href="src/branch/main/README.md">简体中文</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform">
  <img src="https://img.shields.io/badge/Min%20SDK-26-blue.svg" alt="Min SDK">
  <img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="License">
  <img src="https://img.shields.io/badge/Language-Kotlin-purple.svg" alt="Language">
</p>

---

## Background

In December 2025, ByteDance partnered with ZTE to release "Doubao Phone Assistant" - an AI assistant that can automatically operate your phone to complete complex tasks. It can compare prices and place orders, batch submit job applications, scroll through videos, and even play games for you.

The first batch of 30,000 engineering units priced at 3,499 CNY (~$480) sold out on launch day, with resale prices reaching 5,000+ CNY.

**Can't buy one? Let's build our own.**

And so Roubao was born - a fully open-source AI phone automation assistant.

Why "Roubao" (肉包, meaning "meat bun")? Because the author doesn't like vegetables. 🥟

---

## Comparison

| Feature | Roubao | Doubao Phone | Other Open Source |
|---------|--------|--------------|-------------------|
| Requires PC | ❌ No | ❌ No | ✅ Most do |
| Requires Hardware | ❌ No | ✅ $480+ | ❌ No |
| Open Source | ✅ MIT | ❌ Closed | ✅ Yes |
| UI Design | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ |
| Custom Models | ✅ Yes | ❌ Doubao only | ✅ Partial |

### What Problem Do We Solve?

**Pain points of traditional phone automation:**

- Must connect to a computer to run ADB commands
- Need to set up Python environment and various dependencies
- Can only operate from computer, phone must be connected via USB
- High technical barrier, difficult for regular users

**Roubao's Solution:**

One app, install and use. No computer, no cables, no technical background required.

Open App → Configure API Key → Tell it what you want → Done.

---

## Key Features

### 🤖 Intelligent AI Agent

- Based on advanced Vision Language Models (VLM), can "see" and understand screen content
- Natural language commands - just speak normally
- Smart decision making, automatically plans next steps based on screen state

### 🎨 Beautifully Designed UI

**This is probably the best-looking UI among all open-source phone automation projects.**

- Modern Material 3 design language
- Smooth animations
- Dark/Light theme auto-adaptation
- Carefully designed onboarding experience
- Full English and Chinese language support

### 🔒 Safety Protection

- Automatically stops when detecting payment or password pages
- Full visibility during task execution with overlay progress display
- Can manually stop tasks anytime

### 🔧 Highly Customizable

- Supports multiple VLMs: Alibaba Qwen-VL, OpenAI GPT-4V, Claude, etc.
- Configurable custom API endpoints
- Can add custom models

---

## Quick Start

### Prerequisites

1. **Android 8.0 (API 26)** or higher
2. **Shizuku** - For system-level control permissions
3. **VLM API Key** - Requires a Vision Language Model API key

### Installation Steps

#### 1. Install and Start Shizuku

Shizuku is an open-source tool that allows regular apps to gain ADB-level permissions without Root.

- [Google Play](https://play.google.com/store/apps/details?id=moe.shizuku.privileged.api)
- [GitHub Releases](https://github.com/RikkaApps/Shizuku/releases)

**Startup Methods (choose one):**

**Wireless Debugging (Recommended, requires Android 11+)**
1. Go to `Settings > Developer Options > Wireless Debugging`
2. Enable Wireless Debugging
3. In Shizuku app, select "Wireless Debugging" to start

**Computer ADB**
1. Connect phone to computer, enable USB Debugging
2. Run: `adb shell sh /storage/emulated/0/Android/data/moe.shizuku.privileged.api/start.sh`

#### 2. Install Roubao

Download the latest APK from [Releases](../../releases) page.

#### 3. Authorization & Configuration

1. Open Roubao app
2. Authorize Roubao in Shizuku
3. **⚠️ Important: Go to Settings and configure your API Key**

### Getting an API Key

**Alibaba Qwen-VL (Recommended for China users)**
1. Visit [Alibaba Cloud Bailian Platform](https://bailian.console.aliyun.com/)
2. Enable DashScope service
3. Create API key in API-KEY management

**OpenAI (Requires proxy in some regions)**
1. Visit [OpenAI Platform](https://platform.openai.com/)
2. Create an API Key

---

## Usage Examples

```
Order a tasty burger nearby
Open NetEase Music and play daily recommendations
Post my latest photo to Weibo
Order pork trotter rice on Meituan
Watch trending videos on Bilibili
```

---

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                     Roubao App                          │
├─────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
│  │   UI Layer  │  │ Agent Layer │  │Control Layer│     │
│  │   Compose   │  │ MobileAgent │  │ Controller  │     │
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

### Workflow

1. **Screenshot** - Capture current screen via Shizuku
2. **Analyze** - Send screenshot and instructions to VLM for operation suggestions
3. **Execute** - Perform taps, swipes, text input via Shizuku
4. **Loop** - Repeat until task completion or safety limits reached

### Project Structure

```
app/src/main/java/com/example/autopilot/
├── agent/              # AI Agent core logic
│   └── MobileAgent.kt  # Main Agent implementation
├── controller/         # Device control
│   ├── DeviceController.kt   # Shizuku controller
│   └── AppScanner.kt         # App scanner
├── data/               # Data layer
│   ├── SettingsManager.kt    # Settings management
│   └── ExecutionRepository.kt # Execution history storage
├── ui/                 # User interface
│   ├── screens/        # Screen composables
│   ├── theme/          # Theme definitions
│   └── OverlayService.kt     # Overlay service
├── vlm/                # VLM client
│   └── VLMClient.kt    # API wrapper
└── MainActivity.kt     # Main Activity
```

---

## Roadmap

### Near-term

- [ ] **Skills System** - Predefined operation flows for improved efficiency and accuracy
- [ ] **MCP (Model Context Protocol)** - Extended capabilities like calendar, email, file management
- [ ] **Execution Recording** - Save task execution videos for review and debugging

### Mid-term

- [ ] **Accessibility Mode** - No Shizuku required, control via Android Accessibility Service, lowering the barrier to entry
- [ ] **More Device Support** - Support more Android devices and custom systems (MIUI, ColorOS, HarmonyOS, etc.)
- [ ] **Local Models** - Support running small VLMs on-device for offline use
- [ ] **Task Templates** - Save and share common tasks

### Long-term Vision

- [ ] **Multi-app Collaboration** - Cross-app workflows for complex tasks
- [ ] **Smart Learning** - Learn from user habits to optimize execution strategies
- [ ] **Voice Control** - Voice activation and commands

---

## Development

### Requirements

- Android Studio Hedgehog or later
- JDK 17
- Android SDK 34

### Building

```bash
# Clone repository
git clone https://github.com/yourusername/roubao.git
cd roubao

# Build Debug version
./gradlew assembleDebug

# Install to device
./gradlew installDebug
```

---

## Contributing

Issues and Pull Requests are welcome!

1. Fork this repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## License

This project is open-sourced under the MIT License. See [LICENSE](LICENSE) file for details.

---

## Acknowledgments

- [MobileAgent](https://github.com/X-PLUG/MobileAgent) - Mobile Agent framework open-sourced by Alibaba DAMO Academy X-PLUG team, provided important technical reference for this project
- [Shizuku](https://github.com/RikkaApps/Shizuku) - Excellent Android permission management framework

---

<p align="center">
  Made with ❤️ by Roubao Team
</p>
