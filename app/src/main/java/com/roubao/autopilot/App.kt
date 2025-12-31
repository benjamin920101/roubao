package com.roubao.autopilot

import android.app.Application
import android.content.pm.PackageManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.roubao.autopilot.controller.AppScanner
import com.roubao.autopilot.controller.DeviceController
import com.roubao.autopilot.data.SettingsManager
import com.roubao.autopilot.skills.SkillManager
import com.roubao.autopilot.tools.ToolManager
import com.roubao.autopilot.utils.CrashHandler
import rikka.shizuku.Shizuku

class App : Application() {

    lateinit var deviceController: DeviceController
        private set
    lateinit var appScanner: AppScanner
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Initialize crash capture (local logs)
        CrashHandler.getInstance().init(this)

        // Initialize Firebase Crashlytics (based on user settings)
        val settingsManager = SettingsManager(this)
        val cloudCrashReportEnabled = settingsManager.settings.value.cloudCrashReportEnabled
        FirebaseCrashlytics.getInstance().apply {
            setCrashlyticsCollectionEnabled(cloudCrashReportEnabled)
            setCustomKey("app_version", BuildConfig.VERSION_NAME)
            setCustomKey("device_model", android.os.Build.MODEL)
            setCustomKey("android_version", android.os.Build.VERSION.SDK_INT.toString())
            // Send pending crash reports
            sendUnsentReports()
        }
        println("[App] Cloud crash reporting: ${if (cloudCrashReportEnabled) "enabled" else "disabled"}")

        // Initialize Shizuku
        Shizuku.addRequestPermissionResultListener(REQUEST_PERMISSION_RESULT_LISTENER)

        // Initialize core components
        initializeComponents()
    }

    private fun initializeComponents() {
        // Initialize device controller
        deviceController = DeviceController(this)
        deviceController.setCacheDir(cacheDir)

        // Initialize app scanner
        appScanner = AppScanner(this)

        // Initialize Tools layer
        val toolManager = ToolManager.init(this, deviceController, appScanner)

        // Async pre-scan installed apps (avoid ANR)
        println("[App] Starting async scan of installed apps...")
        Thread {
            appScanner.refreshApps()
            println("[App] Scanned ${appScanner.getApps().size} apps")
        }.start()

        // Initialize Skills layer (pass appScanner for installed app detection)
        val skillManager = SkillManager.init(this, toolManager, appScanner)
        println("[App] SkillManager loaded ${skillManager.getAllSkills().size} Skills")

        println("[App] Component initialization complete")
    }

    override fun onTerminate() {
        super.onTerminate()
        Shizuku.removeRequestPermissionResultListener(REQUEST_PERMISSION_RESULT_LISTENER)
    }

    /**
     * Dynamically update cloud crash reporting toggle
     */
    fun updateCloudCrashReportEnabled(enabled: Boolean) {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(enabled)
        println("[App] Cloud crash reporting ${if (enabled) "enabled" else "disabled"}")
    }

    companion object {
        @Volatile
        private var instance: App? = null

        fun getInstance(): App {
            return instance ?: throw IllegalStateException("App not initialized")
        }

        private val REQUEST_PERMISSION_RESULT_LISTENER =
            Shizuku.OnRequestPermissionResultListener { requestCode, grantResult ->
                val granted = grantResult == PackageManager.PERMISSION_GRANTED
                println("[Shizuku] Permission result: $granted")
            }
    }
}
