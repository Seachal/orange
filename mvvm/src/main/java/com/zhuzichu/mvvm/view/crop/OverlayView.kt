package com.zhuzichu.mvvm.view.crop

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.view.View
import androidx.annotation.IntDef

class OverlayView(context: Context) : View(context) {

    private var overlayColor: Int = Color.TRANSPARENT
    private var overlayShape: Int = SHAPE_OVAL
    private val clearPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var viewport = RectF()

    constructor(context: Context, @OverlayShape shape: Int) : this(context) {
        this.overlayShape = shape
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        clearPaint.color = Color.BLACK
        clearPaint.style = Paint.Style.FILL
        clearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        if (viewport.isEmpty) {
            with(viewport) {
                left = 0f
                top = 0f
                right = width.toFloat()
                bottom = height.toFloat()
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun setOverlayColor(color: Int) {
        overlayColor = color
        invalidate()
    }

    fun setOverlayShape(@OverlayShape shape: Int) {
        overlayShape = shape
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(overlayColor)
        when (overlayShape) {
            SHAPE_OVAL -> canvas.drawOval(viewport, clearPaint)
            else -> canvas.drawRect(viewport, clearPaint)
        }
    }

}

@IntDef(SHAPE_OVAL, SHAPE_RECT)
@Retention(AnnotationRetention.SOURCE)
annotation class OverlayShape

const val SHAPE_OVAL = 0
const val SHAPE_RECT = 1