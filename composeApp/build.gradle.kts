import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization) // ** ADDED for Navigation **
    alias(libs.plugins.ksp) // ** ADDED for Room (Database) **
    alias(libs.plugins.androidx.room) // ** ADDED for Room (Database) **
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.serialization.json) // ** ADDED for Navigation **
            implementation(libs.androidx.navigation.compose) // ** ADDED for Navigation **
            implementation(libs.kotlinx.datetime) // ** ADDED **
            implementation(libs.androidx.room.runtime) // ** ADDED for Room (Database) **
            implementation(libs.androidx.sqlite.bundled) // ** ADDED for Room (Database) **

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
    add("kspAndroid", libs.androidx.room.compiler) // ** ADDED for Room (Database) **
    add("kspIosSimulatorArm64", libs.androidx.room.compiler) // ** ADDED for Room (Database) **
    //add("kspIosX64", libs.androidx.room.compiler) // ** ADDED for Room (Database) **
    add("kspIosArm64", libs.androidx.room.compiler) // ** ADDED for Room (Database) **

}

room {
    schemaDirectory("$projectDir/schemas") // ** ADDED for Room (Database) **
}

android {
    namespace = "edu.moravian.survey"
    compileSdk =
        libs.versions.android.compileSdk
            .get()
            .toInt()

    defaultConfig {
        applicationId = "edu.moravian.survey"
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.android.targetSdk
                .get()
                .toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
