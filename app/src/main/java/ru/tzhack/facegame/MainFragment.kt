package ru.tzhack.facegame

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

interface StartGameListener {
    fun onStartClick()
}

//TODO: ЗАДАНИЕ #2
/**
 * Задание №2.
 *
 * Создание Logo Screen.
 *
 * Здесь реализуем интерфейс, как на макете.
 *
 * */

class MainFragment : Fragment() {

    companion object {
        val TAG: String = MainFragment::class.java.simpleName

        fun createFragment() =
            MainFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    private var listener: StartGameListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO: используйте Glide, отобразите с его помощью .gif 'R.drawable.logo_anim'
        //TODO: также, нужно сделать Crop для изображения


        //TODO: при нажатии на экран, необходимо стартовать игру 'listener?.onStartClick()'
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {
            is StartGameListener -> listener = context
        }
    }
}