plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    //ksp compiler and parcelize
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")

    //google service
    id("com.google.gms.google-services")
}

android {
    namespace = "com.capstone.dressify"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.capstone.dressify"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }


    //ViewBinding
    buildFeatures {
        viewBinding = true
        mlModelBinding = true
        externalNativeBuild {
            cmake {
                path; "CMakeLists.txt"
            }
        }
    }


    dependencies {
        //Glide
        implementation("com.github.bumptech.glide:glide:4.14.2")

        //Datastore
        implementation("androidx.datastore:datastore-preferences:1.1.1")

        //Retrofit
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")

        //Coroutines
        implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

        //Viewmodels
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")


        //Room
        implementation("androidx.room:room-runtime:2.6.1")
        implementation(libs.androidx.ui.desktop)

        implementation(libs.tensorflow.lite.support)
        implementation(libs.tensorflow.lite.metadata)
        implementation(libs.tensorflow.lite.gpu)
        implementation(libs.tensorflow.lite.task.vision)
        implementation("com.google.android.gms:play-services-tflite-gpu:16.2.0")
        implementation("org.tensorflow:tensorflow-lite-task-vision-play-services:0.4.2")
        implementation("com.google.android.gms:play-services-tflite-support:16.1.0")

        ksp("androidx.room:room-compiler:2.6.1")

        // Fragment and navigation
        implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
        implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")

        // CameraX
        val cameraxVersion = "1.3.3"
        implementation("androidx.camera:camera-camera2:$cameraxVersion")
        implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
        implementation("androidx.camera:camera-view:$cameraxVersion")


        // Viewpager and dots indicator
        implementation("me.relex:circleindicator:2.1.6")

        // Firebase
        implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
        implementation("com.google.firebase:firebase-auth")
        implementation("com.firebaseui:firebase-ui-auth:7.2.0")
        implementation("com.google.android.gms:play-services-auth:21.1.1")
        implementation("com.google.firebase:firebase-analytics")

        // Viewpager and dots indicator
        implementation("me.relex:circleindicator:2.1.6")

        //circle image view
        implementation("de.hdodenhof:circleimageview:3.1.0")


        //camera
        implementation("androidx.camera:camera-core:1.1.0")
        implementation("androidx.camera:camera-camera2:1.1.0")
        implementation("androidx.camera:camera-lifecycle:1.1.0")
        implementation("androidx.camera:camera-view:1.0.0-alpha32")
        implementation("androidx.camera:camera-extensions:1.0.0-alpha32")


        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)
        implementation(libs.androidx.leanback)
        implementation(libs.glide)
        implementation(libs.tensorflow.lite.support)
        implementation(libs.tensorflow.lite.metadata)
        implementation(libs.tensorflow.lite.gpu)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)

        implementation(libs.androidx.paging.runtime.ktx)

    }
}
dependencies {
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)
    implementation(libs.tensorflow.lite.gpu)
}
