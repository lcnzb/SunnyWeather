plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.coolweather"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.coolweather"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // 核心补充1：SQLite数据库依赖（适配文档中CoolWeatherOpenHelper）
    implementation(libs.sqlite)

    // 核心补充2：网络请求辅助（可选，文档用原生HttpURLConnection，此依赖优化兼容性）
    implementation(libs.core)

    // 核心补充3：日期时间工具（适配文档中天气发布时间格式化）
    implementation(libs.datetime)

    // 核心补充4：权限请求适配（Android 13+动态权限，文档中定位/存储权限需要）
    implementation(libs.activity.ktx) // 纯Java项目也可使用，简化权限回调

}