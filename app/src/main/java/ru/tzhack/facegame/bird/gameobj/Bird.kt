package ru.tzhack.facegame.bird.gameobj

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import ru.tzhack.facegame.R
import ru.tzhack.facegame.bird.Viewport
import ru.tzhack.facegame.bird.utils.Position
import ru.tzhack.facegame.bird.utils.SpriteAnimation
import ru.tzhack.facegame.bird.utils.createBitmap
import ru.tzhack.facegame.bird.utils.createBitmaps

/**
 * Реализовать методы
 * 1. [update]
 * 2. [draw]
 * 3. Установка состояния движения по горизонтали (ввод пользователя)
 * 4. Установка состояния выстрела
 * 5. Поднятие бонуса
 */
class Bird(
    context: Context,
    private val screenX: Float
) {
    companion object {
        // размер спрайта
        private const val K_SPRITE = 3f
        private const val WIDTH_SPRITE = 524f / K_SPRITE
        private const val HEIGHT_SPRITE = 616f / K_SPRITE
        private const val SPRITE_CYCLE_SEC = 0.5f

        // характеристики объекта
        private const val SPEED_VERTICAL_DEFAULT = 200
        private const val SPEED_VERTICAL_STEP = 70
        private const val BONUS_MAX_TIME = 5
        private const val MAX_SPEED_HORIZONTAL = 500
    }

    val position=Position((screenX/2)- (WIDTH_SPRITE/2), 0F, WIDTH_SPRITE, HEIGHT_SPRITE);
    val sprAni=SpriteAnimation(context.createBitmaps(WIDTH_SPRITE, HEIGHT_SPRITE,
            R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4,
            R.drawable.a5, R.drawable.a6, R.drawable.a7, R.drawable.a8),0.1.toFloat())
    enum class Napr {
        LEFT, JUST, RIGTH
    }
    var napr: Napr=Napr.JUST;


    /**  обновление игрового состояния
     * 1. Постоянное смещение объекта вверх
     * 2. Реакция на ввод пользователя смещение по горизонтали
     * 3. Обновление спрайта
     * 4. Проверка возможности столкновения
     * 5. Обработка состояния выстрела
     * 6. Обработка эффекта от бонуса
     */
    fun update(dt: Float) {
        //шагвверх
        position.top+= dt* SPEED_VERTICAL_DEFAULT;
        sprAni.update(dt);
        if (napr==Napr.LEFT&&position.left>0)
            position.left-=dt* MAX_SPEED_HORIZONTAL;
        if (napr==Napr.RIGTH&&position.left<screenX-WIDTH_SPRITE)
           position.left+=dt* MAX_SPEED_HORIZONTAL;

    }

    fun Left()
    {
        napr=Napr.LEFT
    }
    fun Right()
    {
        napr=Napr.RIGTH
    }
    fun Just()
    {
        napr=Napr.JUST
    }

    /**
     * отрисовка текущего фрейма
     */
    fun draw(canvas: Canvas, paint: Paint, viewport: Viewport) {

        canvas.drawBitmap(sprAni.getCurrent(), position.left, viewport.convertToDisplay(position), paint);

    }

}