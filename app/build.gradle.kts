import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}


android {
    namespace = "com.florientmanfo.battlesketch"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.florientmanfo.battlesketch"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }


    buildTypes {
        val localProperties = Properties()
        localProperties.load(FileInputStream(rootProject.file("local.properties")))
        buildTypes.forEach {
            it.buildConfigField("String", "HTTP_BASE_URL", localProperties.getProperty("http", ""))
            it.buildConfigField("String", "WS_BASE_URL", localProperties.getProperty("ws", ""))
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.adaptive.android)
    implementation(libs.androidx.compose.material3.windows.size)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.graphics.shapes)
    //implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.3.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Koin dependencies
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Ktor
    implementation(libs.ktor.client.core)
    implementation (libs.ktor.client.okhttp)
    implementation (libs.ktor.client.websocket)
    implementation (libs.ktor.client.content.negotiation)
    implementation (libs.ktor.serialization.kotlinx.json)

    // Compose lifecycle viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Compose navigation
    implementation(libs.navigation.compose)
}