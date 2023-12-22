package com.anjaslp.ailoop.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.anjaslp.ailoop.register.RegisterActivity.Companion.EMAIL_RESULT

class EditEmailText : AppCompatEditText {
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
                EMAIL_RESULT = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    setError("Email tidak valid", null)
                    false
                }else{
                    true
                }

            }

            override fun afterTextChanged(s: Editable?) {
                EMAIL_RESULT = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    setError("Email tidak valid", null)
                    false
                }else{
                    true
                }
            }
        })
    }

//    companion object {
//        var EMAIL_RESULT= false
//    }
}