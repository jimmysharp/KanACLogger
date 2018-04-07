package jimmysharp.kanaclogger.app.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jimmysharp.kanaclogger.R
import jimmysharp.kanaclogger.databinding.FragmentBulkRegisterBinding

class BulkRegisterFragment : Fragment() {
    companion object {
        fun newInstance() : BulkRegisterFragment {
            return BulkRegisterFragment()
        }
    }
    lateinit var binding : FragmentBulkRegisterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bulk_register, container, false)

        return binding.root
    }
}