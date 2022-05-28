package vn.metamon.app.presentation.home.space

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import coil.load
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.item_space.*
import vn.metamon.R
import vn.metamon.data.model.Metamon
import javax.inject.Inject

class SpaceItemFragment : DaggerFragment() {
    companion object {
        private const val KEY_SPACE_DATA = "key_space_data"

        fun newInstance(space: Metamon): SpaceItemFragment {
            val fragment = SpaceItemFragment()
            val args = Bundle().apply {
                putParcelable(KEY_SPACE_DATA, space)
            }
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var appContext: Context

    private val spaceShareViewModel by viewModels<SpaceShareViewModel>(ownerProducer = { requireParentFragment() })

//    private var space: Space? = null
    private var metamon: Metamon? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        metamon = arguments?.getParcelable(KEY_SPACE_DATA)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.item_space, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setData()

        spaceView.setOnClickListener {
            metamon?.let {
                spaceShareViewModel.onSpaceClick(it)
            }
        }
    }

    private fun setData() {
        metamon?.let { space ->
            spaceName.text = space.name

            spaceCover.load(space.cover) {
                placeholder(R.color.silver)
                error(R.color.silver)
                fallback(R.color.silver)
            }
        }
    }
}