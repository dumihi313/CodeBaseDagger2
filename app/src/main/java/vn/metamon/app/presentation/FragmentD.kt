package vn.metamon.app.presentation

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import vn.metamon.R

class FragmentD : Fragment() {
    companion object {
        val TAG = FragmentD::class.java.simpleName

        fun newInstance(): FragmentD {
            return FragmentD()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_d, container, false)
    }
}