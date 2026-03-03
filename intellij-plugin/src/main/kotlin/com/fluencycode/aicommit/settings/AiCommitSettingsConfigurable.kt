package com.fluencycode.aicommit.settings

import com.fluencycode.aicommit.settings.DetailLevel
import com.fluencycode.aicommit.settings.TriggerMode
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.ComboBox
import javax.swing.*

/**
 * AI Commit Assistant 设置页面
 */
class AiCommitSettingsConfigurable : Configurable {
    
    private lateinit var serviceUrlField: JTextField
    private lateinit var apiKeyField: JPasswordField
    private lateinit var modelComboBox: JComboBox<String>
    private lateinit var triggerModeComboBox: JComboBox<TriggerMode>
    private lateinit var detailLevelComboBox: JComboBox<DetailLevel>
    private lateinit var templateComboBox: JComboBox<String>
    private lateinit var showDiffPreviewCheckBox: JCheckBox
    private lateinit var autoCopyCheckBox: JCheckBox
    private lateinit var includeFileCountCheckBox: JCheckBox
    private lateinit var languageComboBox: JComboBox<String>
    
    override fun getDisplayName(): String = "AI Commit Assistant"
    
    override fun createComponent(): JComponent {
        val settings = AiCommitSettings.getInstance()
        
        // 服务配置
        val servicePanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            border = BorderFactory.createTitledBorder("AI 服务配置")
            
            add(JLabel("Service URL:").apply { alignmentX = LEFT_ALIGNMENT })
            serviceUrlField = JTextField(settings.serviceUrl, 30).apply { 
                alignmentX = LEFT_ALIGNMENT 
                maximumSize = java.awt.Dimension(Short.MAX_VALUE, maximumSize.height)
            }
            add(serviceUrlField)
            
            add(Box.createVerticalStrut(10))
            
            add(JLabel("API Key:").apply { alignmentX = LEFT_ALIGNMENT })
            apiKeyField = JPasswordField(settings.apiKey, 30).apply { 
                alignmentX = LEFT_ALIGNMENT 
                maximumSize = java.awt.Dimension(Short.MAX_VALUE, maximumSize.height)
            }
            add(apiKeyField)
            
            add(Box.createVerticalStrut(10))
            
            add(JLabel("Model:").apply { alignmentX = LEFT_ALIGNMENT })
            modelComboBox = JComboBox(arrayOf("qwen", "kimi", "openai")).apply {
                selectedItem = settings.model
                alignmentX = LEFT_ALIGNMENT
            }
            add(modelComboBox)
        }
        
        // 提交配置
        val commitPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            border = BorderFactory.createTitledBorder("提交配置")
            
            add(JLabel("Trigger Mode:").apply { alignmentX = LEFT_ALIGNMENT })
            triggerModeComboBox = ComboBox(arrayOf(TriggerMode.AUTO, TriggerMode.MANUAL)).apply {
                selectedItem = settings.triggerMode
                alignmentX = LEFT_ALIGNMENT
                renderer = object : DefaultListCellRenderer() {
                    override fun getText(value: Any?): String {
                        return when (value) {
                            TriggerMode.AUTO -> "自动生成（打开 Commit 窗口时）"
                            TriggerMode.MANUAL -> "手动触发（点击按钮）"
                            else -> value.toString()
                        }
                    }
                }
            }
            add(triggerModeComboBox)
            
            add(Box.createVerticalStrut(10))
            
            add(JLabel("Detail Level:").apply { alignmentX = LEFT_ALIGNMENT })
            detailLevelComboBox = ComboBox(arrayOf(DetailLevel.BRIEF, DetailLevel.STANDARD, DetailLevel.DETAILED)).apply {
                selectedItem = settings.detailLevel
                alignmentX = LEFT_ALIGNMENT
                renderer = object : DefaultListCellRenderer() {
                    override fun getText(value: Any?): String {
                        return when (value) {
                            DetailLevel.BRIEF -> "简洁（只生成标题）"
                            DetailLevel.STANDARD -> "标准（标题 + 简要说明）"
                            DetailLevel.DETAILED -> "详细（标题 + 详细说明）"
                            else -> value.toString()
                        }
                    }
                }
            }
            add(detailLevelComboBox)
            
