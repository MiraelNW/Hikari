plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
    id("kotlinx-serialization")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    //immutable list
    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.core)

    //kotlin-inject
    implementation(libs.kotlin.inject.runtime)
}