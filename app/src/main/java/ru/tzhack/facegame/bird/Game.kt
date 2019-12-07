package ru.tzhack.facegame.bird

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.os.SystemClock
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.SurfaceView
import androidx.core.content.ContextCompat
import ru.tzhack.facegame.R
import ru.tzhack.facegame.bird.gameobj.*


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
        private val size: Point
        ) : SurfaceView(context),
    Runnable {

    private var playing = false
    var pause = true
    private var thread: Thread? = null
    private val viewport : Viewport

    private var canvas: Canvas = Canvas()
    private val paint: Paint = Paint()
    var bird: Bird = Bird(context, (size.x).toFloat());

    companion object {
        // выстрел не чаще
        private const val SHOT_DEPOUNCE = 2000

    }

    //val bird : Bird
    val blocks : List<Block>
    val bonuses : List<Bonus>
    val bullets : List<Bullet>
    val finish: Finish
    val gameToolbar: GameToolbar

    init {
        viewport =  Viewport(this, size.x.toFloat(), size.y.toFloat())
        blocks = arrayListOf<Block>()
        bonuses = arrayListOf<Bonus>()
        bullets = arrayListOf<Bullet>()
        gameToolbar = GameToolbar(this.context)
        finish = Finish(500f, 1000f, this.context)
        viewport.y = 1000f
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
        finish.update()
        bird.update(dt)
        viewport.centreCamera(bird.position)
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

                finish.draw(canvas, paint, viewport)
                bird.draw(canvas, paint, viewport)

                holder.unlockCanvasAndPost(canvas)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { e->
            var X:Float= e.getX() ?: 0F;
            var Y:Float=e.getY() ?: 0F;

            if (e.actionMasked==MotionEvent.ACTION_DOWN) {
                if ((bird.position.left>=X))
                    bird.Left();
                else
                    bird.Right();
            } else {
                bird.Just()
            }
                return super.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }
}