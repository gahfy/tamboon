package com.omise.tamboon.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class CardExpireFormattingTextWatcher(private val et: EditText) : TextWatcher {
    private var previousText = ""

    override fun afterTextChanged(s: Editable) {
        et.removeTextChangedListener(this)

        val content = s.toString()
        // We're adding characters
        if(content.length > previousText.length){
            if(content.length == 2){
                val finalContent = "${content}/"
                et.setText(finalContent)
            }
            else if(content.length > 2){
                var finalContent = content.replace("[^\\d.]".toRegex(), "")
                finalContent = finalContent.substring(0, 2) + "/" + finalContent.substring(2)
                et.setText(finalContent)
            }
        }
        // We're removing characters
        else{
            if(content.length == 3){
                et.setText(content.substring(0,2))
            }
        }

        et.setSelection(et.text.length)
        et.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        previousText = s.toString()
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
}