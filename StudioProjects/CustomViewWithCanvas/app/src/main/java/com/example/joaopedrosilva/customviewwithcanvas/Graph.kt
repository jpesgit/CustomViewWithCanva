package com.example.joaopedrosilva.customviewwithcanvas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
class Graph : View {

    //instance variables for storing xml attributes
    private var barHeight: Int = 0
    private var barWidth: Int = 0
    //objects used for drawing
    private lateinit var barColor1Paint: Paint
    private lateinit var barColor2Paint: Paint
    private lateinit var barColor3Paint: Paint
    private lateinit var barColor4Paint: Paint
    private lateinit var barColor5Paint: Paint

    @SuppressLint("ResourceType", "NewApi")
    @JvmOverloads
    @Suppress("ConvertSecondaryConstructorToPrimary", "UsePropertyAccessSyntax")
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    @Override
    private fun init(context: Context, attrs: AttributeSet?) {
        isSaveEnabled = true

        //read xml attributes
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.GraphBar, 0, 0)
        barWidth = ta.getDimensionPixelSize(R.styleable.GraphBar_barWidth, 0)
        barHeight = ta.getDimensionPixelSize(R.styleable.GraphBar_barHeight, 0)

        ta.recycle()

        barColor1Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        barColor1Paint.color = context.resources.getColor(android.R.color.holo_red_light)
        barColor2Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        barColor2Paint.color = context.resources.getColor(android.R.color.holo_orange_dark)
        barColor3Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        barColor3Paint.color = context.resources.getColor(android.R.color.holo_orange_light)
        barColor4Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        barColor4Paint.color = context.resources.getColor(android.R.color.holo_green_light)
        barColor5Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        barColor5Paint.color = context.resources.getColor(android.R.color.holo_blue_light)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(5,30)
    }

    override fun onDraw(canvas: Canvas) {
        drawBar(canvas)

    }

    private fun drawBar(canvas: Canvas) {

        val barCenterVertical = getBarCenterVertical()
        val barCenterHorizontal = getBarCenterHorizontal()

        val top = barCenterVertical - barHeight
        val bottom = barCenterVertical + barHeight
        val left = barCenterHorizontal - barWidth
        val right = barCenterHorizontal + barWidth
        val sizeBar = bottom - top

        val rect1 = RectF(left, (bottom - (sizeBar * 1)).toFloat(), right, bottom)
        val rect2 = RectF(left, (bottom - (sizeBar * 0.75)).toFloat(), right, bottom)
        val rect3 = RectF(left, (bottom - (sizeBar * 0.45)).toFloat(), right, bottom)
        val rect4 = RectF(left, (bottom - (sizeBar * 0.20)).toFloat(), right, bottom)
        val rect5 = RectF(left, (bottom - (sizeBar * 0.12)).toFloat(), right, bottom)

        canvas.drawRect(rect1,  barColor1Paint)
        canvas.drawRect(rect2,  barColor2Paint)
        canvas.drawRect(rect3,  barColor3Paint)
        canvas.drawRect(rect4,  barColor4Paint)
        canvas.drawRect(rect5,  barColor5Paint)

    }

    private fun getBarCenterVertical(): Float {
        //position the bar slightly below the middle of the drawable area
        var barCenterVertical = ((height - paddingTop - paddingBottom) / 2).toFloat()
        return barCenterVertical
    }
    private fun getBarCenterHorizontal(): Float {
        //position the bar slightly below the middle of the drawable area
        var barCenterHorizontal = ((width - paddingLeft - paddingRight) / 2).toFloat()
        return barCenterHorizontal
    }

}