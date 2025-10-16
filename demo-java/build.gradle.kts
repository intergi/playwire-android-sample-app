plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.demo_java"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.demo_java"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

repositories {
    google()
    mavenCentral()
    maven {
        name = "GithubPackages"
        url = uri("https://maven.pkg.github.com/intergi/playwire-android-binaries")
        credentials {
            username = "inderdhir"
            password = "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"
        }
    }
}

dependencies {
//    implementation("com.intergi.playwire:playwiresdk_total:11.5.2")

    implementation("com.google.android.material:material:1.13.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}