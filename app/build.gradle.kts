plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.jawairiawaseem.i221274"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jawairiawaseem.i221274"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("de.hdodenhof:circleimageview:3.1.0")
    // ---- Firebase BoM (controls versions for all Firebase libs) ----
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))

    // Auth + Realtime DB (NO version numbers here)
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database-ktx")

    // Material (for TextInputLayout, etc.)
    implementation("com.google.android.material:material:1.12.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
}
