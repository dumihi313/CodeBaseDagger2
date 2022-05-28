package vn.metamon.app.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vn.metamon.app.presentation.splash.SplashScreenFragment

@Module
internal abstract class SplashModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeSplashScreenFragment(): SplashScreenFragment
}