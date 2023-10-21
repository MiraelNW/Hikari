import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlinx-serialization")
}

android {
    namespace = "com.miraelDev.vauma"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.miraelDev.vauma"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        compileSdkPreview = "UpsideDownCake"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val properties = Properties().apply {
            load(rootProject.file("local.properties").reader())
        }

        buildConfigField("String", "BASE_URL", "${properties["BASE_URl_KEY"]}")
        buildConfigField("String", "ANIME_LIST_URL", "${properties["ANIME_LIST_URl_KEY"]}")
        buildConfigField("String", "SEARCH_URL", "${properties["SEARCH_ANIME_URl_KEY"]}")
        buildConfigField("String", "NEW_CATEGORY_URL", "${properties["NEW_CATEGORY_URl_KEY"]}")
        buildConfigField("String", "POPULAR_CATEGORY_URL", "${properties["POPULAR_CATEGORY_URl_KEY"]}")
        buildConfigField("String", "FILMS_CATEGORY_URL", "${properties["FILMS_CATEGORY_URl_KEY"]}")
        buildConfigField("String", "NAME_CATEGORY_URL", "${properties["NAME_CATEGORY_URl_KEY"]}")
        buildConfigField("String", "AUTH_REGISTER_URL", "${properties["AUTH_REGISTER_URl_KEY"]}")
        buildConfigField("String", "AUTH_LOGIN_URL", "${properties["AUTH_LOGIN_URl_KEY"]}")
        buildConfigField("String", "AUTH_CHANGE_PASSWORD_URL", "${properties["AUTH_CHANGE_PASSWORD_URl_KEY"]}")
        buildConfigField("String", "AUTH_LOGOUT_URL", "${properties["AUTH_LOGOUT_URl_KEY"]}")
        buildConfigField("String", "AUTH_REFRESH_URL", "${properties["AUTH_REFRESH_URl_KEY"]}")

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
        freeCompilerArgs = listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + project.buildDir.absolutePath + "/compose_metrics"
        )
        freeCompilerArgs = listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + project.buildDir.absolutePath + "/compose_metrics"
        )
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0"
    }

    packaging {
        resources {
            excludes += setOf("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.8.0-alpha03")
    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.0")
    implementation("androidx.compose.ui:ui-util:1.3.1")
    implementation("androidx.compose.material:material:1.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0-beta01")

    //exoplayer

    implementation("androidx.media3:media3-exoplayer:1.0.0-beta02")
    implementation("androidx.media3:media3-ui:1.0.0-beta02")

    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")


    //images
    val landScapistVersion = "2.2.2"
    implementation("io.coil-kt:coil:2.4.0")
    implementation("io.coil-kt:coil-video:2.4.0")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("com.github.skydoves:landscapist-glide:$landScapistVersion")
    implementation("com.github.skydoves:landscapist-transformation:$landScapistVersion")
    implementation("com.github.skydoves:landscapist-animation:$landScapistVersion")
    implementation("com.github.skydoves:landscapist-palette:$landScapistVersion")

    //test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.0")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.0")

    //navigation
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("com.google.code.gson:gson:2.10")

    //retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")

    //dagger hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    //splash screen api
    implementation("androidx.core:core-splashscreen:1.0.0-beta02")

    //accompanist system ui
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.30.0")

    //accompanist pager
    implementation("com.google.accompanist:accompanist-pager:0.27.1")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.27.1")

    //ktor
    implementation("io.ktor:ktor-client-android:2.3.4")
    implementation("io.ktor:ktor-client-serialization:2.3.4")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")
    implementation("io.ktor:ktor-client-auth:2.3.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("io.ktor:ktor-client-logging-jvm:2.3.4")

    //google oauth
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    //vk auth
    implementation("com.vk:android-sdk-core:4.1.0")
    implementation("com.vk:android-sdk-api:4.1.0")

    //room 
    val room_version = "2.5.2"
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.room:room-paging:$room_version")

    //lottie animations
    val lottieVersion = "6.0.0"
    implementation("com.airbnb.android:lottie-compose:$lottieVersion")

    //paging 3
    implementation("androidx.paging:paging-runtime-ktx:3.2.0")
    implementation("androidx.paging:paging-compose:3.2.0")

    //data store
    implementation("androidx.datastore:datastore-preferences:1.0.0")
}

kapt {
    correctErrorTypes = true
}