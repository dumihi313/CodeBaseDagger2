package vn.metamon.app.presentation

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import vn.metamon.app.presentation.FragmentEmpty.Companion.KEY_REQUEST_VIEW_WALLET
import vn.metamon.app.utils.findNavHost
import vn.metamon.app.utils.observeEvent
import vn.metamon.databinding.FragmentBBinding
import vn.metamon.utils.event.EventDispatcher
import vn.metamon.utils.event.EventListener
import vn.metamon.utils.event.Events
import vn.metamon.utils.eventlivedatabus.LiveDataBus
import vn.metamon.utils.eventlivedatabus.LiveEventCommon

class FragmentB : Fragment() {
    companion object {
        val TAG = FragmentB::class.java.simpleName

        fun newInstance(): FragmentB {
            return FragmentB()
        }
    }

    private var _binding: FragmentBBinding? = null

    private val binding get() = _binding!!

    private val emptyFragmentSharedViewModel by viewModels<EmptyFragmentSharedViewModel>(ownerProducer = { this })

    private val eventListener = EventListener { _, _ ->
        Toast.makeText(context, "Back from Empty - EventDispacher", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventDispatcher.getInstance().addListener(
            Events.EVENT_BACK_FROM_EMPTY_FRAGMENT,
            eventListener
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.addFragmentByNav.setOnClickListener {
            findNavHost()?.let { host ->
                val fragmentManager = host.navHostFragmentManager()
                fragmentManager.beginTransaction().apply {
                    fragmentManager.findFragmentById(host.containerId())
                        ?.let { hide(it) }
                    add(host.containerId(), FragmentEmpty.newInstance())
                    addToBackStack(FragmentEmpty.TAG)
                    commit()
                }
            }
        }

        initBackFromEmptyListener()

    }

    private fun initBackFromEmptyListener() {
        LiveDataBus
            .of(LiveEventCommon::class.java)
            .backFromEmptyFragmentListener()
            .observe(viewLifecycleOwner) {
                Toast.makeText(context, "Back from Empty - LiveDataBus", Toast.LENGTH_SHORT).show()
            }

        findNavHost()?.let { navHost ->
            navHost.navHostFragmentManager().setFragmentResultListener(
                KEY_REQUEST_VIEW_WALLET, this
            ) { _, _ ->
                Toast.makeText(context, "Back from Empty", Toast.LENGTH_SHORT).show()
            }
        }

        emptyFragmentSharedViewModel.event.observeEvent(viewLifecycleOwner) {
            Toast.makeText(context, "Back from Empty", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        EventDispatcher.getInstance().removeListener(
            Events.EVENT_BACK_FROM_EMPTY_FRAGMENT,
            eventListener
        )
        super.onDestroy()
    }
}