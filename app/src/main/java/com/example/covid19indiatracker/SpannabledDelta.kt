package com.example.covid19indiatracker

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan


class SpannabledDelta(text: String, langColor: String,start:Int) : SpannableString(text) {

    init {
        setSpan(
                ForegroundColorSpan(Color.parseColor(langColor)),
                start,
                text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}