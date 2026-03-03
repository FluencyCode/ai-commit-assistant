plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("org.jetbrains.intellij") version "1.15.0"
}

group = "com.fluencycode"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}

intellij {
    version.set("2023.1")
    type.set("IC")
    plugins.set(listOf("Git4Idea", "Subversion"))
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("231")
        untilBuild.set("242.*")
        pluginDescription.set("""
            <h2>AI Commit Assistant</h2>
            <p>AI-powered commit message generator for Git and SVN</p>
            <ul>
                <li>Auto-generate commit messages from code changes</li>
                <li>Support Git and SVN</li>
                <li>Conventional Commits compliant</li>
                <li>Customizable detail level</li>
                <li>Code diff preview</li>
            </ul>
        """.trimIndent())
        changeNotes.set("""
            <h3>1.0.0</h3>
            <ul>
                <li>Initial release</li>
                <li>Git and SVN support</li>
                <li>AI-powered commit message generation</li>
            </ul>
        """.trimIndent())
    }
    
    runPluginVerifier {
        ideVersions.set(listOf("2023.1", "2023.2", "2023.3", "2024.1"))
    }
}
