package vn.metamon.core.di

import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import vn.metamon.core.AppConfigStorage
import vn.metamon.core.Configs

@Module
class CoreModule {
    @Provides
    @CoreScoped
    fun provideAppConfigs(appConfigStorage: AppConfigStorage): Configs =
        appConfigStorage.currentConfig

    @Provides
    fun provideAppConfigsStorage(context: Context): AppConfigStorage =
        AppConfigStorage.getInstance(Pair(context, GsonBuilder().create()))

}