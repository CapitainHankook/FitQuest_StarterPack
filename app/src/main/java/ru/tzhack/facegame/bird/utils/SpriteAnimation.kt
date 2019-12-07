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
    val OneFrameTime: Float=cycleSec.div(frames.size)
    var RealOFT: Float=0F
    //TODO: Реализовать метод: 1. Переключение фрейма 2. Получение текущего


    // переключение фрейма
    fun update(dt: Float) {
        if (RealOFT>=OneFrameTime)
        {
            RealOFT=0F
            Cur++;
            if (Cur>=frames.size)
                Cur=0;
        }
        RealOFT+=dt

    }
    fun reset()
    {
        Cur=0;
        RealOFT=0F;
    }

    fun getCurrent():Bitmap{
        return frames[Cur]
    }

}