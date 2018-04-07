package jimmysharp.kanaclogger.app.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jimmysharp.kanaclogger.R
import jimmysharp.kanaclogger.databinding.FragmentConstructionsBinding

class ConstructionsFragment : Fragment() {
    companion object {
        fun newInstance() : ConstructionsFragment {
            return ConstructionsFragment()
        }
    }

    lateinit var binding : FragmentConstructionsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_constructions, container, false)

        return binding.root
    }
}