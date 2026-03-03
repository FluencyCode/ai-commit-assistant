package com.fluencycode.aicommit.vcs

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import svn4idea.SvnUtil
import svn4idea.SvnVcs
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * SVN 版本控制提供者
 */
class SvnProvider : VcsProvider {
    
    override fun isSupported(project: Project): Boolean {
        val vcs = SvnVcs.getInstance(project)
        return vcs != null && vcs.rootDirectories.isNotEmpty()
    }
    
    override fun getChangedFiles(project: Project): List<VcsFile> {
        val vcs = SvnVcs.getInstance(project) ?: return emptyList()
        
        return try {
            // 执行 svn status
            val root = vcs.rootDirectories.firstOrNull()?.path ?: return emptyList()
            val process = ProcessBuilder("svn", "status", root)
                .redirectErrorStream(true)
                .start()
            
            val files = mutableListOf<VcsFile>()
            BufferedReader(InputStreamReader(process.inputStream)).useLines { lines ->
                lines.forEach { line ->
                    if (line.isNotEmpty()) {
                        val statusChar = line[0]
                        val filePath = line.substring(4).trim()
                        
                        val status = when (statusChar) {
                            'A' -> FileStatus.ADDED
                            'M' -> FileStatus.MODIFIED
                            'D' -> FileStatus.DELETED
                            'R' -> FileStatus.RENAMED
                            'C' -> FileStatus.COPIED
                            '?' -> FileStatus.UNKNOWN  // 未版本化
                            else -> FileStatus.UNKNOWN
                        }
                        
                        files.add(VcsFile(
                            path = filePath,
                            status = status,
                            type = VcsType.SVN,
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
        val vcs = SvnVcs.getInstance(project) ?: return ""
        
        return try {
            val root = vcs.rootDirectories.firstOrNull()?.path ?: return ""
            
            val process = when (file.status) {
                FileStatus.ADDED -> {
                    // 新增文件：显示内容
                    ProcessBuilder("svn", "cat", file.path)
                }
                FileStatus.DELETED -> {
                    // 删除文件：显示 diff
                    ProcessBuilder("svn", "diff", "--old", "HEAD", "--new", "BASE", file.path)
                }
                else -> {
                    // 修改文件：显示 diff
                    ProcessBuilder("svn", "diff", file.path)
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
        return true
    }
    
    override fun getType(): VcsType = VcsType.SVN
}
