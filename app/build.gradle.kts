plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.meizu.hotfix"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.meizu.hotfix"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    dataBinding {
        enable = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

tasks.register("buildPatch", Exec::class) {
    dependsOn("compileDebugKotlin")

    // 正确获取Android SDK路径
    val androidSdkDir = android.sdkDirectory
    val buildToolsVersion = "35.0.0" // 修改为你实际使用的版本

    // 构建d8工具路径
    val d8Path = File(androidSdkDir, "build-tools/$buildToolsVersion/d8").absolutePath

    // 设置输入输出路径
    val classesDir = layout.buildDirectory.dir("tmp/kotlin-classes/debug")
    val outputDir = layout.buildDirectory.dir("outputs")

    commandLine(
        d8Path,
        "--classpath", File(androidSdkDir, "platforms/android-35/android.jar").absolutePath,
        "--output", outputDir.get().asFile.absolutePath + "/patchs/",
        classesDir.get().asFile.absolutePath + "/com/meizu/hotfix/util/StrUtil.class"
    )

    doLast {
        File(outputDir.get().asFile.absolutePath + "/patchs/", "classes.dex").renameTo(File(outputDir.get().asFile.absolutePath + "/patchs/", "patch.dex"))
        File(outputDir.get().asFile.absolutePath + "/patchs/", "patch.dex").setReadOnly()
        println("补丁文件生成于：${outputDir.get().asFile.absolutePath}/patch.dex")
    }
}