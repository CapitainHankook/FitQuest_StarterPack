package ru.tzhack.facegame.bird.gameobj

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import ru.tzhack.facegame.R
import ru.tzhack.facegame.bird.Viewport
import ru.tzhack.facegame.bird.utils.Position
import ru.tzhack.facegame.bird.utils.SpriteAnimation
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
    var currentspeed: Int=SPEED_VERTICAL_DEFAULT;
    var currenthorspeed: Int=MAX_SPEED_HORIZONTAL;
    var curBonusTime: Float=0F;

    //var curKoef:Float=1F;

    val position=Position((screenX/2)-(WIDTH_SPRITE/2), 0F, WIDTH_SPRITE, HEIGHT_SPRITE);
    val sprAni=SpriteAnimation(context.createBitmaps(WIDTH_SPRITE, HEIGHT_SPRITE,
            R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4,
            R.drawable.a5, R.drawable.a6, R.drawable.a7, R.drawable.a8),0.5.toFloat())
    val shootAnimation=SpriteAnimation(context.createBitmaps(WIDTH_SPRITE, HEIGHT_SPRITE,
            R.drawable.as1, R.drawable.as2, R.drawable.as3, R.drawable.as4),0.5.toFloat())

    enum class Napr {
        LEFT, JUST, RIGTH
    }
    enum class LfRg
    {
        NOLEFT, NORIGTH, UP
    }
    enum class Speed
    {
        USUAL, FAST, SLOW
    }
    var birdspeed: Speed=Speed.USUAL
    var nlf: LfRg=LfRg.UP;
    var move: Boolean =true;
    var napr: Napr=Napr.JUST;
    var shoot: Boolean=false;


    /**  обновление игрового состояния
     * 1. Постоянное смещение объекта вверх
     * 2. Реакция на ввод пользователя смещение по горизонтали
     * 3. Обновление спрайта
     * 4. Проверка возможности столкновения
     * 5. Обработка состояния выстрела
     * 6. Обработка эффекта от бонуса
     */
    fun update(dt: Float, blocks : ArrayList<Block>) {
        if (birdspeed!=Speed.USUAL) {
            SpeedBonusEnd();
            curBonusTime += dt;
        }
        if (move)
            position.top+= dt* currentspeed;

        if(!shoot)
        sprAni.update(dt);
        else shootAnimation.update(dt);

        val pos1=Position(position.left-dt* currenthorspeed, position.top, WIDTH_SPRITE, HEIGHT_SPRITE);
        val pos2=Position(position.left+dt* currenthorspeed, position.top, WIDTH_SPRITE, HEIGHT_SPRITE);
        for( block in blocks)
        {
            if(block.checkOnCollision(pos1)&&move)
            {
                nlf=LfRg.NOLEFT
            }
            if(block.checkOnCollision(pos2)&&move)
            {
                nlf=LfRg.NORIGTH
            }
        }


        if (napr==Napr.LEFT&&position.left>0&&nlf!=LfRg.NOLEFT)
            position.left-=dt* currenthorspeed;
        if (napr==Napr.RIGTH&&position.left<screenX-WIDTH_SPRITE&&nlf!=LfRg.NORIGTH)
           position.left+=dt* currenthorspeed;
        nlf=LfRg.UP
    }
    fun SpeedBonusEnd()
    {
       if(curBonusTime>= BONUS_MAX_TIME)
       {
            birdspeed=Speed.USUAL
           currentspeed=SPEED_VERTICAL_DEFAULT;
           currenthorspeed=MAX_SPEED_HORIZONTAL;
           curBonusTime=0F;
       }
    }
    fun SpeedUp(){
        curBonusTime=0F;
        birdspeed=Speed.FAST
        currentspeed =350;
        currenthorspeed=350;
    }
    fun SpeedDown(){
        curBonusTime=0F;
        birdspeed=Speed.SLOW
        currentspeed =50;
        currenthorspeed=50;
    }

    fun Shoot()
    {
        shoot=true
        shootAnimation.reset()
    }
    fun Left(koef:Float)
    {
        napr=Napr.LEFT
        //currentspeed=(koef* SPEED_VERTICAL_DEFAULT).toInt();
        currenthorspeed=(koef* MAX_SPEED_HORIZONTAL).toInt();
    }
    fun Right(koef:Float)
    {
        napr=Napr.RIGTH
        //currentspeed=(koef* SPEED_VERTICAL_DEFAULT).toInt();
        currenthorspeed=(koef* MAX_SPEED_HORIZONTAL).toInt();

    }
    fun Just()
    {
        napr=Napr.JUST
    }
    fun Stop()
    {
        move=false
    }
    fun MoveAgain()
    {
        move=true
    }


    /**
     * отрисовка текущего фрейма
     */
    var shootDraw: Int=0;
    fun draw(canvas: Canvas, paint: Paint, viewport: Viewport) {
        if (!shoot) {
            var bOutput: Bitmap
            var degrees = 0f //rotation degree

            val matrix = Matrix()
            bOutput = Bitmap.createBitmap(sprAni.getCurrent(), 0, 0,
                    sprAni.getCurrent().width, sprAni.getCurrent().height, matrix, true)
            if (napr==Napr.LEFT) {
                degrees=-25f;
                matrix.setRotate(degrees)
                bOutput = Bitmap.createBitmap(sprAni.getCurrent(), 0, 0,
                        sprAni.getCurrent().width, sprAni.getCurrent().height, matrix, true)
            }
            if (napr==Napr.RIGTH) {
                degrees=25f;
                matrix.setRotate(degrees)
                bOutput = Bitmap.createBitmap(sprAni.getCurrent(), 0, 0,
                        sprAni.getCurrent().width, sprAni.getCurrent().height, matrix, true)
            }
            if (napr==Napr.JUST)
            {
                degrees=0f;
                matrix.setRotate(degrees)
            }
            canvas.drawBitmap(bOutput, position.left, viewport.convertToDisplay(position), paint)

        }else {canvas.drawBitmap(shootAnimation.getCurrent(), position.left, viewport.convertToDisplay(position), paint)
            shootDraw++;
            if (shootDraw==4)
            {
                shoot=false;
                shootDraw=0;
                sprAni.reset()
            }
        }
    }

}