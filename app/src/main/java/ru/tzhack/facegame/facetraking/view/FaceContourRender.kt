package ru.tzhack.facegame.facetraking.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
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

    private val dotSize = 3f

    /*TODO: Создать два Paint объекта
    *  1. Будем рисовать точки (белый)
    *  2. Будем рисовать "квадрат" лица */

    /* TODO: faceContour Создать List объектов FirebaseVisionPoint,
    *   это точки, которые необходимо нарисовать */

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

        //TODO: Очищаем старые точки , и добавляем новые (faceContour)

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (this.width != 0 && this.height != 0) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

            // TODO: Два цикла
            // TODO: 1. пробегаемся по списку объектов FirebaseVisionPoint
            // TODO: 2. Для каждого из объектов - рисуем круг на Canvas'e
            // Не забудьте, что нужно применить translateX и translateY,
            // чтобы "маска" четко легла на лицо



//            TODO: раскомментировать, как только создадите объект paintBox
//            canvas.drawRect(rect, paintBox)
        }
    }

    private fun Float.translateX(): Float = width - scaleX()
    private fun Float.scaleX(): Float = this * widthScaleFactor
    private fun Float.translateY(): Float = this * heightScaleFactor

    private fun Int.translateX(): Int = width - scaleX()
    private fun Int.scaleX(): Int = (this * widthScaleFactor).toInt()
    private fun Int.translateY(): Int = (this * heightScaleFactor).toInt()
}