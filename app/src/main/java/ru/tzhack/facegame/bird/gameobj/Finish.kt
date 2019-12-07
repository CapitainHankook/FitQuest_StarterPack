package ru.tzhack.facegame.bird.gameobj

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import ru.tzhack.facegame.R
import ru.tzhack.facegame.bird.Viewport
import ru.tzhack.facegame.bird.utils.createBitmap

/**
 * Финишная линия
 * Реализовать методы
 * 1. [draw]
 */
class Finish(
    private var positionY: Float,
    private val width: Float,
    context: Context
) {
    private val image : Bitmap

    // размер спрайта
    private val sideSprite = 300f / 4f

    init {
        image = context.createBitmap(R.drawable.finish, sideSprite, sideSprite)
    }

    /**
     * 1. Отрисовка
     * 2. Проверить находится ли объект на экране в данный момент
     */
    fun draw(canvas: Canvas, paint: Paint, viewport: Viewport) {
        var leftBorder : Float = 0f
        while (leftBorder < width) {
            canvas.drawBitmap(image, leftBorder, viewport.convertToDisplay(positionY), paint)
            leftBorder += sideSprite
        }

    }

    fun update() {
        positionY++
    }
}