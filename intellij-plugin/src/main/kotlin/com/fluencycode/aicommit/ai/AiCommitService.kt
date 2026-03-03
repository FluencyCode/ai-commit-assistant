package com.fluencycode.aicommit.ai

import com.fluencycode.aicommit.settings.AiCommitSettings
import com.fluencycode.aicommit.settings.DetailLevel
import com.fluencycode.aicommit.vcs.VcsFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

/**
 * AI 提交信息服务
 */
class AiCommitService {
    
    private val client = OkHttpClient()
    private val mapper = jacksonObjectMapper()
    
    /**
     * 生成提交信息
     */
    suspend fun generateMessage(
        files: List<VcsFile>,
        diffs: Map<String, String>,
        detailLevel: DetailLevel,
        language: String = "zh"
    ): CommitMessage {
        val settings = AiCommitSettings.getInstance()
        
        // 构建请求
        val requestBody = mapOf(
            "files" to files.map { it.path },
            "diffs" to diffs,
            "detail_level" to detailLevel.name.lowercase(),
            "language" to language,
            "template" to settings.commitTemplate
        )
        
        val json = mapper.writeValueAsString(requestBody)
        
        val request = Request.Builder()
            .url("${settings.serviceUrl}/generate")
            .post(json.toRequestBody("application/json".toMediaType()))
            .addHeader("Authorization", "Bearer ${settings.apiKey}")
            .build()
        
        return try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: throw Exception("Empty response")
            
            if (response.isSuccessful) {
                val result: Map<String, Any> = mapper.readValue(responseBody)
                CommitMessage(
                    type = result["type"] as? String ?: "feat",
                    scope = result["scope"] as? String,
                    subject = result["subject"] as? String ?: "",
                    body = result["body"] as? String,
                    breakingChanges = result["breaking_changes"] as? String
                )
            } else {
                throw Exception("API error: ${response.code}")
            }
        } catch (e: Exception) {
            // 失败时返回空消息
            CommitMessage(
                type = "feat",
                scope = null,
                subject = "Update code",
                body = null,
                breakingChanges = null
            )
        }
    }
}

/**
 * 提交信息
 */
data class CommitMessage(
    val type: String,
    val scope: String?,
    val subject: String,
    val body: String?,
    val breakingChanges: String?
) {
    /**
     * 格式化为字符串
     */
    fun format(): String {
        val header = if (scope != null) {
            "$type($scope): $subject"
        } else {
            "$type: $subject"
        }
        
        return buildString {
            appendLine(header)
            if (body != null && body.isNotBlank()) {
                appendLine()
                appendLine(body)
            }
            if (breakingChanges != null && breakingChanges.isNotBlank()) {
                appendLine()
                appendLine("BREAKING CHANGE: $breakingChanges")
            }
        }.trimEnd()
    }
}
