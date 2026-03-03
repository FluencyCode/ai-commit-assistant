package com.fluencycode.aicommit.vcs

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * 版本控制提供者接口
 */
interface VcsProvider {
    /**
     * 检查是否支持
     */
    fun isSupported(project: Project): Boolean
    
    /**
     * 获取变更的文件列表
     */
    fun getChangedFiles(project: Project): List<VcsFile>
    
    /**
     * 获取文件 diff
     */
    fun getDiff(project: Project, file: VcsFile): String
    
    /**
     * 提交
     */
    fun commit(project: Project, message: String): Boolean
    
    /**
     * 获取 VCS 类型
     */
    fun getType(): VcsType
}

/**
 * VCS 文件
 */
data class VcsFile(
    val path: String,
    val status: FileStatus,
    val type: VcsType,
    val virtualFile: VirtualFile?
)

/**
 * 文件状态
 */
enum class FileStatus {
    ADDED,       // 新增
    MODIFIED,    // 修改
    DELETED,     // 删除
    RENAMED,     // 重命名
    COPIED,      // 复制
    UNKNOWN      // 未知
}

/**
 * VCS 类型
 */
enum class VcsType {
    GIT,
    SVN
}
