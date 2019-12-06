package ru.tzhack.facegame.welcome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

//TODO: ЗАДАНИЕ #1
/**
 * Задание №1.
 *
 * Создание Splash Screen'a.
 *
 * Splash Screen - отображается пользователю во время "холодного"
 *                 запуска приложения (первичная инициализация).
 *
 * Т.к. нам инициализировать нечего, будем имитировать процесс
 * инициализации приложения (пусть будет 500-700 мс).
 *
 * 1. Изучить объект [android.os.Handler]. Разобраться, как с его
 *    помощью "заставить" приложение немного подождать, не блокируя
 *    главный поток приложения.
 * */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: Handler для запуска отложенной задачи - открытие MainActivity.
    }
}
