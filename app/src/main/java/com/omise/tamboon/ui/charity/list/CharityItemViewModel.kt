package com.omise.tamboon.ui.charity.list

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import com.omise.tamboon.model.Charity
import com.omise.tamboon.ui.charity.CharityViewModel

class CharityItemViewModel(private val parentViewModel: CharityViewModel): ViewModel(){
    val id = ObservableInt()
    val name = ObservableField<String>()
    val imageUrl = ObservableField<String>()

    val clickListener = View.OnClickListener {view ->
        parentViewModel.charityIdToRedirect.value = view.tag as String
    }

    fun bind(charity: Charity){
        name.set(charity.name)
        id.set(charity.id)
        imageUrl.set(charity.logoUrl)
    }
}