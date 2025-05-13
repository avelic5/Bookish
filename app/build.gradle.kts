plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.bookish"
    compileSdk = 35 // Keep user's input. Latest stable Android SDK is typically 34.

    defaultConfig {
        applicationId = "com.example.bookish"
        minSdk = 33 // minSdk 33 je prilično visoka, ograničava broj podržanih uređaja.
        targetSdk = 35
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

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // --- Compose BOM i osnovne AndroidX zavisnosti ---
    // Koristite samo jedan Compose BOM za upravljanje verzijama Compose biblioteka.
    // Pretpostavlja se da libs.androidx.compose.bom u libs.versions.toml pokazuje na "2024.03.00" ili noviju verziju.
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom)) // BOM za instrumentirane testove

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // --- Navigacija ---
    implementation("androidx.navigation:navigation-compose:2.7.7") // Provjerite da li postoji novija stabilna verzija (npr. 2.8.x)

    // --- Coroutines ---
    // Koristite najnoviju stabilnu verziju za Kotlin Coroutines.
    // Uvijek provjerite Maven Central (search.maven.org) za apsolutno najnoviju stabilnu verziju.
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // Kao referenca, 1.7.3 je bila stabilna verzija.

    // --- Coil biblioteka za učitavanje slika (Coil3) ---
    implementation("io.coil-kt.coil3:coil-compose:3.1.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")


    // --- Zavisnosti za testiranje ---
    // JUnit 4 za unit testove
    testImplementation(libs.junit) // Trebalo bi da pokazuje na "junit:junit:4.13.2"
    testImplementation("org.hamcrest:hamcrest:2.2") // Hamcrest za asertacije
    testImplementation(kotlin("test")) // Kotlin test utilities

    // AndroidX test biblioteke za instrumentirane testove
    androidTestImplementation(libs.androidx.junit) // Trebalo bi da pokazuje na "androidx.test.ext:junit:1.1.5"
    androidTestImplementation(libs.androidx.espresso.core) // Trebalo bi da pokazuje na "androidx.test.espresso:espresso-core:3.6.1"

    // AndroidX Test Runner i Rules - Preporučuje se da su ove verzije usklađene.
    // Ako nemate ove u libs.versions.toml, ostavite ih ovde, inače koristite libs.
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test:rules:1.6.1")

    // Jetpack Compose UI Test
    // Vrlo važno: Osigurajte da je verzija kompatibilna sa vašim Compose BOM-om (2024.03.00 obično odgovara Compose 1.6.x).
    // Verzija 1.7.8 koju ste imali je verovatno previše nova za taj BOM.
    // libs.androidx.ui.test.junit4 bi trebao da pokazuje na kompatibilnu verziju (npr. 1.6.0).
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debug zavisnosti (samo za debug buildove)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}