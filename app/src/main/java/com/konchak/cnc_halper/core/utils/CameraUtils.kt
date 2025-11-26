package com.konchak.cnc_halper.core.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CameraUtils @Inject constructor(
    private val context: Context
) {

    fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun hasStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
    }

    suspend fun requestCameraPermission(): Boolean {
        // В реальном приложении здесь будет логика запроса разрешений
        // Для демо возвращаем true
        return true
    }

    fun getSupportedResolutions(): List<String> {
        return listOf(
            "1920x1080",
            "1280x720",
            "800x600",
            "640x480"
        )
    }

    fun getOptimalResolution(): String {
        return "1280x720" // Баланс качества и производительности
    }

    fun canUseFlash(): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    fun getCameraQualitySettings(): CameraQuality {
        return CameraQuality(
            resolution = getOptimalResolution(),
            jpegQuality = 85,
            enableStabilization = true,
            focusMode = "continuous-picture",
            flashMode = "auto"
        )
    }
}

data class CameraQuality(
    val resolution: String,
    val jpegQuality: Int,
    val enableStabilization: Boolean,
    val focusMode: String,
    val flashMode: String
)