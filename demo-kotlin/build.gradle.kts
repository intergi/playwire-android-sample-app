import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.demo_kotlin"
    compileSdk {
        version = release(36)
    }

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.example.demo_kotlin"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}

repositories {
    google()
    mavenCentral()
    maven("https://android-sdk.is.com/")
    maven("https://artifact.bytedance.com/repository/pangle/")
    maven("https://cboost.jfrog.io/artifactory/chartboost-ads/")
    maven("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
    maven("https://repo.pubmatic.com/artifactory/public-repos/")
    maven("https://maven.ogury.co")
    maven("https://s3.amazonaws.com/smaato-sdk-releases/")
    maven("https://verve.jfrog.io/artifactory/verve-gradle-release")
}

dependencies {
    implementation("com.intergi.playwire:playwiresdk_total:13.0.0")

    implementation("com.google.android.material:material:1.13.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("com.android.support.test:runner:1.0.2")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.2")
}