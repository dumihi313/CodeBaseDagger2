package vn.metamon.app.di

import android.content.Context
import android.content.SharedPreferences
import coil.ImageLoader
import coil.imageLoader
import com.facebook.CallbackManager
import dagger.Module
import dagger.Provides
import vn.metamon.app.local.UserLocalStorage
import vn.metamon.app.presentation.utils.LoginManager
import vn.metamon.app.presentation.utils.SocialLoginFactory
import vn.metamon.data.di.UserPrefs
import vn.metamon.data.local.UserStorage
import vn.metamon.utils.NetworkStateManager

@Module
class AppModule {

    @Provides
    @AppScoped
    fun provideImageLoader(context: Context): ImageLoader = context.imageLoader

    @Provides
    fun provideCallbackManager(): CallbackManager = CallbackManager.Factory.create()

    @Provides
    @UserPrefs
    fun providesUserPref(context: Context): SharedPreferences {
        return context.applicationContext.getSharedPreferences(
            "user_prefs",
            Context.MODE_PRIVATE
        )
    }

    @Provides
    fun provideUserStorage(@UserPrefs pref: SharedPreferences): UserStorage =
        UserLocalStorage(pref)

    @AppScoped
    @Provides
    @AudioDir
    fun providesAudioDir(context: Context): String =
        context.filesDir.absolutePath + "/" + "audio"

    @Provides
    @FilterConfigsStorage
    fun providesFilterConfigsStorage(context: Context): SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE) // same providesUserPref

    @Provides
    fun provideSocialLoginManagerFactory(
        context: Context,
        networkStateManager: NetworkStateManager
    ): LoginManager.Factory {
        return SocialLoginFactory(context, networkStateManager)
    }
}