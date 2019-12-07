package ru.tzhack.facegame.bird

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_bird.*
import ru.tzhack.facegame.R

interface BirdGameControlListener {
    fun onBirdGameOver()
}

/**
 * Фрагмент с игрой "Bird" макет - fragment_bird
 * 1. Реализовать приветственный экран с описанием игры макет - fragment_bird
 * 2. Интегрировать [MlKitHeroListener] для управления игрой
 * 3. Реализовать механизм для получения события об окончании игры
 * 4. Отобразить пользователю диалог "Победа"\"Поражение"
 */
class BirdFragment : Fragment() {

    companion object {
        val TAG: String = BirdFragment::class.java.simpleName

        fun createFragment(): Fragment = BirdFragment()

        private const val ANGLE_MAX_SPEED = 30f
        private const val ANGLE_STOPPED = 3f
    }

    private var game: Game? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bird, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val size = Point()
        requireActivity().windowManager.defaultDisplay.getSize(size)
        game = Game(requireContext(), size, this::onEndGame)
        game_container.addView(game)
    }

    override fun onResume() {
        super.onResume()
        game?.start()
        game?.pause = false
    }

    override fun onPause() {
        super.onPause()
        game?.stop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        game = null
    }

    private fun onEndGame(isWon : Boolean) : Unit{
        val resultDialog = AlertDialog.Builder(this.context)
        if (isWon) {
            resultDialog.setTitle("Победа!")
        } else {
            resultDialog.setTitle("Поражение!")
        }
        resultDialog.setNeutralButton("На стартовую страницу") { dialog : DialogInterface,  id : Int -> {}}
        resultDialog.create().show()
    }
}