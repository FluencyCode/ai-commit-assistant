package com.fluencycode.aicommit.actions

import com.fluencycode.aicommit.ai.AiCommitService
import com.fluencycode.aicommit.settings.AiCommitSettings
import com.fluencycode.aicommit.vcs.GitProvider
import com.fluencycode.aicommit.vcs.SvnProvider
import com.fluencycode.aicommit.vcs.VcsProvider
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 生成提交信息动作
 */
class GenerateCommitAction : AnAction() {
    
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        
        // 显示加载提示
        Messages.showInfoMessage(project, "Generating commit message...", "AI Commit Assistant")
        
        // 异步生成
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val vcsProvider = getVcsProvider(project)
                if (vcsProvider == null) {
                    Messages.showErrorDialog(project, "No VCS found", "Error")
                    return@launch
                }
                
                val files = vcsProvider.getChangedFiles(project)
                if (files.isEmpty()) {
                    Messages.showInfoMessage(project, "No changes to commit", "Info")
                    return@launch
                }
                
                val diffs = files.associateWith { file ->
                    vcsProvider.getDiff(project, file)
                }
                
                val settings = AiCommitSettings.getInstance()
                val service = AiCommitService()
                
                val message = service.generateMessage(
                    files = files,
                    diffs = diffs.mapKeys { it.key.path },
                    detailLevel = settings.detailLevel,
                    language = settings.language
                )
                
                // 显示生成的消息
                Messages.showInputDialog(
                    project,
                    "Generated Commit Message:",
                    "AI Commit Assistant",
                    Messages.getQuestionIcon(),
                    message.format(),
                    null
                )
                
            } catch (ex: Exception) {
                Messages.showErrorDialog(project, "Error: ${ex.message}", "Error")
            }
        }
    }
    
    override fun update(e: AnActionEvent) {
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null && getVcsProvider(project) != null
    }
    
    private fun getVcsProvider(project: Project): VcsProvider? {
        return when {
            GitProvider().isSupported(project) -> GitProvider()
            SvnProvider().isSupported(project) -> SvnProvider()
            else -> null
        }
    }
}

/**
 * 预览提交信息动作
 */
class PreviewCommitAction : AnAction() {
    
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        Messages.showInfoMessage(project, "Preview dialog coming soon...", "AI Commit Assistant")
    }
    
    override fun update(e: AnActionEvent) {
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null && (
            GitProvider().isSupported(project) || 
            SvnProvider().isSupported(project)
        )
    }
}
