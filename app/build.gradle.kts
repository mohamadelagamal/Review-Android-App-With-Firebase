import java.io.FileInputStream
import java.util.Properties

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.firebaseserviceandroidapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.firebaseserviceandroidapp"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        // The debug build type is added by default

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    flavorDimensions += "default"

    productFlavors {
        create("development") {
            dimension = "default"
            applicationIdSuffix = ".development"
            resValue("string", "app_name", "Elgamal Development App")
            resValue("mipmap", "ic_launcher", "@mipmap/ic_launcher_dev")
        }
        create("production") {
            dimension = "default"
            applicationIdSuffix = ".production"
            resValue("string", "app_name", "Elgamal Production App")
            resValue("mipmap", "ic_launcher", "@mipmap/ic_launcher_prod")
        }
    }
    bundle {
        language {
            enableSplit =
                false // enable split APKs for languages in the resource table (e.g. ar, en)
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.annotation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // ViewModel utilities for Compose
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.facebook.shimmer)

    implementation(libs.overscroll.decor.android)
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.android.compiler)
    // room
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    // swipe refresh layout
    implementation(libs.swiperefreshlayout)
    implementation(libs.material)
    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseAnalytics)
    implementation(libs.firebaseAuth)
    implementation(libs.firebaseFirestore)
    implementation(libs.dragDropSwipeRecyclerview)
    implementation(libs.materialCalendarView)
    implementation(libs.glide)
    implementation(libs.threetenabp)
    implementation(libs.core)
    implementation(libs.lottie)
    implementation(libs.firebaseStorage)
    implementation(libs.playServicesAuth)
    implementation(libs.gson)

}
kapt {
    correctErrorTypes = true
    arguments {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}


tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:deprecation")
}
apply(plugin = "com.google.gms.google-services")