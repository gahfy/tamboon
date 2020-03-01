package com.omise.tamboon.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


class CreditCardFormattingTextWatcher(private val et: EditText) : TextWatcher {
    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
    }

    override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun afterTextChanged(s: Editable) { // Remove spacing char
        et.removeTextChangedListener(this)

        var content = s.toString().replace("[^\\d.]".toRegex(), "")
        if(content.length > 12){
            content = content.substring(0, 12) + "-" + content.substring(12)
        }
        if(content.length > 8){
            content = content.substring(0, 8) + "-" + content.substring(8)
        }
        if(content.length > 4){
            content = content.substring(0, 4) + "-" + content.substring(4)
        }

        et.setText(content)
        et.setSelection(et.text.length)
        et.addTextChangedListener(this)
    }
}