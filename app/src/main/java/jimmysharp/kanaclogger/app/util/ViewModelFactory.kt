package jimmysharp.kanaclogger.app.util

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import jimmysharp.kanaclogger.app.main.BulkRegisterViewModel
import jimmysharp.kanaclogger.app.main.ConstructionsViewModel
import jimmysharp.kanaclogger.app.main.DropsViewModel
import jimmysharp.kanaclogger.app.main.ShipListViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val application : Application) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE : ViewModelFactory? = null

        fun getInstance(application: Application) =
                INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE ?: ViewModelFactory(application)
                            .also { INSTANCE = it }
                }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(BulkRegisterViewModel::class.java) ->
                            BulkRegisterViewModel()
                    isAssignableFrom(ConstructionsViewModel::class.java) ->
                            ConstructionsViewModel()
                    isAssignableFrom(DropsViewModel::class.java) ->
                            DropsViewModel()
                    isAssignableFrom(ShipListViewModel::class.java) ->
                            ShipListViewModel()
                    else ->
                            throw IllegalArgumentException("Unknown VieModel class: ${modelClass.name}")
                }
            } as T
}