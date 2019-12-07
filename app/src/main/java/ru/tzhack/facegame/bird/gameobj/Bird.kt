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

    val bitmap = context.createBitmap(R.drawable.a1, WIDTH_SPRITE, HEIGHT_SPRITE);
    val frames= Array<Bitmap>(8, {context.createBitmap(R.drawable.a1, WIDTH_SPRITE, HEIGHT_SPRITE);
        context.createBitmap(R.drawable.a2, WIDTH_SPRITE, HEIGHT_SPRITE);
        context.createBitmap(R.drawable.a3, WIDTH_SPRITE, HEIGHT_SPRITE);
        context.createBitmap(R.drawable.a4, WIDTH_SPRITE, HEIGHT_SPRITE);
        context.createBitmap(R.drawable.a5, WIDTH_SPRITE, HEIGHT_SPRITE);
        context.createBitmap(R.drawable.a6, WIDTH_SPRITE, HEIGHT_SPRITE);
        context.createBitmap(R.drawable.a7, WIDTH_SPRITE, HEIGHT_SPRITE);
        context.createBitmap(R.drawable.a8, WIDTH_SPRITE, HEIGHT_SPRITE);});
    val position=Position((screenX/2)- (WIDTH_SPRITE/2), 15F, WIDTH_SPRITE, HEIGHT_SPRITE);
    //val sprAni=SpriteAnimation(frames,8)
    //var poscenter=Float;

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




    }

    fun Offset(ofst:Float)
    {
        if (ofst>screenX)
            position.left=ofst;
        else position.left=ofst;
    }

    /**
     * отрисовка текущего фрейма
     */
    fun draw(canvas: Canvas, paint: Paint, viewport: Viewport) {

        canvas.drawBitmap(bitmap, position.left, viewport.convertToDisplay(position), paint);

    }

}