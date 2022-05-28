import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.google.protobuf.gradle.*

plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("com.google.protobuf")
}

android {
    compileSdk = Configs.COMPILE_SDK

    defaultConfig {
        applicationId = Configs.APPLICATION_ID
        minSdk = Configs.MIN_SDK
        targetSdk = Configs.TARGET_SDK
        versionCode = Configs.VERSION_CODE
        versionName = Configs.VERSION_NAME

        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
        buildConfigField("String", "PROD_API_HOST", "\"${Configs.PROD_API_HOST}\"")
        buildConfigField("int", "PROD_API_PORT", "${Configs.PROD_API_PORT}")
        buildConfigField("String", "DEV_API_HOST", "\"${Configs.DEV_API_HOST}\"")
        buildConfigField("int", "DEV_API_PORT", "${Configs.DEV_API_PORT}")
        buildConfigField("String", "COVER_HOST", "\"${Configs.COVER_HOST}\"")
        buildConfigField("int", "COVER_PORT", "${Configs.COVER_PORT}")
        buildConfigField("String", "DEFAULT_STREAM_HOST", "\"${Configs.DEFAULT_STREAM_HOST}\"")
        buildConfigField("int", "DEFAULT_STREAM_PORT", "${Configs.DEFAULT_STREAM_PORT}")
        buildConfigField("String", "DEFAULT_URL", "\"${Configs.DEFAULT_URL}\"")
        buildConfigField("String", "MARKET_URI", "\"${Configs.MARKET_URI}\"")
        buildConfigField("String", "PLAY_STORE_URL", "\"${Configs.STORE_URL}\"")
        buildConfigField("String", "APP_LINK_SCHEME", "\"${Configs.APP_LINK_SCHEME}\"")
        buildConfigField("String", "APP_LINK_HOST", "\"${Configs.APP_LINK_HOST}\"")
        buildConfigField("String", "APP_LINK_WATCH_LIVE_PATH_PREFIX", "\"${Configs.APP_LINK_WATCH_LIVE_PATH_PREFIX}\"")

    }
    signingConfigs {
        getByName("debug") {
//            val fis = FileInputStream(rootProject.file("publish/keystoreDev.properties"))
//            val properties = Properties()
//            properties.load(fis)
//
//            storeFile = file(properties.getProperty("KEY_STORE"))
//            storePassword = properties.getProperty("KEY_STORE_PASSWORD")
//            keyAlias = properties.getProperty("KEY_STORE_ALIAS")
//            keyPassword = properties.getProperty("KEY_STORE_ALIAS_PASSWORD")
        }

        create("release") {
//            var configFile = rootProject.file("publish/keystorePro.properties")
//            if (!configFile.exists()) {
//                configFile = project.rootProject.file("publish/keystoreDev.properties")
//            }
//
//            val fis = FileInputStream(configFile)
//            val properties = Properties()
//            properties.load(fis)
//
//            storeFile = file(properties.getProperty("KEY_STORE"))
//            storePassword = properties.getProperty("KEY_STORE_PASSWORD")
//            keyAlias = properties.getProperty("KEY_STORE_ALIAS")
//            keyPassword = properties.getProperty("KEY_STORE_ALIAS_PASSWORD")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            val apkName =
                "Metamon_v${Configs.VERSION_NAME}_${Configs.VERSION_CODE}_${Configs.BUILD_NUMBER}"
            android.applicationVariants.all {
                outputs.all {
                    if (this is BaseVariantOutputImpl && name == "release") {
                        outputFileName = "$apkName.apk"
                        if (isMinifyEnabled) {
                            assembleProvider?.get()?.doLast {
                                val mappingFiles = mappingFileProvider.get().files
                                for (file in mappingFiles) {
                                    if (file != null && file.exists()) {
                                        file.renameTo(File(file.parentFile, "$apkName-mapping.txt"))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }


    lint {
        // Eliminates UnusedResources false positives for resources used in DataBinding layouts
        isCheckGeneratedSources = true
        // Running lint over the debug variant is enough
        isCheckReleaseBuilds = false
        // See lint.xml for rules configuration
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    // To avoid the compile error: "Cannot inline bytecode built with JVM target 1.8
    // into bytecode that is being built with JVM target 1.6"
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }
    packagingOptions {
        resources.excludes.add("META-INF/*")
    }
    sourceSets {
//        getByName("main") {
//            java.srcDir("src/main/java")
//            java.srcDir("src/main/kotlin")
//            res.srcDir("src/main/res")
//            manifest.srcFile("src/main/AndroidManifest.xml")
//            withGroovyBuilder {
//                "proto" {
//                    "srcDir" ("src/main/proto")
//                    "exclude" ("rest_api.proto")
//                }
//            }
//        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE_VERSION
    }
}

dependencies {
    api(platform(project(":depconstraints")))
    kapt(platform(project(":depconstraints")))
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Java
    implementation(Libs.JAVA_ANNOTATION)

    // Kotlin
    implementation(Libs.KOTLIN_STDLIB)
    implementation(Libs.COROUTINES)
//    implementation(Libs.COROUTINES)
//    'com.jakewharton.retrofit:retrofit2-kotlin-coroutines-experimental-adapter:1.0.0'

    // Android
    implementation(Libs.ANNOTATION)
    implementation(Libs.APPCOMPAT)
    implementation(Libs.BILLING)
    implementation(Libs.BILLING_KTX)
    implementation(Libs.CONSTRAINT_LAYOUT)
    implementation(Libs.CORE_KTX)
    implementation(Libs.MATERIAL)
    implementation(Libs.MULTI_DEX)
    implementation(Libs.NAVIGATION_FRAGMENT)
    implementation(Libs.NAVIGATION_UI)
    implementation(Libs.VIEW_PAGER_2)
    implementation(Libs.WORK_MANAGER)
    implementation(Libs.IMAGE_CROPPER)

    // 3rd-party
    // Dagger
    implementation(Libs.DAGGER)
    implementation(Libs.DAGGER_ANDROID)
    kapt(Libs.DAGGER_COMPILER)
    kapt(Libs.DAGGER_PROCESSOR)

    // Facebook sdk
    implementation(Libs.FACEBOOK_SDK)

    // Fabric and Firebase
    implementation(Libs.FIREBASE_ANALYTICS)
    implementation(Libs.FIREBASE_CRASHLYTICS)
    implementation(Libs.FIREBASE_MESSAGING)

    // Fresco
    implementation(Libs.FRESCO)
    implementation(Libs.FRESCO_ANIMATED_DRAWABLE)
    implementation(Libs.FRESCO_ANIMATED_WEBP)
    implementation(Libs.FRESCO_WEBP_SUPPORT)

    // Glide
    implementation(Libs.GLIDE)
    kapt(Libs.GLIDE_COMPILER)

    // gPRC
    implementation(Libs.GRPC_PROTOBUF_LITE)
    implementation(Libs.GRPC_OKHTTP)
    implementation(Libs.GRPC_STUB)
    implementation(Libs.GRPC_KOTLIN_STUB) {
        exclude("io.grpc", "grpc-protobuf")
    }
    // Gson
    implementation(Libs.GSON)

//    debugImplementation(Libs.LEAK_CANARY)

    implementation(Libs.PLAY_AUTH)

    implementation(Libs.COIL)

    //Compose
    // Integration with activities
    implementation(Libs.ACTIVITY_COMPOSE)
    // Compose Material Design
    implementation(Libs.MATERIAL_COMPOSE)
    // Animations
    implementation(Libs.ANIMATION_COMPOSE)
    // Tooling support (Previews, etc.)
    implementation(Libs.COMPOSE_UI_TOOL_SUPPORT)
    // Integration with ViewModels
    implementation(Libs.VIEWMODEL_COMPOSE)
    // When using a MDC theme
    implementation(Libs.COMPOSE_THEME_ADAPTER)
    // When using a AppCompat theme
    implementation(Libs.COMPOSE_APPCOMPAT_THEME)
    implementation(Libs.COMPOSE_RUNTIME)
    implementation(Libs.COMPOSE_COIL)
    implementation(Libs.COMPOSE_SWIPE_TO_REFRESH)
    //Compose Pager
    implementation(Libs.COMPOSE_PAGER) // Pager
    implementation(Libs.COMPOSE_PAGER_INDICATOR) // Pager Indicators

    implementation(Libs.COMPOSE_CONSTRAINTLAYOUT)
    implementation(Libs.GUAVA)

    // Instrumentation tests
    androidTestImplementation(Libs.ESPRESSO_CORE)
    androidTestImplementation(Libs.ANDROID_RUNNER)

    // Local unit tests
    testImplementation(Libs.JUNIT)

    // Compose UI Tests
    implementation(Libs.COMPOSE_JUNIT)
}

//protobuf {
//}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${Versions.PROTO_C}"
    }
    plugins {
        id("lite") {
            artifact = "com.google.protobuf:protoc-gen-javalite:${Versions.PROTO_C}"
        }
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${Versions.GRPC}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${Versions.GRPC_KOTLIN}"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.builtins {
                id("java") {
                    option("lite")
                }
            }
            it.plugins {
                id("grpc") {
                    option("lite")
                }
                id("grpckt") {
                    option("lite")
                }
            }
        }
    }
}
