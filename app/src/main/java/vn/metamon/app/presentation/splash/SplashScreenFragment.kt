package vn.metamon.app.presentation.splash

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.postDelayed
import dagger.android.support.DaggerFragment
import vn.metamon.R
import vn.metamon.app.presentation.utils.Navigator
import vn.metamon.app.utils.findNavHost
import javax.inject.Inject

class SplashScreenFragment: DaggerFragment() {
    companion object {
        const val TAG = "SplashFragment"
        private const val DURATION = 2000L

        fun newInstance(): SplashScreenFragment {
            val fragment = SplashScreenFragment()
            fragment.arguments = bundleOf()
            return fragment
        }
    }

    @Inject
    lateinit var appContext: Context

    @Inject
    lateinit var navigator: Navigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.postDelayed(DURATION) {
            navigator.navigateToMainAndFinish(findNavHost())
        }
    }
}