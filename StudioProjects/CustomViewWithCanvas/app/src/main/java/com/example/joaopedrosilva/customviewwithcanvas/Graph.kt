package com.example.joaopedrosilva.customviewwithcanvas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View


class Graph : View {

    //instance variables for storing xml attributes
    private var barHeight: Int = 0
    private var barWidth: Int = 0
    private val listColor = listOf<Int>(
        android.R.color.holo_red_light,
        android.R.color.holo_orange_dark,
        android.R.color.holo_orange_light,
        android.R.color.holo_green_light,
        android.R.color.holo_blue_light
    )
    private val cube =
        listOf<Int>(5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100)

    data class Points(val name: String, val position: Float)

    private val listPoints =
        listOf<Points>(Points("160", 0.95f), Points("100", 0.65f), Points(" 60", 0.00f))
    //objects used for drawing

    private val labelPaint: Paint

    @SuppressLint("ResourceType", "NewApi")
    @JvmOverloads
    @Suppress("ConvertSecondaryConstructorToPrimary", "UsePropertyAccessSyntax")
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        isSaveEnabled = true

        //read xml attributes
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.GraphBar, 0, 0)
        barWidth = ta.getDimensionPixelSize(R.styleable.GraphBar_barWidth, 0)
        barHeight = ta.getDimensionPixelSize(R.styleable.GraphBar_barHeight, 0)

        labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 20f
            color = ContextCompat.getColor(context, android.R.color.white)
            textAlign = Paint.Align.LEFT
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }
        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(barWidth, barHeight)
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
        var oldsize = 0
        val cubeSize = sizeBar / cube.size
        for (x in cube) {
            val barColor1Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = when (x) {
                    in 0..10 -> context.resources.getColor(listColor[4])
                    in 11..18 -> context.resources.getColor(listColor[3])
                    in 19..46 -> context.resources.getColor(listColor[2])
                    in 47..76 -> context.resources.getColor(listColor[1])
                    in 77..100 -> context.resources.getColor(listColor[0])
                    else -> context.resources.getColor(listColor[0])
                }
            }
            val rect1 = RectF(left, bottom - oldsize - cubeSize, right, bottom - oldsize)
            canvas.drawRect(rect1, barColor1Paint)
            oldsize = (cubeSize + oldsize).toInt()
        }

        for (points in listPoints) {
            val bounds = Rect()
            labelPaint.getTextBounds(points.name, 0, points.name.length, bounds)
            val widthMin = if (bounds.width() < 32) 32 else bounds.width()
            val heightMin = if (bounds.height() < 16) 16 else bounds.height()
            val y = (bottom + paddingTop + paddingBottom) - (sizeBar * points.position) + (heightMin / 2)
            val x = (barCenterHorizontal - barWidth) - widthMin - 30

            val paint = Paint()
            val path = Path()
            paint.apply {
                style = Paint.Style.FILL
                color = Color.DKGRAY
            }
            //design arrow
            path.apply {
                moveTo(20f, 0f)
                lineTo(0f, 10f)
                lineTo(0f, -10f)
                close()
                offset(x + 35, y - 6.5f)
            }
            //design rect
            val rectangle = Rect(
                x.toInt() - 4,
                (y.toInt()) - heightMin,
                (x.toInt() + 4) + widthMin,
                y.toInt() + 4
            )
            canvas.drawPath(path, paint)
            canvas.drawRect(rectangle, paint)
            canvas.drawText(points.name, x, y, labelPaint)
        }
    }

    private fun getBarCenterVertical(): Float {
        //position the bar slightly below the middle of the drawable area
        return ((height - paddingTop - paddingBottom) / 2).toFloat()
    }

    private fun getBarCenterHorizontal(): Float {
        //position the bar slightly below the middle of the drawable area
        return ((width - paddingLeft - paddingRight) / 2).toFloat()
    }

}