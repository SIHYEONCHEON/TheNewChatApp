plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.protobuf") version "0.9.4"
}

android {
    namespace = "com.example.thenewchatapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.thenewchatapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

    buildFeatures {
        compose = true
        viewBinding = true
    }

    sourceSets {
        getByName("main") {
            kotlin.srcDirs("src/main/kotlin", "src/main/proto")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/INDEX.LIST"
            excludes += "/META-INF/DEPENDENCIES"
            excludes += "mozilla/public-suffix-list.txt" // ✅ 충돌 방지
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.2"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.59.0"
        }
        create("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.3.0:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") // ✅ Full 버전만 사용
            }
            task.plugins {
                create("grpc") // ✅ Full 버전 사용
                create("grpckt") // ✅ Full 버전 사용
            }
        }
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
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.media3.common.ktx)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation("com.squareup.okhttp3:okhttp:4.12.0")

// ✅ Google Cloud Speech-to-Text (protobuf-javalite 기반 사용)
    implementation("com.google.cloud:google-cloud-speech:1.24.0") {
        exclude(group = "com.google.protobuf", module = "protobuf-javalite")
    }

    // ✅ gRPC 및 Protobuf
    implementation("io.grpc:grpc-api:1.58.0")
    implementation("io.grpc:grpc-stub:1.58.0")
    implementation("io.grpc:grpc-okhttp:1.58.0")
    implementation("io.grpc:grpc-auth:1.58.0") {
        exclude(group = "org.apache.httpcomponents", module = "httpclient") // ✅ httpclient 제거
    }
    implementation("io.grpc:grpc-protobuf:1.58.0")

    // ✅ gRPC-Kotlin 추가 (gRPC 코루틴 지원)
    implementation("io.grpc:grpc-kotlin-stub:1.3.0")

    implementation("com.google.protobuf:protobuf-java:3.24.0")

    // ✅ Google Auth 최신 버전 (httpclient 완전 제거)
    implementation("com.google.auth:google-auth-library-oauth2-http:1.20.0") {
        exclude(group = "commons-logging", module = "commons-logging")
        exclude(group = "org.apache.httpcomponents", module = "httpclient")
    }

    // ✅ Kotlin 및 Coroutine 관련
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // ✅ 테스트 및 디버깅
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.activity:activity:1.8.2")
    implementation("com.google.android.material:material:1.11.0")

    // Fragment
    implementation("androidx.fragment:fragment-ktx:1.7.0")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // ConstraintLayout (필요 시 추가)
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}