# 公众号文章：AI Commit Assistant 立项

**标题：** 第一个工具：AI Git/SVN 提交助手（开篇）

---

🎉 **AI 早班车工具箱系列，今天正式开更！**

我是小刘，一个 5 年全栈工程师。

从今天开始，我会用 7 个真实的 AI 工具，证明一件事：

**AI 不是来取代程序员的，是来帮我们早点下班的。**

---

## 🤔 为什么做这个工具？

每个开发者都经历过：

```bash
$ git commit -m ""
```

然后对着空引号发呆：
- 今天改了啥来着？
- 怎么写出规范的 commit message？
- feat 还是 fix？breaking change 怎么写？

**更痛苦的是：**
- 改了 10 个文件，不知道咋总结
- 想写英文 commit，词汇量不够
- 团队要求规范，记不住格式

**SVN 用户也一样：**
- 提交信息写"update"了事
- 想写好，但太麻烦
- 代码审查时看不懂历史记录

**所以，我要做一个 AI Git/SVN 提交助手。**

---

## 🎯 功能设计

**核心功能：**

1. **双版本控制支持**
   - ✅ Git（Git4Idea）
   - ✅ SVN（SVN Plugin）

2. **两种触发模式**
   - 🤖 自动生成：打开 Commit 窗口时自动生成
   - 🖱️ 手动触发：点击按钮生成

3. **符合提交规范**
   - 📏 Conventional Commits 规范
   - ✏️ 支持自定义规则

4. **详细程度可调**
   - 📝 简洁：只生成标题
   - 📄 标准：标题 + 简要说明
   - 📖 详细：标题 + 详细说明+Breaking Changes

5. **代码对比预览**
   - 👁️ 文件列表
   - 🔍 Diff 预览（高亮显示）
   - ✅ 用户审查后再提交

---

## 💡 效果预览

**用户操作流程：**

```
1. 用户修改代码
       ↓
2. 点击 Commit（Git）或 Checkin（SVN）
       ↓
3. 弹出 AI Commit 预览窗口
   ┌─────────────────────────────────┐
   │  代码对比区域                    │
   │  - src/main.java (+15, -3)      │
   │  - utils/helper.js (+8, -1)     │
   │                                 │
   │  生成的 Commit Message:          │
   │  feat(user): add login feature  │
   │                                 │
   │  - Implemented JWT auth         │
   │  - Added login API endpoint     │
   │  - Updated user model           │
   │                                 │
   │  [✏️ 编辑] [🔄 重生成] [✅ 提交] │
   └─────────────────────────────────┘
       ↓
4. 用户审查/编辑（可选）
       ↓
5. 完成提交
```

**生成的 Commit Message：**

```
feat(user): add login feature

- Implemented JWT authentication
- Added login API endpoint
- Updated user model with new fields
- Added unit tests for auth service

BREAKING CHANGE: Changed login API signature
```

---

## 🛠️ 技术选型

| 模块 | 技术 | 说明 |
|------|------|------|
| IntelliJ 插件 | Kotlin + Git4Idea | 服务 Java/Kotlin 开发者 |
| SVN 支持 | Kotlin + SVN Plugin | 服务传统项目开发者 |
| AI 服务 | Python + FastAPI | 统一后端 |
| 大模型 | 通义千问/Kimi | 中文友好 |

**为什么双版本控制？**
- ✅ Git 用户多（现代项目）
- ✅ SVN 用户也不少（传统企业）
- ✅ 一次开发，覆盖所有人

**为什么 Kotlin？**
- ✅ IntelliJ 官方推荐
- ✅ 与 Java 100% 兼容
- ✅ 代码简洁

---

## 📦 项目结构

```
ai-commit-assistant/
├── intellij-plugin/
│   ├── src/main/kotlin/
│   │   └── com/fluencycode/aicommit/
│   │       ├── settings/          # 设置页面
│   │       ├── vcs/               # Git/SVN 支持
│   │       ├── ai/                # AI 服务
│   │       ├── rules/             # 提交规则
│   │       ├── ui/                # UI 组件
│   │       └── actions/           # 触发动作
│   └── build.gradle.kts
│
├── ai-service/
│   └── app/                       # FastAPI 服务
│
└── vscode-plugin/                 # 后续开发
```

**仓库地址：** https://github.com/FluencyCode/ai-commit-assistant

---

## 📅 开发计划

| 时间 | 任务 | 产出 |
|------|------|------|
| Day 1 | 框架 + 设置页面 | 可运行的空壳插件 |
| Day 2 | Git + SVN 支持 | 能读取 diff |
| Day 3 | AI 服务集成 | 能生成 commit message |
| Day 4 | UI + 预览 | 完整的用户界面 |
| Day 5 | 优化 + 发布 | 插件市场发布 |

**预计 5 天完成，所有代码开源！**

---

## 🚀 开发进度

**✅ 已完成（Day 1）：**
- 项目初始化
- 设置页面 UI
- 插件配置

**⏳ 进行中：**
- Git/SVN 支持
- AI 服务集成

**📅 即将开始：**
- UI 预览对话框
- 测试优化

---

## 💬 互动话题

**你平时写 commit message 吗？**

A. 每次都认真写
B. 看心情
C. 基本写"update"
D. 什么是 commit message？

**评论区告诉我！** 👇

---

## 📬 最后

这里没有高深的理论，只有能用的工具和真实的代码。

我就是一个普通的全栈工程师，想用 AI 让工作轻松一点。

**关注我，一起用 AI 让搬砖更优雅！** 🧰

---

**小刘**
2026 年 3 月 3 日

**公众号：AI 早班车**
**Slogan：AI 早班车，每天准时出发** 🚌

---

**下一篇：**《IntelliJ 插件框架搭建》明天更新！
