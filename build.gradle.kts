import org.polaris2023.mcmeta.extension.McMetaSettings
import org.polaris2023.mcmeta.extension.forge.ForgeLikeDependency
import org.polaris2023.mcmeta.extension.forge.ForgeLikeToml
import org.polaris2023.mcmeta.extension.forge.neo.NeoForgeDependency
import org.polaris2023.mcmeta.extension.forge.neo.NeoForgeDependency.NeoForgeDependencyBuilder
import org.polaris2023.mcmeta.extension.forge.neo.NeoForgeMods
import org.polaris2023.mcmeta.extension.forge.neo.NeoForgeModsToml
import org.slf4j.event.Level
import java.util.Properties

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("io.github.polari-stars-mc:mcmeta-plugin:0.0.3")
    }
}


plugins {
    `java-library`
    `maven-publish`
    idea
    base
    alias(libs.plugins.mod.dev.gradle)
    id("tasks-merge")
}
val mcVersion: String by rootProject
val mcVersionRange: String by rootProject
val modGroupId: String by rootProject
val modAuthors: String by rootProject
val modLicense: String by rootProject
val neoVersion: String by rootProject
val neoVersionRange: String by rootProject
val loaderVersionRange: String by rootProject
val parchmentMappingsVersion: String by rootProject
val parchmentMinecraftVersion: String by rootProject
allprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "java-library")
    apply(plugin = "idea")
    apply(plugin = "io.github.Polari-Stars-MC.mcmeta-plugin")
    configure<McMetaSettings> {
        loaderType = McMetaSettings.Type.DEFAULT
    }
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://maven.neoforged.net/releases")
    }
    configure<ForgeLikeToml> {
        loaderVersion = loaderVersionRange
        license = modLicense
        issueTrackerURL = uri("https://github.com/Polari-Stars-MC/WildWind/issues")
    }
    java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    idea {
        module {
            isDownloadSources = true
            isDownloadJavadoc = true
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }
}

val lib = libs

subprojects {
    apply(plugin = lib.plugins.mod.dev.gradle.get().pluginId)
    configure<McMetaSettings> {
        this.loaderType = McMetaSettings.Type.NEOFORGE
    }
	
    val modId: String by project
    val modName: String by project
    val modVersion: String by project
    val modClassPrefix: String by project


    base {
        archivesName = modId
    }
    description = modName
    version = "$mcVersion-$modVersion"
    group = modGroupId

    tasks.jarJar {
        enabled = true
    }
}

