package ru.tzhack.facegame.bird

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.os.SystemClock
import android.view.MotionEvent
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
    private val viewport: Viewport

    private var canvas: Canvas = Canvas()
    private val paint: Paint = Paint()

    private var timeWidhoutShot = 0f

    companion object {
        // выстрел не чаще
        private const val SHOT_DEPOUNCE = 2f
        private const val COORD_END_GAME = 12200F

    }

    private val bird: Bird = Bird(context, (size.x).toFloat())
    private var blocks: ArrayList<Block>
    private val bonuses = arrayListOf<Bonus>()
    private var bullets: ArrayList<Bullet>
    private val finish: Finish
    private val gameToolbar: GameToolbar

    init {
        paint.textSize = 50f
        viewport = Viewport(this, size.x.toFloat(), size.y.toFloat())
        blocks = Block.generate(context, size.x.toFloat(), 25)
        Bonus.init(context)
        bullets = arrayListOf<Bullet>()
        gameToolbar = GameToolbar(this.context, size.x.toFloat())
        finish = Finish(COORD_END_GAME, size.x.toFloat(), this.context)
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
        }
    }

    fun action(action: FaceEmoji) {
        when (action) {
            FaceEmoji.SMILE -> onSmile()
            FaceEmoji.HEAD_ROTATE_LEFT -> onHeadRotateLeft(0f)
            FaceEmoji.HEAD_ROTATE_RIGHT -> onHeadRotateRight(0f)
        }
    }

    @Synchronized
    private fun onSmile() {
        if (timeWidhoutShot >= SHOT_DEPOUNCE &&  gameToolbar.countShots > 0) {
            bullets.add(Bullet.create(context, bird.position))
            timeWidhoutShot = 0f
            bird.Shoot()
            gameToolbar.countShots--
        }

    }

    @Synchronized
    public fun onHeadRotateLeft(koef: Float) {
        bird.Left()
    }

    @Synchronized
    public fun onHeadRotateRight(koef: Float) {
        bird.Right()
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
    @Synchronized
    private fun update(dt: Float) {
        timeWidhoutShot += dt

        bird.update(dt, blocks)

        gameToolbar.update(dt)
        viewport.centreCamera(bird.position)
        val stayBullet = arrayListOf<Bullet>()
        for (bullet in bullets) {
            if (!bullet.destroyed) {
                stayBullet.add(bullet)
                bullet.update(dt)
            }
        }

        if(bird.position.top > Bonus.generateWhenPositionY) {
            bonuses+=Bonus.create(bird.position, size.x,size.y)
        }

        for (bonus in bonuses) {
            bonus.update(dt)
        }


        bullets = stayBullet
        for (bullet in bullets) {
            val block = findCollisionBlock(bullet)
            if (block !== null) {
                blocks.remove(block)
                bullet.explosioned = true
            }
        }
        var found: Boolean = false
        for (block in blocks) {
            if (block.checkOnCollision(bird.position)) {
                bird.Stop()
                found = true
            }
        }

        val catchBonus = findCollisionBonuse()
        if (catchBonus!== null) {
            bonuses.remove(catchBonus)

            when(catchBonus.type) {
                BonusType.SHOT -> onBonusShot()
                BonusType.SPEED_DOWN ->onBonusSpeedDown()
                BonusType.SPEED_UP -> onBonusSpeedUp()
                BonusType.TIME -> onBonusTime()
            }
        }

        if (!found)
            bird.MoveAgain()

        if (finish.isCollision(bird.position.top)) {
            if (bird.position.top > Bonus.generateWhenPositionY) {

                bonuses += Bonus.create(bird.position, size.x, size.y)
            }
            if (finish.isCollision(bird.position.top)) {
                playing = false
                resultGame(true)
            }
        }
        if (gameToolbar.getTime() <= 0) {
            playing = false
            resultGame(false)
        }

        for (bonus in bonuses) {
            bonus.update(dt)
        }
    }

    private fun onBonusShot() {
        gameToolbar.countShots++
    }

    private fun onBonusTime() {
        gameToolbar.catchupTimeBonus()
    }
    private fun onBonusSpeedUp() {
        //gameToolbar.catchupTimeBonus()
        bird.SpeedUp()
    }
    private fun onBonusSpeedDown() {
        //gameToolbar.catchupTimeBonus()
        bird.SpeedDown()
    }



    private fun findCollisionBlock(bullet: Bullet): Block? {
        for (block in blocks) {
            if (block.checkOnCollision(bullet.position)) return block

        }

        return null
    }

    private fun findCollisionBonuse(): Bonus? {
        for (bonus in bonuses) {
            if (bonus.checkOnCollision(bird.position)) return bonus

        }

        return null
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

                for (block in blocks) {
                    if (viewport.isVisible(block.position))
                         block.draw(canvas, paint, viewport)
                }

                for (bullet in bullets) {
                    if (viewport.isVisible(bullet.position))
                        bullet.draw(canvas, paint, viewport)
                }

                for (block in blocks) {
                    if (viewport.isVisible(block.position))
                        block.draw(canvas,paint,viewport)
                }

                for (bonus in bonuses) {
                    if (viewport.isVisible(bonus.position))
                    bonus.draw(canvas, paint, viewport, context)
                }
                if (viewport.isVisible(finish.positionY))
                    finish.draw(canvas, paint, viewport)

                gameToolbar.draw(canvas, paint)
                bird.draw(canvas, paint, viewport)

                holder.unlockCanvasAndPost(canvas)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { e ->
            var X: Float = e.getX() ?: 0F
            var Y: Float = e.getY() ?: 0F

            if (e.actionMasked == MotionEvent.ACTION_DOWN) {
                if (size.y / 2 > Y) {
                    onSmile()
                } else if ((bird.position.left >= X))
                    onHeadRotateLeft(0f)
                else
                    onHeadRotateRight(0f)


            } else {
                bird.Just()
            }
        }
        return true
    }
}