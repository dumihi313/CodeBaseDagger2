package vn.metamon.app.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import vn.metamon.app.presentation.home.HomeFragment
import vn.metamon.app.presentation.home.following.FollowingFragment
import vn.metamon.app.presentation.home.space.MetamonViewModel
import vn.metamon.app.presentation.home.space.SpaceFragment
import vn.metamon.app.presentation.home.space.SpaceItemFragment
import vn.metamon.app.presentation.home.space.SpaceViewModel
import vn.metamon.app.presentation.login.LoginDialog

@Module
internal abstract class HomeModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeSpaceFragment(): SpaceFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeFollowingFragment(): FollowingFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeSpaceItemFragment(): SpaceItemFragment

    @Binds
    @IntoMap
    @ViewModelKey(SpaceViewModel::class)
    internal abstract fun bindFollowingViewModel(viewModel: SpaceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MetamonViewModel::class)
    internal abstract fun bindMetamonViewModel(viewModel: MetamonViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeLoginDialog(): LoginDialog
}