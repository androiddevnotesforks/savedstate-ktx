import java.util.Properties

plugins {
    id("org.jetbrains.kotlin.android")
    id("com.android.library")
    id("maven-publish")
    id("signing")
}

android {
    namespace = "${project.group}"

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

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.5.1")

    androidTestImplementation("androidx.test:core-ktx:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.1")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation("com.google.truth:truth:1.1.3")
    androidTestImplementation("androidx.appcompat:appcompat:1.5.1")
    androidTestImplementation("androidx.activity:activity-ktx:1.6.1")
}

publishing {
    repositories {
        maven {
            name = "sonatype"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = localProperty("OSSRH_USERNAME")
                password = localProperty("OSSRH_PASSWORD")
            }
        }
    }

    publications {
        create<MavenPublication>("release") {
            afterEvaluate {
                from(components.getByName("release"))
            }

            groupId = "${project.group}"
            artifactId = "savedstate-ktx"
            version = "${project.version}"

            pom {
                name.set("savedstate-ktx")
                description.set("Kotlin extensions for Android SavedStateHandle")
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
    }
}

signing {
    sign(publishing.publications)
}

fun localProperty(name: String): String {
    val localPropertiesFile = project.rootProject.file("local.properties")
    if (!localPropertiesFile.exists()) {
        throw IllegalStateException("Cannot find 'local.properties' file")
    }
    val properties = localPropertiesFile.reader().use { reader ->
        Properties().apply { load(reader) }
    }
    val property = properties.getProperty(name, null)
    return property ?: throw IllegalArgumentException("Cannot find $name in local.properties")
}
