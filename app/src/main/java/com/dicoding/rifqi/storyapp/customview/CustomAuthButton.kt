package com.dicoding.rifqi.storyapp.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.dicoding.rifqi.storyapp.R

class CustomAuthButton: AppCompatButton {
    private lateinit var buttonBackground: Drawable
    private lateinit var disabledButtonBackground: Drawable
    private var txtColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isEnabled) buttonBackground else disabledButtonBackground
        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER
    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, R.color.white)
        buttonBackground = ContextCompat.getDrawable(context, R.drawable.button) as Drawable
        disabledButtonBackground =
            ContextCompat.getDrawable(context, R.drawable.button_disable) as Drawable
    }
}