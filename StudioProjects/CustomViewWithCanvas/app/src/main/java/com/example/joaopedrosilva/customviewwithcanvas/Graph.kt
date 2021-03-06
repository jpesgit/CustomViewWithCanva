package com.example.joaopedrosilva.customviewwithcanvas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View

@Suppress("PrivatePropertyName", "LocalVariableName", "JoinDeclarationAndAssignment")
class Graph : View {

    class Labels(
        val name: String,
        val position: Float
    )

    private val textSizeMessage: Int
    private val barWidth: Int
    private val margin: Float
    private val listColor: IntArray
    private val intervals: FloatArray

    private var barHeight: Float =0f

    data class Points(val name: String, val position: Float)

    private var listPoints = arrayListOf(
        Graph.Points("160", 1f),
    Graph.Points("60", 0.5f),
    Graph.Points("20", 0.2f)
    )

    private val listLabels = listOf(
        Labels("Pico do exercício", 0.75f),
    Labels("Exercício cardio", 0.50f),
    Labels("Queima Gordura", 0.25f)
    )

    private val paint = Paint()
    private var path: Path = Path()
    private var rect = Rect()

    init {
        this.listColor = intArrayOf(
            colorFromRes(android.R.color.holo_red_light),
            colorFromRes(android.R.color.holo_orange_dark),
            colorFromRes(android.R.color.holo_orange_light),
            colorFromRes(android.R.color.holo_green_light),
            colorFromRes(android.R.color.holo_blue_light)
        )
        this.intervals = floatArrayOf(0.24f, 0.54f, 0.81f, 0.88f, 1f)
    }

    @SuppressLint("NewApi")
    @Suppress("ConvertSecondaryConstructorToPrimary")
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
    )
            : super(context, attrs, defStyleAttr, defStyleRes) {

        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.GraphBar, 0, 0)
        barWidth = ta.getDimensionPixelSize(R.styleable.GraphBar_barWidth, 10)
        margin = ta.getDimensionPixelSize(R.styleable.GraphBar_margin, 10).toFloat()
        textSizeMessage = ta.getDimensionPixelSize(R.styleable.GraphBar_textSizeMessage, 20)

        ta.recycle()
    }


    override fun onDraw(canvas: Canvas) {

        if (!isInEditMode && height < 0 || width < 0) {
            return
        }
        barHeight = height.toFloat() - (margin * 2) - paddingBottom - paddingTop
        val leftMargin= ((width -  barWidth) / 2).toFloat()
        drawBarColor(canvas, leftMargin, margin, width - leftMargin)
        drawMessages(canvas)
        drawPoints(canvas)
    }


    @SuppressLint("ResourceType")
    private fun drawPoints(canvas: Canvas) {
        val labelPaint = Paint()
        labelPaint.apply {
            isAntiAlias = true
            textSize = 20f
            color = ContextCompat.getColor(context, android.R.color.white)
            textAlign = Paint.Align.LEFT
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }
        for (points in listPoints) {
            val bounds = Rect()
            labelPaint.getTextBounds(points.name, 0, points.name.length, bounds)

            val widthMin = if (bounds.width() < 32) 32 else bounds.width()
            val heightMin = if (bounds.height() < 16) 16 else bounds.height()
            val y = (barHeight + margin) - ((barHeight * points.position) - (heightMin / 2))
            val x = ((width / 2) - margin - barWidth) - widthMin

            paint.reset()
            paint.apply {
                isAntiAlias = true
                textSize = 20f
                color = ContextCompat.getColor(context, android.R.color.darker_gray)
                textAlign = Paint.Align.LEFT
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            }
            path.reset()

            val halfTriangle = ((heightMin + 6) / 2).toFloat()

            path.apply {
                moveTo(heightMin.toFloat(), 0f)
                lineTo(0f, halfTriangle)
                lineTo(0f, -halfTriangle)
                close()
                offset(x + 35, y - ((heightMin - 2) / 2))
            }
            canvas.drawPath(path, paint)

            rect = Rect(
                x.toInt() - 4,
                (y.toInt() - 2) - heightMin,
                (x.toInt() + 4) + widthMin,
                y.toInt() + 4
            )

            canvas.drawRect(rect, paint)
            canvas.drawText(points.name, x, y, labelPaint)

        }
    }

    private fun drawMessages(canvas: Canvas) {
        for (label in listLabels) {
            paint.reset()
            paint.apply {
                textSize = textSizeMessage.toFloat()
                isAntiAlias = true
                color = ContextCompat.getColor(context, android.R.color.darker_gray)
                textAlign = Paint.Align.LEFT
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            }
            val bounds = Rect()
            paint.getTextBounds(label.name, 0, label.name.length, bounds)


            val y = margin + (barHeight * label.position)
            val x = ((width / 2) + barWidth + 30).toFloat()

            canvas.drawText(label.name, x, y, paint)
        }
    }

    private fun drawBarColor(canvas: Canvas, x0:Float,y0:Float, x1:Float) {

        var y0 = y0

        this.paint.apply {
            reset()
            isAntiAlias = true
        }

        listColor.forEachIndexed { index, color ->
            val y1 = (intervals[index] * barHeight) + margin

            paint.apply {
                this.color = color
            }

            canvas.drawRect(x0, y0, x1, y1, paint)
            y0 = y1
        }
    }

    fun setPoints(list: ArrayList<Points>) {
        this.listPoints = list
        invalidate()
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun View.colorFromRes(color: Int): Int {
        return ContextCompat.getColor(context, color)
    }
}