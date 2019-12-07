package ru.tzhack.facegame.facetraking.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.google.firebase.ml.vision.common.FirebaseVisionPoint
import com.otaliastudios.cameraview.size.Size


//TODO: ё
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
    private var rectangleStrokeWidth = 5f

    private var dotPaintCircle: Paint = Paint()

    var dotColor = Color.WHITE

    private val dotSize = 3f

    /*TODO: Создать два Paint объекта
    *  1. Будем рисовать точки (белый)
    *  2. Будем рисовать "квадрат" лица */

    /* TODO: faceContour Создать List объектов FirebaseVisionPoint,
    *   это точки, которые необходимо нарисовать */
    private var faceContour : List<FirebaseVisionPoint> = ArrayList<FirebaseVisionPoint>()
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

        faceRect?.let {
            rect = it.apply {
                set(left.translateX(), top.translateY(), right.translateX(), bottom.translateY())
            }
        }

        faceContour = points

        //TODO: Очищаем старые точки , и добавляем новые (faceContour) вроде сделано

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        rectanglePaintBox.color = rectangleColor
        rectanglePaintBox.strokeWidth = rectangleStrokeWidth

        dotPaintCircle.color = dotColor

        if (this.width != 0 && this.height != 0) {

            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

            faceContour.forEach { point ->
                canvas.drawCircle(point.x.translateX(), point.y.translateY(), dotSize, dotPaintCircle)
            }
            // TODO: Два цикла
            // TODO: 1. пробегаемся по списку объектов FirebaseVisionPoint
            // TODO: 2. Для каждого из объектов - рисуем круг на Canvas'e вроде сделано форичем
            // Не забудьте, что нужно применить translateX и translateY,
            // чтобы "маска" четко легла на лицо

//            TODO: раскомментировать, как только создадите объект paintBox вроде сделано
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