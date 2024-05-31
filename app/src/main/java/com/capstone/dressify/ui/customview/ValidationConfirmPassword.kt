package com.capstone.dressify.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class ValidationConfirmPassword: AppCompatEditText, TextWatcher {

    private var passwordToMatch: String = ""

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        addTextChangedListener(this)
    }

    fun setPasswordToMatch(password: String) {
        this.passwordToMatch = password
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        val confirmedPassword = text.toString()
        if (confirmedPassword.length < 6) {
            setError("Password do not match", null)
        } else {
            error = null
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable?) {}

}