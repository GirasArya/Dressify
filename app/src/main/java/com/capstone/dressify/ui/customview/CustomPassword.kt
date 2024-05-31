package com.capstone.dressify.ui.customview

import android.content.Context

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import com.capstone.dressify.R
import com.google.android.material.textfield.TextInputEditText

class CustomPassword @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    init {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                when (inputType) {
                    InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT -> {
                        if (password.length < 8) {
                            setError(context.getString(R.string.errorPassword), null)
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                when (inputType) {
                    InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT-> {
                       if (password.length < 8) {
                            setError(context.getString(R.string.errorPassword), null)
                        }
                    }
                }
            }
        })
    }
}