            add(Box.createVerticalStrut(10))
            
            add(JLabel("Commit Template:").apply { alignmentX = LEFT_ALIGNMENT })
            templateComboBox = JComboBox(arrayOf("conventional", "custom")).apply {
                selectedItem = settings.commitTemplate
                alignmentX = LEFT_ALIGNMENT
            }
            add(templateComboBox)
        }
        
        // 其他选项
        val optionsPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            border = BorderFactory.createTitledBorder("其他选项")
            
            showDiffPreviewCheckBox = JCheckBox("Show diff preview before commit", settings.showDiffPreview)
            add(showDiffPreviewCheckBox)
            
            autoCopyCheckBox = JCheckBox("Auto-copy to clipboard", settings.autoCopyToClipboard)
            add(autoCopyCheckBox)
            
            includeFileCountCheckBox = JCheckBox("Include file count in message", settings.includeFileCount)
            add(includeFileCountCheckBox)
            
            add(Box.createVerticalStrut(10))
            
            add(JLabel("Language:").apply { alignmentX = LEFT_ALIGNMENT })
            languageComboBox = JComboBox(arrayOf("zh", "en")).apply {
                selectedItem = settings.language
                alignmentX = LEFT_ALIGNMENT
            }
            add(languageComboBox)
        }
        
        // 主面板
        return JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(servicePanel)
            add(Box.createVerticalStrut(10))
            add(commitPanel)
            add(Box.createVerticalStrut(10))
            add(optionsPanel)
            add(Box.createVerticalGlue())
        }
    }
    
    override fun isModified(): Boolean {
        val settings = AiCommitSettings.getInstance()
        return serviceUrlField.text != settings.serviceUrl ||
               String(apiKeyField.password) != settings.apiKey ||
               modelComboBox.selectedItem != settings.model ||
               triggerModeComboBox.selectedItem != settings.triggerMode ||
               detailLevelComboBox.selectedItem != settings.detailLevel ||
               templateComboBox.selectedItem != settings.commitTemplate ||
               showDiffPreviewCheckBox.isSelected != settings.showDiffPreview ||
               autoCopyCheckBox.isSelected != settings.autoCopyToClipboard ||
               includeFileCountCheckBox.isSelected != settings.includeFileCount ||
               languageComboBox.selectedItem != settings.language
    }
    
    override fun apply() {
        val settings = AiCommitSettings.getInstance()
        settings.serviceUrl = serviceUrlField.text
        settings.apiKey = String(apiKeyField.password)
        settings.model = modelComboBox.selectedItem as String
        settings.triggerMode = triggerModeComboBox.selectedItem as TriggerMode
        settings.detailLevel = detailLevelComboBox.selectedItem as DetailLevel
        settings.commitTemplate = templateComboBox.selectedItem as String
        settings.showDiffPreview = showDiffPreviewCheckBox.isSelected
        settings.autoCopyToClipboard = autoCopyCheckBox.isSelected
        settings.includeFileCount = includeFileCountCheckBox.isSelected
        settings.language = languageComboBox.selectedItem as String
    }
    
    override fun reset() {
        val settings = AiCommitSettings.getInstance()
        serviceUrlField.text = settings.serviceUrl
        apiKeyField.text = settings.apiKey
        modelComboBox.selectedItem = settings.model
        triggerModeComboBox.selectedItem = settings.triggerMode
        detailLevelComboBox.selectedItem = settings.detailLevel
        templateComboBox.selectedItem = settings.commitTemplate
        showDiffPreviewCheckBox.isSelected = settings.showDiffPreview
        autoCopyCheckBox.isSelected = settings.autoCopyToClipboard
        includeFileCountCheckBox.isSelected = settings.includeFileCount
        languageComboBox.selectedItem = settings.language
    }
}
