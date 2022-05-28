plugins {
    id("java-platform")
}


val coroutines = "1.3.4"

val javaAnnotation = "1.3.2"
val annotation = "1.1.0"
val appcompat = "1.1.0"
val billing = "3.0.0"
val constraintLayout = "2.0.4"
val core = "1.6.0"
val crashlytics = "2.9.8"
val dagger = "2.34"
val espresso = "3.1.1"
val facebook = "[9,10)"
val firebaseAnalytics = "17.4.4"
val firebaseCrashlytics = "17.1.1"
val firebaseMessaging = "21.0.0"
val fresco = "1.8.1"
val glide = "4.11.0"
val guava = "30.0-jre"
val gson = "2.8.6"
val junit = "4.13"
val leakCanary = "2.4"
val material = "1.2.1"
val mockito = "3.3.1"
val mockitoKotlin = "1.5.0"
val multidex = "2.0.1"
val navigation = "2.3.0"
val playAuth = "18.1.0"
val runner = "1.2.0"
val viewpager2 = "1.0.0"
val workManager = "2.4.0"
val cropperImage = "2.8.+"
val coil = "1.1.1"
val retrofit = "2.9.0"
val retrofitRxAdapter = "2.1.0"
val retrofitGsonConverter = "2.3.0"
val activityCompose = "1.3.1"
val materialCompose = "1.0.1"
val animationCompose = "1.0.1"
val composeUITool = "1.0.1"
val viewModelCompose = "1.0.0-alpha07"
val composeThemeAdapter = "1.0.1"
val composeAppCompatTheme = "0.16.0"
val composeRuntime = "1.0.1"
val composeCoil = "1.3.2"
val composeSwipeToRefresh = "0.17.0"
val composePager = "0.18.0"
val composePagerIndicators = "0.18.0"
val composeConstraintLayout = "1.0.0-rc01"
val composeJUnit = "1.0.1"

dependencies {
    constraints {
        // Kotlin
        api("${Libs.KOTLIN_STDLIB}:${Versions.KOTLIN}")
        api("${Libs.COROUTINES}:$coroutines")

        // Java
        api("${Libs.JAVA_ANNOTATION}:$javaAnnotation")

        // Android
        api("${Libs.ANNOTATION}:$annotation")
        api("${Libs.APPCOMPAT}:$appcompat")
        api("${Libs.BILLING}:$billing")
        api("${Libs.BILLING_KTX}:$billing")
        api("${Libs.CONSTRAINT_LAYOUT}:$constraintLayout")
        api("${Libs.CORE_KTX}:$core")
        api("${Libs.MATERIAL}:$material")
        api("${Libs.MULTI_DEX}:$multidex")
        api("${Libs.NAVIGATION_FRAGMENT}:$navigation")
        api("${Libs.NAVIGATION_UI}:$navigation")
        api("${Libs.VIEW_PAGER_2}:$viewpager2")
        api("${Libs.WORK_MANAGER}:$workManager")
        api("${Libs.IMAGE_CROPPER}:$cropperImage")


        // 3rd-party
        api("${Libs.DAGGER}:$dagger")
        api("${Libs.DAGGER_ANDROID}:$dagger")
        api("${Libs.DAGGER_COMPILER}:$dagger")
        api("${Libs.DAGGER_PROCESSOR}:$dagger")

        api("${Libs.FACEBOOK_SDK}:$facebook")

        api("${Libs.FIREBASE_ANALYTICS}:$firebaseAnalytics")
        api("${Libs.FIREBASE_CRASHLYTICS}:$firebaseCrashlytics")
        api("${Libs.FIREBASE_MESSAGING}:$firebaseMessaging")

        api("${Libs.FRESCO}:$fresco")
        api("${Libs.FRESCO_ANIMATED_DRAWABLE}:$fresco")
        api("${Libs.FRESCO_ANIMATED_WEBP}:$fresco")
        api("${Libs.FRESCO_WEBP_SUPPORT}:$fresco")

        api("${Libs.GRPC_PROTOBUF_LITE}:${Versions.GRPC}")
        api("${Libs.GRPC_OKHTTP}:${Versions.GRPC}")
        api("${Libs.GRPC_STUB}:${Versions.GRPC}")
        api("${Libs.GRPC_KOTLIN_STUB}:${Versions.GRPC_KOTLIN}")

        api("${Libs.GLIDE}:$glide")
        api("${Libs.GLIDE_COMPILER}:$glide")

        api("${Libs.GSON}:$gson")

        api("${Libs.LEAK_CANARY}:$leakCanary")

        api("${Libs.PLAY_AUTH}:$playAuth")

        api("${Libs.COIL}:$coil")

        api("${Libs.ACTIVITY_COMPOSE}:$activityCompose")
        api("${Libs.MATERIAL_COMPOSE}:$materialCompose")
        api("${Libs.ANIMATION_COMPOSE}:$animationCompose")
        api("${Libs.COMPOSE_UI_TOOL_SUPPORT}:$composeUITool")
        api("${Libs.VIEWMODEL_COMPOSE}:$viewModelCompose")
        api("${Libs.COMPOSE_THEME_ADAPTER}:$composeThemeAdapter")
        api("${Libs.COMPOSE_APPCOMPAT_THEME}:$composeAppCompatTheme")
        api("${Libs.COMPOSE_RUNTIME}:$composeRuntime")
        api("${Libs.COMPOSE_COIL}:$composeCoil")
        api("${Libs.COMPOSE_SWIPE_TO_REFRESH}:$composeSwipeToRefresh")
        api("${Libs.COMPOSE_PAGER}:$composePager")
        api("${Libs.COMPOSE_PAGER_INDICATOR}:$composePagerIndicators")

        api("${Libs.COMPOSE_CONSTRAINTLAYOUT}:$composeConstraintLayout")
        api("${Libs.GUAVA}:$guava")

        // Test
        api("${Libs.ESPRESSO_CORE}:$espresso")
        api("${Libs.JUNIT}:$junit")
        api("${Libs.ANDROID_RUNNER}:$runner")
        api("${Libs.COMPOSE_JUNIT}:$composeJUnit")

    }
}