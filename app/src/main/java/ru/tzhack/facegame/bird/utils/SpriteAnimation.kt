package ru.tzhack.facegame.bird.utils

import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import ru.tzhack.facegame.R

/**
 * Отвечает за спрайтовую анимацию, переключение фреймов
 */
class SpriteAnimation(
    private val frames: Array<Bitmap>,
    val cycleSec: Float
) {
    var Cur: Int=1;
    //TODO: Реализовать метод: 1. Переключение фрейма 2. Получение текущего
    // переключение фрейма
    fun update(dt: Float) {
        Cur=(dt.rem(cycleSec)).toInt()
    }

    fun getCurrent():Bitmap{

        return frames[Cur]
    }

}