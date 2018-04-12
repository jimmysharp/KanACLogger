package jimmysharp.kanaclogger.app.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jimmysharp.kanaclogger.app.util.ViewModelFactory
import jimmysharp.kanaclogger.databinding.FragmentConstructionsBinding

class ConstructionsFragment : Fragment() {
    companion object {
        val TAG = ConstructionsFragment::class.java.simpleName

        fun newInstance() : ConstructionsFragment {
            return ConstructionsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentConstructionsBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProviders
                .of(activity!!, ViewModelFactory.getInstance(activity!!.application))
                .get(ConstructionsViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }
}