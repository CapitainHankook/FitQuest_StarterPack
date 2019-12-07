package ru.tzhack.facegame.facetraking.mlkit

import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.otaliastudios.cameraview.frame.Frame
import ru.tzhack.facegame.data.model.FaceEmoji
import ru.tzhack.facegame.data.model.FaceEmoji.*
import ru.tzhack.facegame.facetraking.mlkit.listener.MlKitDebugListener
import ru.tzhack.facegame.facetraking.mlkit.listener.MlKitEmojiListener
import ru.tzhack.facegame.facetraking.mlkit.listener.MlKitHeroListener
import ru.tzhack.facegame.facetraking.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

const val maxHeadZ = 25F
const val minHeadZ = maxHeadZ * -1

const val framesWithoutFaceGap = 10

object MlKitEngine {

    private var faceDetector: FirebaseVisionFaceDetector? = null

    private var framesWithoutFace = AtomicInteger(0)
    private var faceWas = AtomicBoolean(false)

    private var analyzing = AtomicBoolean(false)

    fun initMlKit() {
        val options = FirebaseVisionFaceDetectorOptions.Builder()
                .enableTracking()
                .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                .build()

        faceDetector = FirebaseVision.getInstance().getVisionFaceDetector(options)
    }

    fun extractDataFromFrame(
            frame: Frame,
            currentEmoji: FaceEmoji? = null,
            listenerHero: MlKitHeroListener? = null,
            listenerEmoji: MlKitEmojiListener? = null,
            debugListener: MlKitDebugListener? = null
    ) {
        if(analyzing.get()) return
        analyzing.set(true)

        val frameSize = frame.size
        var faceFound = false
        getFaceDetector().detectInImage(frame.getVisionImageFromFrame())
                .addOnSuccessListener { faces ->
                    if (faces.isNotEmpty()) {
                        val currentFace = faces[0]
                        if (currentFace.getContour(FirebaseVisionFaceContour.ALL_POINTS).points.isNotEmpty()) {
                            if (currentEmoji == null) listenerHero?.let { calculateHeroActions(currentFace, it) }
                            else listenerEmoji?.let { calculateEmojiActions(currentFace, currentEmoji, it) }
                            debugListener?.onDebugInfo(frameSize, currentFace)
                            faceFound = true
                            faceWas.set(true)
                            framesWithoutFace.set(0)
                        }
                    }
                }
                .addOnFailureListener {
                    listenerHero?.onError(it)
                }
        if (faceWas.get()) {
            if (!faceFound) framesWithoutFace.incrementAndGet()
            if (framesWithoutFace.get() > framesWithoutFaceGap) {
                debugListener?.onDebugInfo(frameSize, null)
                framesWithoutFace.set(0)
                faceWas.set(false)
            }
        }

        analyzing.set(false)
    }

    private fun calculateHeroActions(face: FirebaseVisionFace, listener: MlKitHeroListener) {
        listener.onHeroHorizontalAnim(face.headEulerAngleZ)

        if (face.checkSmileOnFaceAvailable()) listener.onHeroSuperPowerAnim()
        if (face.checkRightEyeCloseOnFaceAvailable()) listener.onHeroRightEyeAnim()
        if (face.checkLeftEyeCloseOnFaceAvailable()) listener.onHeroLeftEyeAnim()

        if (face.checkDoubleEyeCloseOnFaceAvailable()) listener.onHeroDoubleEyeAnim()

        if (face.checkOpenMouthOnFaceAvailable()) listener.onHeroMouthOpenAnim()
    }

    private fun calculateEmojiActions(
            face: FirebaseVisionFace,
            currentEmoji: FaceEmoji,
            listener: MlKitEmojiListener
    ) {
        when (currentEmoji) {
            // Зажмуривание
            DOUBLE_EYE_CLOSE -> if (face.checkDoubleEyeCloseOnFaceAvailable()) listener.onEmojiObtained(
                    DOUBLE_EYE_CLOSE
            )

            // Левый глаз закрыт
            LEFT_EYE_CLOSE -> if (face.checkLeftEyeCloseOnFaceAvailable()) listener.onEmojiObtained(LEFT_EYE_CLOSE)

            //Правый глаз закрыт
            RIGHT_EYE_CLOSE -> if (face.checkRightEyeCloseOnFaceAvailable()) listener.onEmojiObtained(
                    RIGHT_EYE_CLOSE
            )

            // Движения обеими бровями
            DOUBLE_EYEBROWN_MOVE -> if (face.checkDoubleEyeBrownMoveOnFaceAvailable()) listener.onEmojiObtained(
                    DOUBLE_EYEBROWN_MOVE
            )

            // Улыбка
            SMILE -> if (face.checkSmileOnFaceAvailable()) listener.onEmojiObtained(SMILE)

            // Открыт рот
            MOUTH_OPEN -> if (face.checkOpenMouthOnFaceAvailable()) listener.onEmojiObtained(MOUTH_OPEN)

            // Повороты головы влево
            HEAD_ROTATE_LEFT -> if (face.checkHeadLeftRotateAvailable()) listener.onEmojiObtained(HEAD_ROTATE_LEFT)

            // Повороты головы вправо
            HEAD_ROTATE_RIGHT -> if (face.checkHeadRightRotateAvailable()) listener.onEmojiObtained(HEAD_ROTATE_RIGHT)

            // Повороты головы вперед
            HEAD_BIAS_DOWN -> if (face.checkHeadBiasDownAvailable()) listener.onEmojiObtained(HEAD_BIAS_DOWN)

            // Повороты головы назад
            HEAD_BIAS_UP -> if (face.checkHeadBiasUpAvailable()) listener.onEmojiObtained(HEAD_BIAS_UP)

            // Наклон головы влево
            HEAD_BIAS_LEFT -> if (face.headEulerAngleZ <= minHeadZ) listener.onEmojiObtained(HEAD_BIAS_LEFT)

            // Наклон головы вправо
            HEAD_BIAS_RIGHT -> if (face.headEulerAngleZ >= maxHeadZ) listener.onEmojiObtained(HEAD_BIAS_RIGHT)
        }
    }

    private fun getFaceDetector(): FirebaseVisionFaceDetector = faceDetector
            ?: throw Exception("MlKit is not configured! Call first 'initMlKit()' method.")
}