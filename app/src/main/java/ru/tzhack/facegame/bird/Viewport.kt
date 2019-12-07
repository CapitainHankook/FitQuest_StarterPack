package ru.tzhack.facegame.bird

import ru.tzhack.facegame.bird.utils.Position

/**
 *  Управление игровой областью
 *
 *  Реализовать методы
 *  1. Перевод глобальных координат в координаты дисплея
 *  2. Центрирование на объекте пользователя
 *  3. Проверка попадает ли игровой объект в область дисплея
 */
class Viewport(private val game : Game, private val screenWidth : Float, private val screenHeight: Float) {
    private var y : Float = 0f

    companion object {
        // отступ снизу при центрировании на игровом объекта
        private const val BOTTOM_PADDING = 300f
    }

    fun convertToDisplay (position : Position) : Float {
        return y - position.top
    }

    fun convertToDisplay (y:  Float) : Float {
        return this.y - y
    }


    fun centreCamera(birdPosition: Position) {
        y = birdPosition.top + screenHeight - BOTTOM_PADDING
    }

    fun isVisible (position: Position) : Boolean {
        return position.top >= y - screenHeight || position.top <= y + screenHeight
    }
}