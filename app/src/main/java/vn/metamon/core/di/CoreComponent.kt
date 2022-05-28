package vn.metamon.core.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import vn.metamon.app.DeviceInfo
import vn.metamon.app.di.AppVersion
import vn.metamon.app.di.PlatformCode
import vn.metamon.app.di.appcontext.ContextComponent
import vn.metamon.core.AppConfigStorage
import vn.metamon.core.Configs

@CoreScoped
@Component(
    modules = [CoreModule::class],
    dependencies = [ContextComponent::class],
)
interface CoreComponent {
    @RemoteHost
    fun remoteHost(): String

    @RemotePort
    fun port(): Int?

    @PlatformCode
    fun platform(): String

    @AppVersion
    fun versions(): String

    fun deviceInfo(): DeviceInfo

    fun appConfigStorage(): AppConfigStorage

    fun configsData(): Configs

    fun context(): Context

    @Component.Factory
    interface Factory {
        fun create(
            contextComponent: ContextComponent,
            @BindsInstance @RemoteHost host: String,
            @BindsInstance @RemotePort port: Int? = null,
            @BindsInstance @PlatformCode platform: String,
            @BindsInstance @AppVersion appVersion: String,
            @BindsInstance deviceInfo: DeviceInfo
        ): CoreComponent
    }
}