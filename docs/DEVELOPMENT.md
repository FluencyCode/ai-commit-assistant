# AI Commit Assistant - 开发文档

## 项目概述

AI Commit Assistant 是一款智能代码提交助手，支持 Git 和 SVN 两种版本控制系统，通过 AI 分析代码变更自动生成符合规范的提交信息。

## 功能设计

### 核心功能

1. **双版本控制支持**
   - Git（通过 Git4Idea）
   - SVN（通过 SVN Plugin）

2. **触发模式**
   - 自动生成：打开 Commit 窗口时自动生成
   - 手动触发：点击按钮生成

3. **提交规范**
   - Conventional Commits 规范
   - 支持自定义规则

4. **详细程度**
   - 简洁：只生成标题
   - 标准：标题 + 简要说明
   - 详细：标题 + 详细说明+Breaking Changes

5. **代码对比预览**
   - 文件列表
   - Diff 预览（高亮显示）
   - 用户审查

### 用户操作流程

```
1. 用户修改代码
       ↓
2. 点击 Commit/Checkin
       ↓
3. 弹出 AI Commit 预览窗口
       ↓
4. 用户审查/编辑（可选）
       ↓
5. 完成提交
```

## 技术架构

```
┌─────────────────────────────────────────┐
│            IntelliJ Plugin              │
│                                         │
│  ┌───────────┐  ┌───────────┐          │
│  │ Git       │  │ SVN       │          │
│  │ Provider  │  │ Provider  │          │
│  └─────┬─────┘  └─────┬─────┘          │
│        └──────┬───────┘                 │
│               ↓                          │
│        ┌─────────────┐                  │
│        │ Diff Reader │                  │
│        └──────┬──────┘                  │
│               ↓                          │
│        ┌─────────────┐                  │
│        │ AI Service  │                  │
│        └──────┬──────┘                  │
│               ↓                          │
│        ┌─────────────┐                  │
│        │ Preview UI  │                  │
│        └─────────────┘                  │
└─────────────────────────────────────────┘
              ↓ HTTP
┌─────────────────────────────────────────┐
│         AI Service (FastAPI)            │
│                                         │
│  ┌─────────────┐  ┌─────────────┐      │
│  │ Diff        │  │ Rule        │      │
│  │ Analyzer    │  │ Engine      │      │
│  └──────┬──────┘  └──────┬──────┘      │
│         └───────┬───────┘               │
│                 ↓                        │
│         ┌─────────────┐                 │
│         │ LLM Client  │                 │
│         └─────────────┘                 │
└─────────────────────────────────────────┘
```

## 核心接口

### VcsProvider

```kotlin
interface VcsProvider {
    fun isSupported(project: Project): Boolean
    fun getChangedFiles(project: Project): List<VcsFile>
    fun getDiff(project: Project, file: VcsFile): String
    fun commit(project: Project, message: String): Boolean
}
```

### CommitRule

```kotlin
interface CommitRule {
    val name: String
    val description: String
    fun generateMessage(diff: String, detailLevel: DetailLevel): CommitMessage
    fun validate(message: String): Boolean
}
```

### CommitMessage

```kotlin
data class CommitMessage(
    val type: String,          // feat, fix, docs...
    val scope: String?,        // 可选范围
    val subject: String,       // 标题
    val body: String?,         // 正文
    val breakingChanges: String?
)
```

## 开发环境

### 要求

- IntelliJ IDEA 2023.1+
- JDK 17+
- Kotlin 1.9+
- Gradle 8.0+

### 依赖

- Git4Idea
- SVN Plugin
- OkHttp
- FastAPI (AI 服务)

## 构建指南

### IntelliJ 插件

```bash
cd intellij-plugin
./gradlew buildPlugin
```

构建产物：`build/distributions/ai-commit-assistant-*.zip`

### AI 服务

```bash
cd ai-service
pip install -r requirements.txt
python -m uvicorn app.main:app --reload
```

## 配置说明

### 插件配置

打开 `Settings → Tools → AI Commit Assistant`

- **Service URL**: AI 服务地址（默认：http://localhost:8000）
- **API Key**: API 密钥（可选）
- **Trigger Mode**: 触发模式（自动/手动）
- **Detail Level**: 详细程度（简洁/标准/详细）
- **Commit Template**: 提交模板

### AI 服务配置

创建 `.env` 文件：

```env
OPENAI_API_KEY=your_key
KIMI_API_KEY=your_key
QWEN_API_KEY=your_key
SERVICE_HOST=0.0.0.0
SERVICE_PORT=8000
```

## 测试指南

### 单元测试

```bash
./gradlew test
```

### 集成测试

1. 启动 AI 服务
2. 运行插件（`./gradlew runIde`）
3. 打开测试项目
4. 修改代码并提交
5. 验证生成的 commit message

## 发布指南

### IntelliJ Plugin Marketplace

1. 构建插件：`./gradlew buildPlugin`
2. 访问：https://plugins.jetbrains.com/plugin/uploadPlugin
3. 上传 `build/distributions/ai-commit-assistant-*.zip`
4. 填写插件信息
5. 提交审核

### VSCode Marketplace

1. 打包：`vsce package`
2. 发布：`vsce publish`

## 常见问题

### Q: SVN 不支持怎么办？

A: 确保已安装 SVN Plugin（Settings → Plugins → 搜索 "SVN"）

### Q: AI 服务连接失败？

A: 检查 Service URL 是否正确，确保 AI 服务已启动

### Q: 生成的 commit message 不符合规范？

A: 可以在设置中自定义规则，或手动编辑后提交

## 贡献指南

欢迎提交 Issue 和 Pull Request！

## License

MIT License

---

**AI 早班车** | 用 AI 让搬砖更优雅 🚌
