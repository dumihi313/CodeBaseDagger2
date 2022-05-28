package vn.metamon.app.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vn.metamon.app.presentation.main.MainFragment

@Module
internal abstract class MainModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeMainFragment(): MainFragment
}