import proguard.gradle.ProGuardTask

plugins {
    id("java")
}

buildscript {
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.4.2")
    }
}

group = "me.amadeusmz"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
}

tasks {
    val jarName = "${project.name}-${project.version}"

    val proguard by creating(ProGuardTask::class) {
        dependsOn(jar)
        configuration(file("proguard.pro"))


        // region libraries
        val java = System.getProperty("java.home")
        libraryjars("$java//lib/rt.jar")

        (configurations.compileClasspath.get() + configurations.runtimeClasspath.get()).forEach {
            libraryjars(it.absolutePath)
        }
        // endregion

        keep(mapOf("name" to "me.amadeusmz.RemapperTest"))

        injars("build/libs/$jarName.jar")
        outjars("build/libs/$jarName-obf.jar")
    }

    assemble {
        dependsOn(proguard)
    }
}
