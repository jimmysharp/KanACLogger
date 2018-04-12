package jimmysharp.kanaclogger.app.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jimmysharp.kanaclogger.app.util.ViewModelFactory
import jimmysharp.kanaclogger.databinding.FragmentShipListBinding

class ShipListFragment : Fragment() {
    companion object {
        val TAG = ShipListFragment::class.java.simpleName

        fun newInstance() : ShipListFragment {
            return ShipListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentShipListBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProviders
                .of(activity!!, ViewModelFactory.getInstance(activity!!.application))
                .get(ShipListViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }
}