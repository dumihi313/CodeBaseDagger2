package vn.metamon.data.di

import android.content.Context
import com.google.gson.Gson
import dagger.BindsInstance
import dagger.Component
import vn.metamon.core.di.RemoteHost
import vn.metamon.app.DeviceInfo
import vn.metamon.app.di.AppVersion
import vn.metamon.app.di.PlatformCode
import vn.metamon.core.Configs
import vn.metamon.core.di.CoreComponent
import vn.metamon.core.di.RemotePort
import vn.metamon.data.MetamonUserManager
import vn.metamon.data.local.UserStorage
import vn.metamon.data.model.ConnectionManager
import vn.metamon.data.repository.MetamonRepository
import vn.metamon.data.repository.PassportRepository
import vn.metamon.data.repository.SpaceRepository

@Component(
    modules = [
        DataModule::class,
    ],
    dependencies = [CoreComponent::class]
)
@DataScoped
interface DataComponent {
    fun context(): Context

    fun spaceRepository(): SpaceRepository

    fun connectionManager(): ConnectionManager

    fun userManager(): MetamonUserManager

    @RemoteHost
    fun remoteHost(): String

    @RemotePort
    fun port(): Int?

    @PlatformCode
    fun platform(): String

    @AppVersion
    fun versions(): String

    fun deviceInfo(): DeviceInfo

    fun configsData(): Configs

    fun passportRepository(): PassportRepository

    fun metamonRepository(): MetamonRepository

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance gson: Gson,
            coreComponent: CoreComponent,
            @BindsInstance userStorage: UserStorage
        ): DataComponent
    }
}