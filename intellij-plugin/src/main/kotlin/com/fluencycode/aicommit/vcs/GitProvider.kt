package com.fluencycode.aicommit.vcs

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import git4idea.GitUtil
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryManager
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Git 版本控制提供者
 */
class GitProvider : VcsProvider {
    
    override fun isSupported(project: Project): Boolean {
        val repositories = GitRepositoryManager.getInstance(project).repositories
        return repositories.isNotEmpty()
    }
    
    override fun getChangedFiles(project: Project): List<VcsFile> {
        val repository = GitRepositoryManager.getInstance(project).repositories.firstOrNull() ?: return emptyList()
        val root = repository.root
        
        return try {
            // 执行 git status --porcelain
            val process = ProcessBuilder("git", "-C", root.path, "status", "--porcelain")
                .redirectErrorStream(true)
                .start()
            
            val files = mutableListOf<VcsFile>()
            BufferedReader(InputStreamReader(process.inputStream)).useLines { lines ->
                lines.forEach { line ->
                    if (line.length >= 3) {
                        val statusChar = line[0]
                        val filePath = line.substring(3).trim()
                        
                        val status = when (statusChar) {
                            'A' -> FileStatus.ADDED
                            'M' -> FileStatus.MODIFIED
                            'D' -> FileStatus.DELETED
                            'R' -> FileStatus.RENAMED
                            'C' -> FileStatus.COPIED
                            else -> FileStatus.UNKNOWN
                        }
                        
                        files.add(VcsFile(
                            path = filePath,
                            status = status,
                            type = VcsType.GIT,
                            virtualFile = null
                        ))
                    }
                }
            }
            
            process.waitFor()
            files
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    override fun getDiff(project: Project, file: VcsFile): String {
        val repository = GitRepositoryManager.getInstance(project).repositories.firstOrNull() ?: return ""
        val root = repository.root
        
        return try {
            val process = when (file.status) {
                FileStatus.ADDED -> {
                    // 新增文件：显示完整内容
                    ProcessBuilder("git", "-C", root.path, "show", ":${file.path}")
                }
                FileStatus.DELETED -> {
                    // 删除文件：显示删除前的内容
                    ProcessBuilder("git", "-C", root.path, "show", "HEAD:${file.path}")
                }
                else -> {
                    // 修改文件：显示 diff
                    ProcessBuilder("git", "-C", root.path, "diff", "--cached", "--", file.path)
                }
            }.redirectErrorStream(true).start()
            
            val diff = BufferedReader(InputStreamReader(process.inputStream)).readText()
            process.waitFor()
            diff
        } catch (e: Exception) {
            ""
        }
    }
    
    override fun commit(project: Project, message: String): Boolean {
        // 实际提交由 IntelliJ 的 Commit 对话框处理
        // 这里只返回 true 表示支持
        return true
    }
    
    override fun getType(): VcsType = VcsType.GIT
}
