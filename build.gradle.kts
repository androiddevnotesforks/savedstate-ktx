import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.23.1")
    }
}

allprojects {
    group = "io.woong.savedstate"
    version = "1.0.0"

    repositories {
        google()
        mavenCentral()
    }
}

subprojects {
    tasks.withType<Test> {
        useJUnit()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
        kotlinOptions.freeCompilerArgs += "-Xexplicit-api=strict"
    }
}
