package vn.metamon.app.di

import androidx.lifecycle.ViewModel
import com.facebook.login.LoginFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import vn.metamon.app.presentation.login.LoginViewModel
import vn.metamon.app.presentation.utils.FacebookLoginManager
import vn.metamon.app.presentation.utils.GoogleLoginManager
import vn.metamon.app.presentation.utils.LoginManager
import javax.inject.Named

@Module
abstract class LoginModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @Named(FACEBOOK_LOGIN_MANAGER)
    abstract fun bindFacebookLoginManager(manager: FacebookLoginManager): LoginManager

    @Binds
    @Named(GOOGLE_LOGIN_MANAGER)
    abstract fun bindGoogleLoginManager(manager: GoogleLoginManager): LoginManager
}