package com.fluencycode.aicommit.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * AI Commit Assistant 设置
 */
@State(
    name = "com.fluencycode.aicommit.settings.AiCommitSettings",
    storages = [Storage("AiCommitSettings.xml")]
)
class AiCommitSettings : PersistentStateComponent<AiCommitSettings> {
    
    companion object {
        fun getInstance(): AiCommitSettings = ApplicationManager.getApplication().getService(AiCommitSettings::class.java)
    }
    
    // AI 服务配置
    var serviceUrl: String = "http://localhost:8000"
    var apiKey: String = ""
    var model: String = "qwen"  // qwen, kimi, openai
    
    // 触发模式
    var triggerMode: TriggerMode = TriggerMode.MANUAL
    
    // 详细程度
    var detailLevel: DetailLevel = DetailLevel.STANDARD
    
    // 提交规范
    var commitTemplate: String = "conventional"  // conventional, custom
    var customRules: String = ""  // JSON 格式的规则定义
    
    // 其他选项
    var showDiffPreview: Boolean = true
    var autoCopyToClipboard: Boolean = false
    var includeFileCount: Boolean = false
    var language: String = "zh"  // zh, en
    
    override fun getState(): AiCommitSettings = this
    
    override fun loadState(state: AiCommitSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }
}

enum class TriggerMode {
    AUTO,      // 自动生成
    MANUAL     // 手动触发
}

enum class DetailLevel {
    BRIEF,     // 简洁（只标题）
    STANDARD,  // 标准（标题 + 正文）
    DETAILED   // 详细（标题 + 正文+Breaking）
}
