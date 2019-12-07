package ru.tzhack.facegame.facetraking.util

import com.google.android.gms.vision.face.Contour
import com.google.firebase.ml.vision.face.FirebaseVisionFace

//TODO: величину дельт, вы должны подобрать, после того, как поиграетесь с камерой
private const val correctSmileProbabilityPercent = 0.0F
private const val correctCloseEyeProbabilityPercent = 0.0F
private const val correctMouthOpenDelta = 0F

private const val correctHeadLeftRotateDelta = 0F
private const val correctHeadRightRotateDelta = 0F

private const val correctHeadBiasDownDelta = 0F
private const val correctHeadBiasUpDelta = 0F

private const val correctEyeBrownMoveDelta = 0F

// For Help:
// https://firebase.google.com/docs/ml-kit/images/examples/face_contours.svg

/**
 * Метод для проверки открытого рта на лице игрока.
 * */
fun FirebaseVisionFace.checkOpenMouthOnFaceAvailable(): Boolean {
//    88-96	Upper lip (bottom)
//    97-105	Lower lip (top)
//    106-116	Upper lip (top)
//    117-125	Lower lip (bottom)

    val upperLipTop = this.getContour(Contour.UPPER_LIP_TOP)
    val lowerLipTop = this.getContour(Contour.LOWER_LIP_TOP)

    val upperLipBottom = this.getContour(Contour.UPPER_LIP_BOTTOM)
    val lowerLipBottom = this.getContour(Contour.LOWER_LIP_BOTTOM)

    println("upper LIP " + upperLipTop.points.size)
    println("upper LIP " +lowerLipTop.points.size)

    println("lower LIP " +upperLipBottom.points.size)
    println("lower LIP " +lowerLipBottom.points.size)

    return false
}

/**
 * Метод для проверки поворота головы игрока налево.
 * */
fun FirebaseVisionFace.checkHeadLeftRotateAvailable(): Boolean {
    //TODO: Реализовать логику обнаружения данного действия.
    return false
}

/**
 * Метод для проверки поворота головы игрока направо.
 * */
fun FirebaseVisionFace.checkHeadRightRotateAvailable(): Boolean {
    //TODO: Реализовать логику обнаружения данного действия.
    return false
}

/**
 * Метод для проверки наклона головы игрока вперед.
 * */
fun FirebaseVisionFace.checkHeadBiasDownAvailable(): Boolean {
    //TODO: Реализовать логику обнаружения данного действия.
    return false
}

/**
 * Метод для проверки наклона головы игрока назад.
 * */
fun FirebaseVisionFace.checkHeadBiasUpAvailable(): Boolean {
    //TODO: Реализовать логику обнаружения данного действия.
    return false
}

/**
 * Метод для проверки наличия улыбки на лице игрока.
 * */
fun FirebaseVisionFace.checkSmileOnFaceAvailable(): Boolean {
    //TODO: Реализовать логику обнаружения данного действия.
    return false
}

/**
 * Метод для проверки подмигивания правым глазом.
 * */
fun FirebaseVisionFace.checkRightEyeCloseOnFaceAvailable(): Boolean {
    //TODO: Реализовать логику обнаружения данного действия.
    //TODO: Не забывай, что это фронтальная камера, а значит все перевернуто...
    return false
}

/**
 * Метод для проверки подмигивания левым глазом.
 * */
fun FirebaseVisionFace.checkLeftEyeCloseOnFaceAvailable(): Boolean {
    //TODO: Реализовать логику обнаружения данного действия.
    //TODO: Не забывай, что это фронтальная камера, а значит все перевернуто...
    return false
}

/**
 * Метод для проверки подмигивания обоими глазами.
 *
 * Задача со звездочкой: Необходимо исключить моргания
 * */
fun FirebaseVisionFace.checkDoubleEyeCloseOnFaceAvailable(): Boolean {
    //TODO: Реализовать логику обнаружения данного действия.
    return false
}

/**
 * Метод для проверки движения обеих бровей на лице игрока.
 * */
fun FirebaseVisionFace.checkDoubleEyeBrownMoveOnFaceAvailable(): Boolean {
    //TODO: Реализовать логику обнаружения данного действия.
    return false
}