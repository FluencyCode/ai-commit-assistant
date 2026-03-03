# AI Commit Assistant 🤖

AI 驱动的 Git/SVN 提交助手，支持 IntelliJ IDEA 和 VSCode 双插件。

## ✨ 功能特性

- 🔄 **双版本控制支持** - Git + SVN 完美兼容
- 🤖 **AI 自动生成** - 智能分析代码变更，生成提交信息
- 📏 **符合规范** - Conventional Commits 规范（支持自定义规则）
- 🎚️ **详细程度可调** - 简洁/标准/详细 三档可选
- 👁️ **代码对比预览** - 提交前审查变更内容
- ⚙️ **灵活配置** - 自动生成/手动触发 可选

## 🛠️ 技术栈

| 模块 | 技术 |
|------|------|
| IntelliJ 插件 | Kotlin + Git4Idea + SVN Plugin |
| VSCode 插件 | TypeScript |
| AI 服务 | Python + FastAPI |
| 大模型 | 通义千问/Kimi |

## 📦 项目结构

```
ai-commit-assistant/
├── intellij-plugin/       # IntelliJ 插件
│   ├── src/main/kotlin/
│   │   └── com/fluencycode/aicommit/
│   │       ├── settings/          # 设置页面
│   │       ├── vcs/               # Git/SVN 支持
│   │       ├── diff/              # Diff 读取
│   │       ├── ai/                # AI 服务
│   │       ├── rules/             # 提交规则
│   │       ├── ui/                # UI 组件
│   │       └── actions/           # 触发动作
│   └── build.gradle.kts
│
├── vscode-plugin/         # VSCode 插件
│   └── src/
│
├── ai-service/            # AI 后端服务
│   └── app/
│
└── docs/                  # 文档
```

## 🚀 开发计划

| 阶段 | 内容 | 周期 |
|------|------|------|
| Day 1 | 框架 + 设置页面 | 1 天 |
| Day 2 | Git + SVN 支持 | 1 天 |
| Day 3 | AI 服务集成 | 1 天 |
| Day 4 | UI + 预览 | 1 天 |
| Day 5 | 优化 + 发布 | 1 天 |

## 📄 相关文章

本工具是「AI 早班车」公众号工具箱系列的第一个项目。

**公众号：AI 早班车**

## 📝 License

MIT License

## 👨‍💻 Author

小刘 - 5 年全栈工程师

用 AI 让搬砖更优雅 🚌
