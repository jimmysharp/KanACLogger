package jimmysharp.kanaclogger.app.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jimmysharp.kanaclogger.app.util.ViewModelFactory
import jimmysharp.kanaclogger.databinding.FragmentBulkRegisterBinding

class BulkRegisterFragment : Fragment() {
    companion object {
        val TAG = BulkRegisterFragment::class.java.simpleName

        fun newInstance() : BulkRegisterFragment {
            return BulkRegisterFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentBulkRegisterBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProviders
                .of(activity!!, ViewModelFactory.getInstance(activity!!.application))
                .get(BulkRegisterViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }
}