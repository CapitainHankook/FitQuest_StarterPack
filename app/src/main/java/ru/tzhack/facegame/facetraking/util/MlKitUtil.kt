package ru.tzhack.facegame.facetraking.util

import com.google.android.gms.vision.face.Contour
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour

//TODO: величину дельт, вы должны подобрать, после того, как поиграетесь с камерой
private const val correctSmileProbabilityPercent = 0.5F
private const val correctCloseEyeProbabilityPercent = 0.7F
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
    //TODO: Реализовать логику обнаружения данного действия.
    val topPointsLip = getContour(FirebaseVisionFaceContour.LOWER_LIP_TOP).points[4].y.toInt();
    val bottonPointsLip = getContour(FirebaseVisionFaceContour.UPPER_LIP_BOTTOM).points[4].y.toInt();
    if(topPointsLip - bottonPointsLip >= 30)
    {
        return true;
    }
    return false
}

/**
 * Метод для проверки поворота головы игрока налево.
 * */
fun FirebaseVisionFace.checkHeadLeftRotateAvailable(): Boolean {
    //TODO: Реализовать логику обнаружения данного действия.
    val noseCentre = getContour(FirebaseVisionFaceContour.NOSE_BRIDGE).points[1].x.toInt();
    val leftFace = getContour(FirebaseVisionFaceContour.FACE).points[9].x.toInt();

    if(leftFace - noseCentre <= 20)
    {
        return true;
    }
    return false
}

/**
 * Метод для проверки поворота головы игрока направо.
 * */
fun FirebaseVisionFace.checkHeadRightRotateAvailable(): Boolean {
    //TODO: Реализовать логику обнаружения данного действия.

    val noseCentre = getContour(FirebaseVisionFaceContour.NOSE_BRIDGE).points[1].x.toInt();
    val rightFace = getContour(FirebaseVisionFaceContour.FACE).points[27].x.toInt();

    if(noseCentre - rightFace <= 20)
    {
        return true;
    }
    return false
}

/**
 * Метод для проверки наклона головы игрока вперед.
 * */
fun FirebaseVisionFace.checkHeadBiasDownAvailable(): Boolean {
    //TODO: Реализовать логику обнаружения данного действия.
    val topPointsNose = getContour(FirebaseVisionFaceContour.NOSE_BOTTOM).points[1].y.toInt();
    val bottonPointsNose = getContour(FirebaseVisionFaceContour.NOSE_BRIDGE).points[1].y.toInt();
    if(topPointsNose - bottonPointsNose <= 0)
    {
        return true;
    }
    return false
}

/**
 * Метод для проверки наклона головы игрока назад.
 * */
fun FirebaseVisionFace.checkHeadBiasUpAvailable(): Boolean {
    //TODO: Реализовать логику обнаружения данного действия.
    val topPointsNose = getContour(FirebaseVisionFaceContour.NOSE_BRIDGE).points[1].y.toInt();
    val bottonPointsNose = getContour(FirebaseVisionFaceContour.NOSE_BRIDGE).points[0].y.toInt();
    if(topPointsNose - bottonPointsNose <= 35)
    {
        return true;
    }
    return false
}

/**
 * Метод для проверки наличия улыбки на лице игрока.
 * */
fun FirebaseVisionFace.checkSmileOnFaceAvailable(): Boolean {
    return smilingProbability <= correctSmileProbabilityPercent
}

/**
 * Метод для проверки подмигивания правым глазом.
 * */
fun FirebaseVisionFace.checkRightEyeCloseOnFaceAvailable(): Boolean {
    return rightEyeOpenProbability <= correctCloseEyeProbabilityPercent
}

/**
 * Метод для проверки подмигивания левым глазом.
 * */
fun FirebaseVisionFace.checkLeftEyeCloseOnFaceAvailable(): Boolean {
    if (leftEyeOpenProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY &&
        leftEyeOpenProbability <= correctCloseEyeProbabilityPercent)
        return true
    else return false
}

/**
 * Метод для проверки подмигивания обоими глазами.
 *
 * Задача со звездочкой: Необходимо исключить моргания
 * */
fun FirebaseVisionFace.checkDoubleEyeCloseOnFaceAvailable(): Boolean {
    return leftEyeOpenProbability <= correctCloseEyeProbabilityPercent &&
            rightEyeOpenProbability <= correctCloseEyeProbabilityPercent
}

/**
 * Метод для проверки движения обеих бровей на лице игрока.
 * */
fun FirebaseVisionFace.checkDoubleEyeBrownMoveOnFaceAvailable(): Boolean {
    //TODO: Реализовать логику обнаружения данного действия.
    val leftEyebrow = getContour(FirebaseVisionFaceContour.LEFT_EYEBROW_BOTTOM).points[3].y.toInt();
    val rigthEyebrow = getContour(FirebaseVisionFaceContour.RIGHT_EYEBROW_BOTTOM).points[3].y.toInt();
    val leftEye = getContour(FirebaseVisionFaceContour.LEFT_EYE).points[5].y.toInt();
    val rigthEye = getContour(FirebaseVisionFaceContour.RIGHT_EYE).points[3].y.toInt();

    if(leftEye - leftEyebrow >= 40 && rigthEye - rigthEyebrow>=40)
    {
        return true;
    }
    return false
}