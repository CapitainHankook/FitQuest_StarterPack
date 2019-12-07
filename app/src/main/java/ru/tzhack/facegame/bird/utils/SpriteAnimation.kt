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
    var Cur: Int=0;
    var CurSec: Float=0F;

    //TODO: Реализовать метод: 1. Переключение фрейма 2. Получение текущего


    // переключение фрейма
    fun update(dt: Float) {
        if (dt.div(cycleSec)-CurSec>cycleSec)
        {
            CurSec=dt;
            Cur++;
            if (Cur>=8)
                Cur=0;
        }

    }

    fun getCurrent():Bitmap{

        return frames[Cur]
    }

}