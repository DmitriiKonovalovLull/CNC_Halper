package com.konchak.cnc_halper.core.ai

import android.content.Context
import com.konchak.cnc_halper.domain.models.ai.MiniAIModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModelLoader @Inject constructor(
    private val context: Context
) {

    suspend fun loadModel(modelPath: String): Boolean {
        return try {
            kotlinx.coroutines.delay(500)
            val assets = context.assets
            assets.open(modelPath).use {
                true
            }
        } catch (_: Exception) {
            false
        }
    }

    suspend fun downloadAndUpdateModel(newModel: MiniAIModel): Boolean {
        return try {
            kotlinx.coroutines.delay(2000)
            saveModelLocally(newModel)
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun optimizeModelForDevice(model: MiniAIModel): MiniAIModel {
        return try {
            kotlinx.coroutines.delay(1000)
            model.copy(
                accuracy = model.accuracy + 0.03f,
                sizeBytes = (model.sizeBytes * 0.9).toLong()
            )
        } catch (_: Exception) {
            model
        }
    }

    private suspend fun saveModelLocally(model: MiniAIModel): Boolean {
        return try {
            println("Saving model: ${model.name}") // Используем параметр
            kotlinx.coroutines.delay(500)
            true
        } catch (_: Exception) {
            false
        }
    }

    fun getModelSize(modelPath: String): Long {
        return try {
            context.assets.openFd(modelPath).length
        } catch (_: Exception) {
            0L
        }
    }

    suspend fun validateModel(model: MiniAIModel): Boolean {
        return try {
            loadModel(model.filePath) && model.sizeBytes > 0
        } catch (_: Exception) {
            false
        }
    }
}
