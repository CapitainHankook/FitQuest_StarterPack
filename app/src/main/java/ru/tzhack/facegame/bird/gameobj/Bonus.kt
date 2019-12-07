package ru.tzhack.facegame.bird.gameobj

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import ru.tzhack.facegame.R
import ru.tzhack.facegame.bird.Viewport
import ru.tzhack.facegame.bird.gameobj.Bonus.Companion.create
import ru.tzhack.facegame.bird.utils.Position
import ru.tzhack.facegame.bird.utils.createBitmap


enum class BonusType {
    SPEED_UP,
    SPEED_DOWN,
    SHOT,
    TIME
}

/**
 * Игровой бонус
 * Реализовать методы
 * 1. [create]
 * 2. [update]
 * 3. [draw]
 * 4.  Проверка столкновений
 */
class Bonus(
    private val position: Position,
    private val speed: Float,
    val type: BonusType

) {

    companion object {
        // размер спрайта
        private const val SIDE_SPRITE = 400f / 3.5f

        // диапозон скорости падения бонуса
        private val SPEED_VERTICAL = 100..200

        // генерация
        // горизонтальный отступ от границы экрана
        private const val PADDING_HORIZONTAL = 50
        // растояние между бонусами
        private const val SPACE_Y = 1000

        // создать бонус когда игрок будет находится в координате
        var generateWhenPositionY = 700

        lateinit var bitmapSpeedDown: Bitmap
        lateinit var  bitmapSpeedUp: Bitmap
        lateinit var  bitmapTime: Bitmap
        lateinit var  bitmapShot: Bitmap

        fun init(context: Context){
             bitmapSpeedDown = context.createBitmap(R.drawable.bonus_speed_down, SIDE_SPRITE, SIDE_SPRITE)
             bitmapSpeedUp = context.createBitmap(R.drawable.bonus_speed_up, SIDE_SPRITE, SIDE_SPRITE)
             bitmapTime=context.createBitmap(R.drawable.bonus_time, SIDE_SPRITE, SIDE_SPRITE)
             bitmapShot=context.createBitmap(R.drawable.bonus_shot, SIDE_SPRITE, SIDE_SPRITE)

        }

        /**
         *  Создать объект
         */
        fun create(position: Position, sizeX: Int, sizeY :Int): Bonus {

            val x = (1..4).random();
            lateinit var type :BonusType

            when(x){
                1 -> type = BonusType.TIME
                2 -> type = BonusType.SPEED_DOWN
                3 -> type = BonusType.SPEED_UP
                4 -> type = BonusType.SHOT
            }

            val speed = SPEED_VERTICAL.random()
            val leftPosition = (PADDING_HORIZONTAL..sizeX).random()
            var top = generateWhenPositionY + sizeY
            val bonusPosition = Position(leftPosition.toFloat(), top.toFloat(), SIDE_SPRITE, SIDE_SPRITE)
            val bonus = Bonus(bonusPosition,speed.toFloat(),type)

            generateWhenPositionY+=SPACE_Y

            return bonus

        }
    }

    fun checkOnCollision(bird_position: Position): Boolean {
        return position.contains(bird_position)
    }

    /**
     *  Обновление позиции
     */
    fun update(dt: Float) {
        position.top -= speed * dt
    }



    /**
     * 1. Отрисовка
     * 2. Проверить находится ли объект на экране в данный момент
     */
    fun draw(canvas: Canvas, paint: Paint, viewport: Viewport, context: Context) {
        val positionTop = viewport.convertToDisplay(position.top)


        val currentBitmap = when(type)
        {
            BonusType.SPEED_UP -> bitmapSpeedUp
            BonusType.SPEED_DOWN -> bitmapSpeedDown
            BonusType.SHOT -> bitmapShot
            BonusType.TIME -> bitmapTime
        }
        canvas.drawBitmap(currentBitmap,position.left,positionTop,paint)

    }

}