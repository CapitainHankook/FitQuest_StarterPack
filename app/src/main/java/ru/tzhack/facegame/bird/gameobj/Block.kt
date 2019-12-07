package ru.tzhack.facegame.bird.gameobj

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import ru.tzhack.facegame.R
import ru.tzhack.facegame.bird.Viewport
import ru.tzhack.facegame.bird.gameobj.Block.Companion.generate
import ru.tzhack.facegame.bird.utils.Position
import ru.tzhack.facegame.bird.utils.createBitmap

/**
 * Препядствие
 *
 * Реализовать методы
 * 1. [generate]
 * 2. [draw]
 * 3. Проверка столкновений
 */
class Block(
        context: Context,
        val position: Position
) {

    val bitmap = context.createBitmap(R.drawable.pad1, WIDTH_SPRITE, HEIGHT_SPRITE);


    companion object {
        // размер спрайта
        private const val K_SPRITE = 1.5f
        private const val HEIGHT_SPRITE = 276f / K_SPRITE
        private const val WIDTH_SPRITE = 828f / K_SPRITE
        private const val OVERLAY_HORIZONTAL_SPRITE = 120f / K_SPRITE
        private const val COLLISION_ALLOWED_SPRITE = 75f / K_SPRITE

        // генерация
        // минимальный размер блока
        private const val minWidth = 100f
        // максимальный разброс ширины
        private const val rangeRandomWidth = 600
        // растояние между блоками по горизонтали
        private const val spaceSize = 300f
        // растояние между блоками по вертикали
        private const val wallsSpacing = 300
        // координата первого блока
        private const val startY = 1000f

        /**
         *  Генерация блоков
         */
        fun generate(context: Context, screenX: Float, count: Int): List<Block> {
            val blocks = ArrayList<Block>()
            var nextY = startY;
            var counter = 0;
            while(counter<25)
            {

                    var width0 = minWidth + (0..rangeRandomWidth).random();
                    var width1 = screenX - spaceSize - width0;
                    val positionLeft = Position(0.toFloat(), nextY, width0, HEIGHT_SPRITE);
                    val positionRight = Position(positionLeft.right+ spaceSize, nextY, width1, HEIGHT_SPRITE);

                    blocks += Block(context, positionLeft)
                    blocks += Block(context, positionRight)

                counter++;
                nextY+= wallsSpacing;

            }

            return blocks
        }
    }
    fun checkOnCollision(bird_position: Position):Boolean
    {
        return position.contains(bird_position)
    }
    /**
     * 1. Отрисовка
     * 2. Проверить находится ли объект на экране в данный момент
     */
    fun draw(canvas: Canvas, paint: Paint, viewport: Viewport) {
        val positionTop = viewport.convertToDisplay(position.top);
        if(position.left==0.toFloat())
        {
            val positionLeft = position.left - WIDTH_SPRITE + position.width;
            canvas.drawBitmap(bitmap, positionLeft, positionTop, paint);
        }
        else
        canvas.drawBitmap(bitmap, position.left, positionTop, paint);
    }
}