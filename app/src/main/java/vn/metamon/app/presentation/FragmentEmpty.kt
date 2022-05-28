package vn.metamon.app.presentation

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.layout_menu_bar.*
import vn.metamon.app.utils.findNavHost
import vn.metamon.app.utils.navigateBack
import vn.metamon.databinding.FragmentEmptyBinding
import vn.metamon.utils.event.EventDispatcher
import vn.metamon.utils.event.Events
import vn.metamon.utils.eventlivedatabus.LiveDataBus
import vn.metamon.utils.eventlivedatabus.LiveEventCommon

class FragmentEmpty : Fragment() {
    companion object {
        val TAG = FragmentEmpty::class.java.simpleName
        const val KEY_REQUEST_VIEW_WALLET: String = "BACK_FROM_EMPTY_FRAGMENT"
        fun newInstance(): FragmentEmpty {
            return FragmentEmpty()
        }
    }


    private var _binding: FragmentEmptyBinding? = null

    private val binding get() = _binding!!

    private val emptyFragmentSharedViewModel by viewModels<EmptyFragmentSharedViewModel>(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmptyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tvTitle.text = "Back"

        backBtn.setOnClickListener {
            navigateBack()
        }
        tvTitle.setOnClickListener {
            navigateBack()
        }
    }

    override fun onDestroy() {
        val case = 2 // 2, 3, 4
        when(case) {
            4 -> {
                EventDispatcher.getInstance().post(Events.EVENT_BACK_FROM_EMPTY_FRAGMENT)
            }
            3 -> {
                LiveDataBus
                    .of(LiveEventCommon::class.java)
                    .backFromEmptyFragmentListener()
                    .postValue(Unit)
            }
            2 -> {
                findNavHost()?.navHostFragmentManager()?.setFragmentResult(
                    KEY_REQUEST_VIEW_WALLET,
                    bundleOf()
                )
            }
            1-> {
               emptyFragmentSharedViewModel.triggerEvent()// fixme: will create crash, use EventDispatcher, LivedataBus, setFragmentResultListener instead
            }
        }




        super.onDestroy()
    }
}