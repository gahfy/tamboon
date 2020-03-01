package com.omise.tamboon.ui.charity

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.omise.tamboon.network.TamboonApi
import com.omise.tamboon.ui.charity.list.CharityListAdapter
import com.omise.tamboon.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.lang.Exception

class CharityViewModel: ViewModel() {
    private val tamboonApi by inject(TamboonApi::class.java)
    val showLoader = ObservableBoolean()
    val showError = SingleLiveEvent<Boolean>()
    val charityIdToRedirect = SingleLiveEvent<String>()
    val adapter = CharityListAdapter(this)

    init{
        getCharities()
    }

    fun getCharities(){
        showLoader.set(true)
        showError.value = false
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val result = tamboonApi.getCharities()
                adapter.updateList(result)
            }
            catch(e: Exception){
                showError.value = true
            }
            showLoader.set(false)
        }
    }
}