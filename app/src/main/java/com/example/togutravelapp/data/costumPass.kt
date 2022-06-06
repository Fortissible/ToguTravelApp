package com.example.togutravelapp.data

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.togutravelapp.R

class CustomPassword: AppCompatEditText{
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, atr: AttributeSet) : super(context, atr) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    private fun init() {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length < 6) {
                    error = context.getString(R.string.kurang_dari_8)

                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }


}