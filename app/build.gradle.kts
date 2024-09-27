plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.quanlynhansu"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.quanlynhansu"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)

    // Thêm Firebase Storage và cập nhật BOM Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.0.0")) // Cập nhật BOM Firebase
    implementation("com.google.firebase:firebase-storage-ktx")  // Thêm Firebase Storage

    // Thêm Glide
    implementation(libs.glide)
    implementation(libs.litert.metadata) // Thư viện Glide
    annotationProcessor(libs.glide.compiler) // Compiler của Glide cho annotation processing

    // Thêm Picasso
    implementation(libs.picasso) // Thư viện Picasso

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
