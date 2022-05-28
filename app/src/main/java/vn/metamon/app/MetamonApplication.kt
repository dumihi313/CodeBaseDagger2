package vn.metamon.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.multidex.MultiDex
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.gson.GsonBuilder
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import vn.metamon.BuildConfig
import vn.metamon.app.di.DaggerAppComponent
import vn.metamon.app.di.appcontext.DaggerContextComponent
import vn.metamon.app.local.UserLocalStorage
import vn.metamon.app.utils.MetamonLifeCycleCallback
import vn.metamon.core.Environment
import vn.metamon.core.di.DaggerCoreComponent
import vn.metamon.data.di.DaggerDataComponent
import vn.metamon.utils.event.EventDispatcher
import vn.metamon.utils.event.EventListener
import vn.metamon.utils.event.Events
import javax.inject.Inject

class MetamonApplication : Application(), HasAndroidInjector {

    @Inject
    @Volatile
    @JvmField
    var androidInjector: DispatchingAndroidInjector<Any>? = null

    @Inject
    lateinit var activityLifecycleCallback: MetamonLifeCycleCallback

    private lateinit var environment: Environment

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private val eventListener = EventListener { _, _ ->
        environment.rotate()
        clearInjection()
        injectIfNecessary()

    }


    override fun onCreate() {
        super.onCreate()

        environment =
            Environment.getInstance(getSharedPreferences("metamon_config", Context.MODE_PRIVATE))

        injectIfNecessary()

        registerActivityLifecycleCallbacks(activityLifecycleCallback)

        Fresco.initialize(applicationContext)

        EventDispatcher.getInstance().addListener(Events.SWITCH_ENVIRONMENT, eventListener)

    }

    private fun applicationInjector(): AndroidInjector<MetamonApplication> {
        val contextComponent = DaggerContextComponent.factory().create(this)

        val coreComponent = DaggerCoreComponent.factory().create(
            contextComponent,
            environment.current().apiHost,
            environment.current().apiPort,
            "android",
            BuildConfig.VERSION_NAME,
            getDeviceInfo()
        )

        val gson = GsonBuilder().apply {
            //todo: add more
        }.create()

        val dataComponent = DaggerDataComponent.factory()
            .create(
                gson,
                coreComponent,
                UserLocalStorage(getSharedPreferences("user_prefs", MODE_PRIVATE))
            )
        return DaggerAppComponent.factory().create(dataComponent)
    }

    private fun injectIfNecessary() {
        if (androidInjector == null) {
            synchronized(this) {
                if (androidInjector == null) {
                    val applicationInjector = applicationInjector()
                    applicationInjector.inject(this)
                    if (androidInjector == null) {
                        throw IllegalStateException(
                            "The AndroidInjector returned from applicationInjector() did not inject the "
                                    + "DaggerApplication"
                        )
                    }
                }
            }
        }
    }

    private fun clearInjection() {
        synchronized(this) {
            androidInjector = null
        }
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceInfo(): DeviceInfo {
        val deviceId = try {
            Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            "undefined"
        }
        val deviceName = Build.MODEL
        return DeviceInfo(deviceId, deviceName)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        injectIfNecessary()

        return androidInjector as AndroidInjector<Any>
    }

}