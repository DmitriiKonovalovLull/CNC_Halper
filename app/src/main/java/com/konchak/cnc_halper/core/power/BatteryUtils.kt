package com.konchak.cnc_halper.core.power

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BatteryUtils @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getBatteryLevel(): Int {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }
        return batteryStatus?.let { intent ->
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            if (level != -1 && scale != -1) {
                (level * 100 / scale.toFloat()).toInt()
            } else {
                -1
            }
        } ?: -1
    }

    fun isCharging(): Boolean {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }
        val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
    }

    fun isPowerSaveMode(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
            powerManager.isPowerSaveMode
        } else {
            false
        }
    }

    @Suppress("unused")
    fun shouldLimitHeavyOperations(): Boolean {
        return getBatteryLevel() < 20 || isPowerSaveMode()
    }

    @Suppress("unused")
    fun canPerformHeavyAITasks(): Boolean {
        return getBatteryLevel() > 30 && !isPowerSaveMode()
    }

    @Suppress("unused")
    fun canPerformBackgroundSync(): Boolean {
        return isCharging() && getBatteryLevel() > 80
    }
}