# Roubao (Baozi) - Claude Code Context Document

This document provides context for future Claude Code sessions working on this Android automation app.

## Project Overview

**Roubao** (formerly "Baozi") is an Android phone automation app that uses Vision Language Models (VLMs) to understand screenshots and execute UI automation tasks. It uses a multi-agent architecture where specialized agents collaborate to complete user requests.

## Architecture

### Multi-Agent System

The app uses 4 agents defined in `/app/src/main/java/com/roubao/autopilot/agent/`:

1. **Manager** (`Manager.kt`) - Plans high-level task execution, breaks down complex requests into subgoals, tracks progress
2. **Executor** (`Executor.kt`) - Analyzes screenshots and decides specific actions (tap, swipe, type, etc.)
3. **Reflector** (`Reflector.kt`) - Evaluates action results, determines success/failure
4. **Notetaker** (`Notetaker.kt`) - Records important information during execution

### Core Components

| File | Purpose |
|------|---------|
| `MobileAgent.kt` | Main orchestrator - coordinates all agents, manages execution loop |
| `InfoPool.kt` | Shared state between agents (plan, action history, errors, etc.) |
| `VLMClient.kt` | API client for vision language models (OpenAI-compatible) |
| `SettingsManager.kt` | Persists user settings, API keys (encrypted), provider configs |
| `SkillRegistry.kt` | Manages "skills" - predefined workflows for common tasks |
| `ToolManager.kt` | Manages available tools (open_app, search_apps, clipboard, etc.) |

### Key Directories

```
app/src/main/java/com/roubao/autopilot/
├── agent/          # Multi-agent system
├── controller/     # Screen capture, accessibility service, input injection
├── data/           # Settings, database
├── skills/         # Skill system for predefined workflows
├── tools/          # Tool implementations
├── ui/             # Compose UI screens
├── vlm/            # VLM API client
└── service/        # Android services (overlay, accessibility)
```

## API Provider Support

Configured in `SettingsManager.kt` and `VLMClient.kt`:

| Provider | Base URL | Notes |
|----------|----------|-------|
| Aliyun (Qwen) | `https://dashscope.aliyuncs.com/compatible-mode/v1` | Default, Chinese provider |
| OpenAI | `https://api.openai.com/v1` | GPT-4V |
| OpenRouter | `https://openrouter.ai/api/v1` | Multi-provider proxy |
| Custom | User-defined | For self-hosted or other APIs |

### Gemini Support (Added Dec 2024)

Google's Gemini API works via OpenAI-compatible endpoint but doesn't support all parameters.

**Configuration:**
- Base URL: `https://generativelanguage.googleapis.com/v1beta/openai`
- Model: `gemini-2.0-flash` or `gemini-2.5-flash`

**Code change in `VLMClient.kt`** (line ~146):
```kotlin
val isGemini = baseUrl.contains("googleapis.com") || model.contains("gemini")
val requestBody = JSONObject().apply {
    put("model", model)
    put("messages", messages)
    put("max_tokens", 4096)
    put("temperature", 0.0)
    // Gemini doesn't support these parameters
    if (!isGemini) {
        put("top_p", 0.85)
        put("frequency_penalty", 0.2)
    }
}
```

## Task Completion Detection

In `MobileAgent.kt` (line ~223), task completion is detected when the Manager outputs "Finished":

```kotlin
val planLower = planResult.plan.lowercase().trim()
val isFinished = planLower == "finished" ||
        planLower == "finished." ||
        planLower.startsWith("finished.") ||
        planLower.startsWith("finished!") ||
        planLower.startsWith("finished -") ||
        (planResult.plan.contains("Finished") && planResult.plan.length < 50)
```

## Building on NixOS

The project uses Gradle but requires special handling on NixOS due to dynamically linked binaries (AAPT2).

### shell.nix

The `shell.nix` provides an FHS environment to run Android build tools:

```bash
# Enter nix-shell and use FHS environment
NIXPKGS_ALLOW_UNFREE=1 NIXPKGS_ACCEPT_ANDROID_SDK_LICENSE=1 nix-shell --run "android-fhs" <<'EOF'
cd /home/char/roubao
./gradlew assembleDebug --no-daemon
EOF
```

### Quick Build & Install

```bash
# Build
NIXPKGS_ALLOW_UNFREE=1 NIXPKGS_ACCEPT_ANDROID_SDK_LICENSE=1 nix-shell --run "android-fhs" <<'EOF'
cd /home/char/roubao
./gradlew assembleDebug --no-daemon
EOF

# Install (phone must be connected with USB debugging)
adb install -r /home/char/roubao/app/build/outputs/apk/debug/app-debug.apk
```

### Signature Mismatch

If you get `INSTALL_FAILED_UPDATE_INCOMPATIBLE`, uninstall first:
```bash
adb uninstall com.roubao.autopilot && adb install /home/char/roubao/app/build/outputs/apk/debug/app-debug.apk
```

## UI & Theming

- Built with Jetpack Compose
- Theme defined in `ui/theme/` - uses `BaoziTheme.colors`
- Dark mode support via `ThemeMode` enum in `SettingsManager.kt`
- All UI text has been translated from Chinese to English

### Main Screens

| Screen | File | Purpose |
|--------|------|---------|
| Home | `HomeScreen.kt` | Task input, execution control |
| History | `HistoryScreen.kt` | Past task logs |
| Capabilities | `CapabilitiesScreen.kt` | Shows agents and tools |
| Settings | `SettingsScreen.kt` | API config, preferences |

## Skills System

Skills are predefined workflows loaded from `assets/skills.json`. They provide:
- Keywords for intent matching
- Related apps with package names
- Step-by-step instructions for the agent
- Deep link support for direct app navigation

The `SkillManager.kt` uses LLM to match user intent to available skills.

## Common Issues & Solutions

### 401 "No cookie auth credentials found"
- Wrong API endpoint (using web URL instead of API URL)
- Missing or invalid API key
- Wrong provider selected for the API key

### 400 "Unknown name frequency_penalty"
- Gemini API doesn't support `frequency_penalty` or `top_p`
- Fixed by detecting Gemini endpoints and omitting those params

### Task doesn't stop after completion
- Manager not outputting "Finished" clearly
- Fixed by improving completion detection regex

### NixOS build fails with AAPT2 error
- Use the FHS environment: `android-fhs` inside nix-shell
- Run with `--no-daemon` flag

## Future Enhancement Ideas

1. **Custom home screen knowledge** - Settings field to describe KLWP or custom launcher layouts
2. **More robust Gemini support** - Handle other Gemini-specific API differences
3. **Local model support** - Ollama or other local VLM inference
4. **Action recording** - Learn from user demonstrations

## Package Structure

```
com.roubao.autopilot
├── App.kt                    # Application class
├── MainActivity.kt           # Main activity
├── agent/                    # Multi-agent system
├── controller/               # Device control
├── data/                     # Data layer
├── service/                  # Android services
├── skills/                   # Skill definitions
├── tools/                    # Tool implementations
├── ui/                       # Compose UI
│   ├── components/          # Reusable components
│   ├── screens/             # Screen composables
│   └── theme/               # Theming
└── vlm/                      # VLM client
```

## Key Dependencies

- Kotlin + Jetpack Compose for UI
- OkHttp for API calls
- AndroidX Security for encrypted preferences
- Firebase Crashlytics for crash reporting
- Accessibility Service for UI automation

---

*Last updated: December 30, 2024*
*Changes: Gemini API support, improved task completion detection, English translations*
