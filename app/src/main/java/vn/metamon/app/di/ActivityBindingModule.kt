package vn.metamon.app.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vn.metamon.app.presentation.MainActivity

@Module
abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            LoginModule::class,
            SplashModule::class,
            MainModule::class,
            HomeModule::class
        ]
    )
    internal abstract fun contributeMainActivity(): MainActivity
}