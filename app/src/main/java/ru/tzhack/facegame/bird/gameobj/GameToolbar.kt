package ru.tzhack.facegame.bird.gameobj

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import ru.tzhack.facegame.R
import ru.tzhack.facegame.bird.utils.createBitmap

/**
 *  Игровая информация:
 *  - Таймер
 *  - Кол-во выстрелов
 *  Реализовать методы
 *  1. [update]
 *  2. [draw]
 *  3. Метод для увелечения таймера
 */
class GameToolbar(context: Context, widthScreen : Float) {

    // размер спрайта выстрела
    private val sideSprite = 70f
    // увелечение таймера при поднятии бонуса
    private val bonusTime = 5f

    private var countShots = 0

    private val imageShots =  context.createBitmap(R.drawable.bonus_shot, sideSprite, sideSprite)

    private var time = 120f

    private val startPositionTime = widthScreen.toFloat() - 285f

    private val startPositionShotImg = widthScreen.toFloat() - 140f

    private val startPositionCountShot = widthScreen.toFloat() - 45f

    fun getTime() : Float {
        return time
    }

    companion object {
        private const val yTopIndent = 55f
    }

    /**
     * Обновление таймера
     */
    fun update(dt: Float) {
        time -= dt
    }

    /**
     *  1. Отрисовка таймера
     *  2. кол-во выстрелов, иконку выстрела
     */
    fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawText(timeToText(), startPositionTime,  yTopIndent, paint)
        canvas.drawBitmap(imageShots, startPositionShotImg,  0f, paint)
        canvas.drawText(countShots.toString(), startPositionCountShot,  yTopIndent, paint)

    }

    private fun timeToText() : String {
        val second = time.toInt() % 60
        val secondStr : String = if(second < 2) "0" + second.toString() else second.toString()
        return "0" + (time.toInt() / 60).toString() + ":" + secondStr
    }

    fun catchupBonus (){
        time += bonusTime
    }
}