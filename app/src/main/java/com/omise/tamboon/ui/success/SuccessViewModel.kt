package com.omise.tamboon.ui.success

import android.view.View
import androidx.lifecycle.ViewModel
import com.omise.tamboon.utils.SingleLiveEvent

class SuccessViewModel : ViewModel(){
    val finish = SingleLiveEvent<Boolean>()

    val finishClickListener = View.OnClickListener{
        finish.value = true
    }
}