package kg.nurtelecom.chat_engine.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kg.nurtelecom.chat_engine.R

class TouchAndDrawView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.TouchAndDrawViewDefaultStyle,
    defStyle: Int = R.style.chat_engine_TouchAndDrawStyle
) : View(context, attributeSet, defStyleAttr, defStyle) {

    private var isStartedToDraw = false

    private var drawingCallbackListener: TouchAndDrawViewCallback? = null

    fun setupView(listener: TouchAndDrawViewCallback) {
        this.drawingCallbackListener = listener
    }

    private val paint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
            strokeCap = Paint.Cap.ROUND
            strokeWidth = resources.getDimensionPixelSize(com.design.chili.R.dimen.view_4dp).toFloat()
        }
    }

    private val drawingShapes = mutableListOf<MutableList<DrawingPoint>>()

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                drawingShapes.add(mutableListOf(DrawingPoint(event.x, event.y)))
            }
            else -> drawingShapes.lastOrNull()?.add(DrawingPoint(event.x, event.y))
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        if (drawingCallbackListener != null && drawingShapes.isNotEmpty() && !isStartedToDraw) {
            drawingCallbackListener?.onStartDrawing()
            isStartedToDraw = true
        }
        drawingShapes.forEach {
            drawLines(canvas, it)
        }
    }

    private fun drawLines(canvas: Canvas, list: List<DrawingPoint>) {
        when {
            list.size == 1 -> canvas.drawPoint(list[0], paint)
            list.size > 1 -> {
                canvas.drawPoint(list[0], paint)
                for (i in 1 until list.size) {
                    canvas.drawLine(list[i - 1], list[i], paint)
                }
            }
        }
    }

    fun clearCanvas() {
        if (drawingCallbackListener != null && isStartedToDraw) {
            drawingCallbackListener?.onClearCanvas()
            isStartedToDraw = false
        }
        drawingShapes.clear()
        invalidate()
    }
}

interface TouchAndDrawViewCallback {
    fun onStartDrawing()
    fun onClearCanvas()
}

data class DrawingPoint(val x: Float, val y: Float)

fun Canvas.drawPoint(point: DrawingPoint, paint: Paint) {
    drawPoint(point.x, point.y, paint)
}

fun Canvas.drawLine(prevPoint: DrawingPoint, nextPoint: DrawingPoint, paint: Paint) {
    drawLine(prevPoint.x, prevPoint.y, nextPoint.x, nextPoint.y, paint)
}