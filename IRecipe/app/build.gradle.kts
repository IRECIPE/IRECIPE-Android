import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.umcproject.irecipe"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
        viewBinding = true
        dataBinding = true
    }

    defaultConfig {
        buildConfigField("String", "NAVER_CLIENT_ID", getApiKey("NAVER_CLIENT_ID"))
        buildConfigField("String", "NAVER_CLIENT_KEY", getApiKey("NAVER_CLIENT_KEY"))
        buildConfigField("String", "KAKAO_NATIVE_KEY", getApiKey("KAKAO_NATIVE_KEY"))
        buildConfigField("String", "BASE_URL", getApiKey("BASE_URL"))
        manifestPlaceholders["KAKAO_NATIVE_KEY"] =
            getApiKey("KAKAO_NATIVE_KEY_MF")
    }

    defaultConfig {
        applicationId = "com.umcproject.eyerecipe"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

dependencies {
    
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.cardview:cardview:1.0.0")
    // Gson
    implementation ("com.google.code.gson:gson:2.8.7")
    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.6.1")
    implementation ("androidx.activity:activity-ktx:1.7.2")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation ("androidx.fragment:fragment-ktx:1.6.1")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation ("com.squareup.moshi:moshi-kotlin:1.12.0")

    // Okhttp
    implementation ("com.squareup.okhttp3:okhttp:4.9.2")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.2")

    // Lifecycle
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    // Coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // RoomDB
    implementation ("androidx.room:room-runtime:2.5.2")
    implementation ("androidx.room:room-ktx:2.5.2")
    annotationProcessor ("androidx.room:room-compiler:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.47")
    kapt("com.google.dagger:hilt-compiler:2.47")

    // kakao
    implementation("com.kakao.sdk:v2-all:2.15.0") // 전체 모듈 설치, 2.11.0 버전부터 지원

    // naver
    implementation("com.navercorp.nid:oauth:5.6.0") // jdk 11

    // Data store
    implementation ("androidx.datastore:datastore-preferences:1.0.0")

    // Circle ImageView
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //indicator
    implementation ("me.relex:circleindicator:2.1.6")

    // splash
    implementation ("androidx.core:core-splashscreen:1.1.0-alpha02")

    //Coil
    implementation("io.coil-kt:coil:2.4.0")

    //Viewpager
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
}

kapt {
    correctErrorTypes = true
}