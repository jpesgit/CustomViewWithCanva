package com.example.joaopedrosilva.customviewwithcanvas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View

@Suppress("PrivatePropertyName", "LocalVariableName", "JoinDeclarationAndAssignment")
class Graph : View {
    private var barCenterVertical: Float = 0f
    private var barCenterHorizontal: Float = 0f
    private var top: Float = 0f
    private var bottom: Float = 0f
    private var left: Float = 0f
    private var right: Float = 0f
    private var sizeBar: Float = 0f
    private var oldSize: Float = 0f
    private var cubeSize: Float = 0f
    private var barHeight: Int = 0
    private var barWidth: Int = 0
    private val listColor = listOf(
        context.resources.getColor(android.R.color.holo_red_light),
        context.resources.getColor(android.R.color.holo_orange_dark),
        context.resources.getColor(android.R.color.holo_orange_light),
        context.resources.getColor(android.R.color.holo_green_light),
        context.resources.getColor(android.R.color.holo_blue_light)
    )
    private val cube =
        listOf(5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100)

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
    private val paint= Paint()
    private var arrowPoint: Path = Path()
    private var rectanglePoint = Rect()
    private var rectf = RectF()

    @SuppressLint("NewApi")
    @Suppress("ConvertSecondaryConstructorToPrimary")
    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        isSaveEnabled = true

        //read xml attributes
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.GraphBar, 0, 0)
        barWidth = ta.getDimensionPixelSize(R.styleable.GraphBar_barWidth, 0)
        barHeight = ta.getDimensionPixelSize(R.styleable.GraphBar_barHeight, 0)
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(barWidth, barHeight)
    }

    override fun onDraw(canvas: Canvas) {
        drawBar(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (h > 0) {
            barCenterVertical = ((height - paddingTop - paddingBottom) / 2).toFloat()
        }
        if (w > 0) {
            barCenterHorizontal = ((width - paddingLeft - paddingRight) / 2).toFloat()
        }


    }

    private fun drawBar(canvas: Canvas) {
        if(height < 0 || width < 0){
            return
        }
        top = barCenterVertical - barHeight
        bottom = barCenterVertical + barHeight
        left = barCenterHorizontal - barWidth
        right = barCenterHorizontal + barWidth
        sizeBar = bottom - top
        cubeSize = sizeBar / cube.size

        for (x in cube) {
            paint.reset()
            paint.color = getColor(x)
            rectf = RectF(left, bottom - oldSize - cubeSize, right, bottom - oldSize)
            canvas.drawRect(rectf, paint)
            oldSize += cubeSize
        }
        if (listPoints != null) {
            for (points in listPoints) {
                val bounds = Rect()
                pointPaintText.getTextBounds(points.name, 0, points.name.length, bounds)
                val widthMin = if (bounds.width() < 32) 32 else bounds.width()
                val heightMin = if (bounds.height() < 16) 16 else bounds.height()
                val y = ((bottom + paddingBottom) + (heightMin / 2)) - (sizeBar * points.position)
                val x = (barCenterHorizontal - barWidth) - widthMin - 30
                arrowPoint.reset()
                arrowPoint.apply {
                    moveTo(20f, 0f)
                    lineTo(0f, 10f)
                    lineTo(0f, -10f)
                    close()
                    offset(x + 35, y - 6.5f)
                }
                //design rect
                rectanglePoint = Rect(
                    x.toInt() - 4,
                    (y.toInt()) - heightMin,
                    (x.toInt() + 4) + widthMin,
                    y.toInt() + 4
                )
                canvas.drawPath(arrowPoint, pointPaintBg)
                canvas.drawRect(rectanglePoint, pointPaintBg)
                canvas.drawText(points.name, x, y, pointPaintText)
            }
        }

        for (label in listLabels) {
            labelPaint.getTextBounds(label.name, 0, label.name.length, rectanglePoint)

            val y = (bottom + paddingBottom) - (sizeBar * label.position)
            val x = (barCenterHorizontal + barWidth) + 30

            canvas.drawText(label.name, x, y, labelPaint)
        }


    }

    private fun getColor(x:Int):Int{
          return when (x) {
                in 0..10 -> listColor[4]
                in 11..18 -> listColor[3]
                in 19..46 -> listColor[2]
                in 47..76 -> listColor[1]
                in 77..100 -> listColor[0]
                else -> listColor[0]
            }


    }
    fun setPoints(list: ArrayList<Points>) {
        this.listPoints = list
        invalidate()
    }
}