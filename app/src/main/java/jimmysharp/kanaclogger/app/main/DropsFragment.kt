package jimmysharp.kanaclogger.app.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jimmysharp.kanaclogger.R
import jimmysharp.kanaclogger.databinding.FragmentDropsBinding

class DropsFragment : Fragment() {
    companion object {
        fun newInstance() : DropsFragment {
            return DropsFragment()
        }
    }

    lateinit var binding : FragmentDropsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_drops, container, false)

        return binding.root
    }
}