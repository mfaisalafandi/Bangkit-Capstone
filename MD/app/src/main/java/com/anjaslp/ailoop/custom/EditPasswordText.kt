package com.anjaslp.ailoop.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.anjaslp.ailoop.register.RegisterActivity.Companion.PASSWORD_RESULT

class EditPasswordText : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    setError("Password tidak boleh kurang dari 8 karakter", null)
                    PASSWORD_RESULT = false
                } else {
                    error = null
                    PASSWORD_RESULT = true
                }
            }
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length < 8) {
                    setError("Password tidak boleh kurang dari 8 karakter", null)
                    PASSWORD_RESULT = false
                } else {
                    error = null
                    PASSWORD_RESULT = true
                }
            }
        })
    }

//    companion object {
//        var PASSWORD_RESULT= false
//    }
}