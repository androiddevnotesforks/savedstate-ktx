import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("org.jetbrains.kotlin.android")
    id("com.android.library")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "io.woong.savedstate"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")
    api("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.5.1")
    implementation("androidx.core:core-ktx:1.9.0")

    androidTestImplementation("androidx.test:core-ktx:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.1")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation("com.google.truth:truth:1.1.3")
    androidTestImplementation("androidx.appcompat:appcompat:1.5.1")
    androidTestImplementation("androidx.activity:activity-ktx:1.6.1")
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.S01)
    signAllPublications()

    coordinates("io.woong.savedstate", "savedstate-ktx", "${project.version}")

    pom {
        name.set("savedstate-ktx")
        description.set("Kotlin extensions library for Android SavedStateHandle")
        url.set("https://github.com/cheonjaewoong/savedstate-ktx")

        licenses {
            license {
                name.set("MIT")
                url.set("https://github.com/cheonjaewoong/savedstate-ktx/blob/master/LICENSE.txt")
            }
        }

        developers {
            developer {
                id.set("cheonjaewoong")
                name.set("Jaewoong Cheon")
                email.set("cheonjaewoong@gmail.com")
            }
        }

        scm {
            url.set("https://github.com/cheonjaewoong/savedstate-ktx")
            connection.set("scm:git:git://github.com/cheonjaewoong/savedstate-ktx.git")
            developerConnection.set("scm:git:ssh://git@github.com/cheonjaewoong/savedstate-ktx.git")
        }
    }
}
