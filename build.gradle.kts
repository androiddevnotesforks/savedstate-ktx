import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
    }
}

allprojects {
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
        kotlinOptions {
            this.jvmTarget = "1.8"
            this.freeCompilerArgs += "-Xexplicit-api=strict"
        }
    }
}
