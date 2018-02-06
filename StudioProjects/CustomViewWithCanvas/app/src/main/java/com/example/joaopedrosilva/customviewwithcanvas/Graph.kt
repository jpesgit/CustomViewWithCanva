package com.example.joaopedrosilva.customviewwithcanvas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View

@Suppress("PrivatePropertyName", "LocalVariableName", "JoinDeclarationAndAssignment")
class Graph : View {
    private var barHeight: Float = 0f
    private var textSizeMessage: Int = 0
    private var barWidth: Int = 0
    private var margin: Int = 0
    private var listColor = intArrayOf()
    data class Points(val name: String, val position: Float)
    private var listPoints = arrayListOf<Points>()
    data class Labels(val name: String, val position: Float)
    private val listLabels = listOf<Labels>(
        Labels("Pico do exercício", 0.75f),
        Labels("Exercício cardio", 0.50f),
        Labels("Queima Gordura", 0.25f)
    )
    private val paint = Paint()
    private var intervals: FloatArray = floatArrayOf()
    private var path: Path = Path()
    private var rect = Rect()

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
            this.listColor = intArrayOf(
                context.resources.getColor(android.R.color.holo_red_light),
                context.resources.getColor(android.R.color.holo_orange_dark),
                context.resources.getColor(android.R.color.holo_orange_light),
                context.resources.getColor(android.R.color.holo_green_light),
                context.resources.getColor(android.R.color.holo_blue_light)
            )
            this.intervals = floatArrayOf(0.24f, 0.54f, 0.81f, 0.88f, 1f)

        //read xml attributes
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.GraphBar, 0, 0)
        barWidth = ta.getDimensionPixelSize(R.styleable.GraphBar_barWidth, 0)
        margin = ta.getDimensionPixelSize(R.styleable.GraphBar_margin, 10)
        textSizeMessage = ta.getDimensionPixelSize(R.styleable.GraphBar_textSizeMessage, 20)

        ta.recycle()

    }


    override fun onDraw(canvas: Canvas) {

        if (!isInEditMode && height < 0 || width < 0) {
            return
        }
        barHeight = height.toFloat() - (margin * 2) - paddingBottom - paddingTop
        drawBarColor(canvas)
        drawMessages(canvas)
        drawPoints(canvas)
    }


    @SuppressLint("ResourceType")
    private fun drawPoints(canvas: Canvas) {

        if (listPoints != null) {
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
                val x = (((width / 2) - margin - barWidth) - widthMin).toFloat()

                paint.reset()
                paint.apply {
                    isAntiAlias = true
                    textSize = 20f
                    color = ContextCompat.getColor(context, android.R.color.darker_gray)
                    textAlign = Paint.Align.LEFT
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                }
                path.reset()

                val halfTriangle=((heightMin+6)/2).toFloat()

                path.apply {
                    moveTo(heightMin.toFloat(), 0f)
                    lineTo(0f, halfTriangle)
                    lineTo(0f, -halfTriangle)
                    close()
                    offset(x + 35, y - ((heightMin-2)/2))
                }
                canvas.drawPath(path, paint)

                rect = Rect(
                    x.toInt() - 4,
                    (y.toInt()-2) - heightMin,
                    (x.toInt() + 4) + widthMin,
                    y.toInt() + 4
                )

                canvas.drawRect(rect, paint)
                canvas.drawText(points.name, x, y, labelPaint)

            }
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

    private fun drawBarColor(canvas: Canvas) {

        val startEndOffset = (width + margin - barWidth) / 2
        var y0 = margin.toFloat()
        val x1 = (width - startEndOffset).toFloat()

        val x0 = (0 + startEndOffset).toFloat()

        paint.apply {
            reset()
            isAntiAlias = true
        }

        listColor.forEachIndexed { index, color ->
            val y1 =
                (intervals[index] * barHeight) + margin

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
}