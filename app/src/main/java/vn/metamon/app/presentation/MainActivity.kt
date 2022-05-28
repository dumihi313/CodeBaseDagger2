package vn.metamon.app.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import dagger.android.support.DaggerAppCompatActivity
import vn.metamon.app.di.model.BackPressedListener
import vn.metamon.R
import vn.metamon.app.presentation.splash.SplashScreenFragment
import vn.metamon.app.presentation.utils.Navigator
import vn.metamon.data.MetamonUserManager
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), NavHost {
    @Inject
    lateinit var appContext: Context

//    @Inject
//    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userManager: MetamonUserManager

    @Inject
    lateinit var navigator: Navigator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        Toast.makeText(appContext, "Hieu", Toast.LENGTH_SHORT).show()

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainNavContainer, SplashScreenFragment.newInstance(), SplashScreenFragment.TAG)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        val fragments = supportFragmentManager.fragments
        for (f in fragments) {
            if (f.isVisible && f is BackPressedListener && f.onBackPressed()) {
                return
            }
        }
        super.onBackPressed()
    }

    override fun containerId(): Int {
        return R.id.mainNavContainer
    }

    override fun navHostFragmentManager(): FragmentManager {
        return supportFragmentManager
    }

    override fun containerView(): View {
        return findViewById(containerId())
    }
}