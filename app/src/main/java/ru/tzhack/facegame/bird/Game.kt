package ru.tzhack.facegame.bird

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.os.SystemClock
import android.view.SurfaceView
import androidx.core.content.ContextCompat
import ru.tzhack.facegame.R
import ru.tzhack.facegame.bird.gameobj.*
import ru.tzhack.facegame.data.model.FaceEmoji


/**
 * Модель управления игрой
 * 1. Инициализация игровых объектов
 * 2. Для отладки обработки действий пользователя реализовать считивание тапов с экрана
 * 3. Обработка ввода пользователя
 *          - Движения по горизонали
 *          - Выстрел
 * 4. Оповещение о завершении игры
 *
 */
@SuppressLint("ViewConstructor")
class Game(
        context: Context,
        private val size: Point,
        private val resultGame: (Boolean) -> Unit
        ) : SurfaceView(context),
    Runnable {

    private var playing = false
    var pause = true
    private var thread: Thread? = null
    private val viewport : Viewport

    private var canvas: Canvas = Canvas()
    private val paint: Paint = Paint()
    val bird: Bird = Bird(context, (size.x).toFloat())
    val blocks = Block.generate(context, size.x.toFloat(),20)
    val bonuses = arrayListOf<Bonus>()
    companion object {
        // выстрел не чаще
        private const val SHOT_DEPOUNCE = 2000
        private const val COORD_END_GAME = 7000F

    }

    //val blocks : List<Block>
    //val bonus : Bonus
    //val bullets : List<Bullet>
    val finish: Finish
    val gameToolbar: GameToolbar

    init {
        paint.textSize = 50f
        viewport =  Viewport(this, size.x.toFloat(), size.y.toFloat())
        //blocks = Block.generate(context, size.x.toFloat(),20)
        //bonus = Bonus.create()
        //bullets = arrayListOf<Bullet>()
        gameToolbar = GameToolbar(this.context, size.x.toFloat())
        finish = Finish(COORD_END_GAME, size.x.toFloat(), this.context)
        Bonus.init(context)
    }

    private val backgroundColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)


    fun start() {
        if (!playing) {
            playing = true
            thread = Thread(this)
            thread!!.start()
        }
    }

    fun stop() {
        if (playing) {
            playing = false
            thread?.join()
            thread = null
        }
    }

    override fun run() {
        var lastFrameTime = SystemClock.uptimeMillis()
        while (playing) {
            val time = SystemClock.uptimeMillis()
            val deltaTime = (time - lastFrameTime) / 1000f
            lastFrameTime = time

            if (!pause) {
                update(deltaTime)
            }

            draw()

//            val timeThisFrame = SystemClock.uptimeMillis() - time
//            if (timeThisFrame >= 1) {
//                val fps = 1000 / timeThisFrame
//                Log.d("thread", "fps:$fps")
//            }
        }
    }

    public fun action (action : FaceEmoji) {
        when (action) {
            FaceEmoji.SMILE -> {

            }
            FaceEmoji.HEAD_ROTATE_LEFT -> {

            }
            FaceEmoji.HEAD_ROTATE_RIGHT ->  {

            }
        }
    }

    /**
     *  Обновление состояния игры
     *
     *  Метод реализует
     *  1. Обновление игровых объектов
     *  2. Обработка столкновения
     *  3. Проверка завершения игры
     *
     *  @param dt - прошло секунт после обработки кадра
     */
    private fun update(dt: Float) {
        bird.update(dt)
        gameToolbar.update(dt)
        viewport.centreCamera(bird.position)

        if(bird.position.top > Bonus.generateWhenPositionY)
        {

            bonuses+=Bonus.create(bird.position, size.x,size.y)
        }
        if (finish.isCollision(bird.position.top) ) {
            playing = false
            resultGame(true)
        }
        for (bonus in bonuses)
        {
            bonus.update(dt)
        }

    }

    /**
     *  Отрисовка игровых объектов
     */
    private fun draw() {
        if (holder.surface.isValid) {
            val lockCanvas = holder.lockCanvas()
            if (lockCanvas != null) {
                canvas = lockCanvas

                canvas.drawColor(backgroundColor)

                for (block in blocks)
                {
                    block.draw(canvas,paint,viewport)
                }
                for (bonus in bonuses) {

                    bonus.draw(canvas, paint, viewport, context)
                }
                finish.draw(canvas, paint, viewport)
                gameToolbar.draw(canvas, paint)
                bird.draw(canvas, paint, viewport)

                holder.unlockCanvasAndPost(canvas)
            }
        }
    }

    private fun printEndMessage () {

    }
}