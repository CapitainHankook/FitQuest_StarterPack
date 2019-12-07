package ru.tzhack.facegame.facetraking.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.google.firebase.ml.vision.common.FirebaseVisionPoint
import com.otaliastudios.cameraview.size.Size


//TODO: ЗАДАНИЕ #3
/**
 * Задание №3.
 *
 * Восстановление работоспособности FaceContourRender'a
 *
 * Упс! Кто-то все сломал, но оставил подсказки.
 * Чините скорее, ведь еще столько всего предстоит сделать.
 * */
class FaceContourRender @JvmOverloads constructor(
        context: Context,
        private val attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var rectanglePaintBox: Paint = Paint()

    var rectangleColor = Color.GREEN
    private var rectangleStrokeWidth = 1.5f

    private var dotPaintCircle: Paint = Paint()

    var dotColor = Color.WHITE

    private val dotSize = 3f


    init {
        rectanglePaintBox.color = rectangleColor
        rectanglePaintBox.style = Paint.Style.STROKE
        rectanglePaintBox.strokeWidth = rectangleStrokeWidth

        dotPaintCircle.color = dotColor
    }

    private var faceContour: List<FirebaseVisionPoint> = ArrayList()
    private var rect = Rect()

    private var widthScaleFactor = 1.0F
    private var heightScaleFactor = 1.0F

    fun updateContour(
            frameSize: Size,
            faceRect: Rect?,
            points: List<FirebaseVisionPoint>
    ) {
        frameSize.let {
            widthScaleFactor = width.toFloat() / it.width.toFloat()
            heightScaleFactor = height.toFloat() / it.height.toFloat()
        }

        if (faceRect == null) rect.setEmpty()
        else rect = faceRect.apply {
            set(left.translateX(), top.translateY(), right.translateX(), bottom.translateY())
        }

        faceContour = points

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (this.width != 0 && this.height != 0) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

            faceContour.forEach { point ->
                canvas.drawCircle(point.x.translateX(), point.y.translateY(), dotSize, dotPaintCircle)
            }

            canvas.drawRect(rect, rectanglePaintBox)
        }
    }

    private fun Float.translateX(): Float = width - scaleX()
    private fun Float.scaleX(): Float = this * widthScaleFactor
    private fun Float.translateY(): Float = this * heightScaleFactor

    private fun Int.translateX(): Int = width - scaleX()
    private fun Int.scaleX(): Int = (this * widthScaleFactor).toInt()
    private fun Int.translateY(): Int = (this * heightScaleFactor).toInt()
}