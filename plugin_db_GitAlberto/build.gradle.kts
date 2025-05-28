plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    id("org.jetbrains.intellij.platform") version "2.3.0"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    implementation("net.bytebuddy:byte-buddy:1.14.11")
    implementation("net.bytebuddy:byte-buddy-agent:1.14.11")
    implementation("org.freemarker:freemarker:2.3.32")
    intellijPlatform {
        create("IC", "2024.2.5")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
        implementation("mysql:mysql-connector-java:8.0.33")
        implementation("org.postgresql:postgresql:42.7.1")
        /*implementation("com.microsoft.sqlserver:mssql-jdbc:12.4.2.jre11")
        implementation("org.mongodb:mongodb-driver-sync:4.11.1")
        implementation("org.xerial:sqlite-jdbc:3.44.1.0")*/
        implementation("org.postgresql:postgresql:42.2.27")


        // Add necessary plugin dependencies for compilation here, example:
        // bundledPlugin("com.intellij.java")
    }
}



intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "242"
        }

        changeNotes = """
      Initial version
    """.trimIndent()
    }
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "21"
    }
}
