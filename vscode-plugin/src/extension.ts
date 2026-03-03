import * as vscode from 'vscode';
import axios from 'axios';

export function activate(context: vscode.ExtensionContext) {
	console.log('AI Commit Assistant is now active!');

	let disposable = vscode.commands.registerCommand('ai-commit.generate', async () => {
		vscode.window.showInformationMessage('AI Commit: Analyzing changes...');
		
		try {
			// TODO: 获取 git diff
			// TODO: 调用 AI 服务生成 commit message
			// TODO: 显示生成的 commit message
			
			vscode.window.showInformationMessage('Commit message generated!');
		} catch (error) {
			vscode.window.showErrorMessage('Failed to generate commit message');
		}
	});

	context.subscriptions.push(disposable);
}

export function deactivate() {}
