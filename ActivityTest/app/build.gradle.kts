plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.activitytest"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.activitytest"
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

    // Lokale Unit-Tests (laufen auf dem PC)
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.2.0") // Für LiveData-Tests

    // Mockito für das Mocking von Objekten in Tests
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.11.0") // Falls Sie JUnit 5 verwenden

    // Instrumentierte Tests (laufen auf dem Gerät/Emulator)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // NEU: Fügen Sie Mockito auch für instrumentierte Tests hinzu
    androidTestImplementation("org.mockito:mockito-android:5.11.0") // Für /androidTest
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")

}