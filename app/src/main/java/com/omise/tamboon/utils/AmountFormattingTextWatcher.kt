package com.omise.tamboon.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import java.util.*


class AmountFormattingTextWatcher(private val et: EditText) : TextWatcher {
    private val df: DecimalFormat = DecimalFormat("#,###.##", DecimalFormatSymbols(Locale.US))
    private val dfnd: DecimalFormat
    private var hasFractionalPart: Boolean
    private var fractionalPart: String = ""

    override fun afterTextChanged(s: Editable) {
        et.removeTextChangedListener(this)
        try {
            val inilen: Int = et.text.length
            val v: String = s.toString().replace(
                java.lang.String.valueOf(df.decimalFormatSymbols.groupingSeparator),
                ""
            )
            val n: Double = df.parse(v)?.toDouble()?:0.0
            val cp = et.selectionStart
            val finalText = "${dfnd.format(n)}$fractionalPart"
            et.setText(finalText)
            val endlen: Int = et.text.length
            val sel = cp + (endlen - inilen)
            if (sel > 0 && sel <= et.text.length) {
                et.setSelection(sel)
            } else { // place cursor at the end?
                et.setSelection(et.text.length - 1)
            }
        } catch (nfe: NumberFormatException) { // do nothing?
        } catch (e: ParseException) { // do nothing?
        }
        et.addTextChangedListener(this)
    }

    override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        hasFractionalPart =
            s.toString().contains(java.lang.String.valueOf(df.decimalFormatSymbols.decimalSeparator))
        fractionalPart = if(hasFractionalPart) {
            val fractionPosition = s.toString().indexOf(java.lang.String.valueOf(df.decimalFormatSymbols.decimalSeparator))
            s.toString().substring(
                fractionPosition,
                (fractionPosition + 3).coerceAtMost(s.toString().length)
            )
        }
        else
            ""
    }

    init {
        df.isDecimalSeparatorAlwaysShown = true
        dfnd = DecimalFormat("#,###", DecimalFormatSymbols(Locale.US))
        hasFractionalPart = false
    }
}