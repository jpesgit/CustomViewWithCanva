package com.example.joaopedrosilva.customviewwithcanvas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View

@Suppress("PrivatePropertyName", "LocalVariableName", "JoinDeclarationAndAssignment")
class Graph : View {
    private var barCenterHorizontal: Float = 0f
    private var barHeight: Float = 0f
    private var textSizeMessage: Int = 0
    private var oldSize: Float = 0f
    private var cubeSize: Float = 0f
    private var barWidth: Int = 0
    private var margin: Int = 0
    private var sliderRect: Rect = Rect()
    private val listColor = listOf(
        context.resources.getColor(android.R.color.holo_red_light),
        context.resources.getColor(android.R.color.holo_orange_dark),
        context.resources.getColor(android.R.color.holo_orange_light),
        context.resources.getColor(android.R.color.holo_green_light),
        context.resources.getColor(android.R.color.holo_blue_light)
    )


    data class Points(val name: String, val position: Float)

    private var listPoints = arrayListOf<Points>()

    data class Labels(val name: String, val position: Float)

    private val listLabels = listOf<Labels>(
        Labels("Pico do exercício", 0.75f),
        Labels("Exercício cardio", 0.50f),
        Labels("Queima Gordura", 0.25f)
    )
    private var listPaint = ArrayList<Paint>()
    private val pointPaintText: Paint
    private val pointPaintBg: Paint
    private val labelPaint: Paint
    private val paint = Paint()
    private var intervals: FloatArray = floatArrayOf()
    private var arrowPoint: Path = Path()
    private var rectanglePoint = Rect()
    private var rectf = RectF()
    private var sliderWidth: Int = 200

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
        isSaveEnabled = true
        this.intervals = floatArrayOf(0.2f, 0.4f, 0.6f, 0.8f, 1f)
        //read xml attributes
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.GraphBar, 0, 0)
        barWidth = ta.getDimensionPixelSize(R.styleable.GraphBar_barWidth, 0)
        margin = ta.getDimensionPixelSize(R.styleable.GraphBar_margin, 0)
        margin += 10
        textSizeMessage = ta.getDimensionPixelSize(R.styleable.GraphBar_textSizeMessage, 20)
        pointPaintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 20f
            color = ContextCompat.getColor(context, android.R.color.white)
            textAlign = Paint.Align.LEFT
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }
        pointPaintBg = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.DKGRAY
        }
        labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 20f
            color = ContextCompat.getColor(context, android.R.color.darker_gray)
            textAlign = Paint.Align.LEFT
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }



        ta.recycle()


    }


    override fun onDraw(canvas: Canvas) {
        if (height < 0 || width < 0) {
            return
        }
        barHeight = height.toFloat() - (margin * 2) - paddingBottom - paddingTop
        drawBarColor(canvas)
        drawMessages(canvas)
    }


    private fun drawPoints(canvas: Canvas) {


//        if (listPoints != null) {
//            for (points in listPoints) {
//                val bounds = Rect()
//                pointPaintText.getTextBounds(points.name, 0, points.name.length, bounds)
//                val widthMin = if (bounds.width() < 32) 32 else bounds.width()
//                val heightMin = if (bounds.height() < 16) 16 else bounds.height()
//                val y = ((bottom + paddingBottom) + (heightMin / 2)) - (sizeBar * points.position)
//                val x = (barCenterHorizontal - barWidth) - widthMin - 30
//                arrowPoint.reset()
//                arrowPoint.apply {
//                    moveTo(20f, 0f)
//                    lineTo(0f, 10f)
//                    lineTo(0f, -10f)
//                    close()
//                    offset(x + 35, y - 6.5f)
//                }
//                //design rect
//                rectanglePoint = Rect(
//                    x.toInt() - 4,
//                    (y.toInt()) - heightMin,
//                    (x.toInt() + 4) + widthMin,
//                    y.toInt() + 4
//                )
//                canvas.drawPath(arrowPoint, pointPaintBg)
//                canvas.drawRect(rectanglePoint, pointPaintBg)
//                canvas.drawText(points.name, x, y, pointPaintText)
//            }
//        }
//


    }

    private fun drawMessages(canvas: Canvas) {
        for (label in listLabels) {
            paint.reset()
            paint.apply {
                textSize = textSizeMessage.toFloat()
                isAntiAlias=true
                color = ContextCompat.getColor(context, android.R.color.darker_gray)
                textAlign = Paint.Align.LEFT
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            }
            val bounds= Rect()
            paint.getTextBounds(label.name, 0, label.name.length, bounds)


            val y = margin + (barHeight * label.position)
            val x = ((width /2) + barWidth  + 30).toFloat()

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