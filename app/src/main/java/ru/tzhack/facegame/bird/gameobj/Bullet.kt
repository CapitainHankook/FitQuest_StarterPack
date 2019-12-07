package ru.tzhack.facegame.bird.gameobj

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import ru.tzhack.facegame.R
import ru.tzhack.facegame.bird.Viewport
import ru.tzhack.facegame.bird.gameobj.Bullet.Companion.create
import ru.tzhack.facegame.bird.utils.Position
import ru.tzhack.facegame.bird.utils.createBitmap

/**
 *  Выстрел
 * Реализовать методы
 * 1. [create]
 * 2. [draw]
 * 3. Метод для установки состояния "столкнулся"
 * 4. [update]
 * 5. Утилизация объекта после перемещения на максимальную дистанцию
 */
class Bullet(
        context: Context,
        val position: Position
) {

    private val image = context.createBitmap(R.drawable.bullet_flies, widthFly, heightFly)

    private val imageCrash = context.createBitmap(R.drawable.bullet_crashed, sideCrashed, sideCrashed)

    var destroyed = false

    var explosioned = false

    private var current_distance = MAX_DISTANCE

    private var time_crashed = CRASHED_VISIBLE_MAX_TIME

    companion object {

        // размер спрайта
        private const val K_SPRITE = 2f
        private const val widthFly = 89f / K_SPRITE
        private const val heightFly = 360f / K_SPRITE
        private const val sideCrashed = 224f
        private const val CRASHED_VISIBLE_MAX_TIME = 0.5f

        // характеристики объекта
        private const val SPEED = 600
        private const val MAX_DISTANCE = 600f

        /**
         *  Создание объекта
         */
        fun create(context: Context, birdPosition: Position): Bullet {
            return Bullet(context , birdPosition.copy())

        }
    }

    /**
     *  Смещение позиции если ещё нет столкновения
     */
    fun update(dt: Float) : Unit {
        if (destroyed) return

        if (current_distance > 0 && !explosioned) {
            position.top += dt * SPEED
            current_distance -= dt * SPEED
            if (current_distance <= 0) explosioned = true

        } else if (explosioned && time_crashed > 0) {
            time_crashed -= dt

        } else {
            destroyed = true
        }

    }

    /**
     * 1. Отрисовка
     */
    fun draw(canvas: Canvas, paint: Paint, viewport: Viewport) {
        if (explosioned) canvas.drawBitmap(imageCrash, position.left, viewport.convertToDisplay(position), paint)
        else canvas.drawBitmap(image, position.left, viewport.convertToDisplay(position), paint)

    }
}