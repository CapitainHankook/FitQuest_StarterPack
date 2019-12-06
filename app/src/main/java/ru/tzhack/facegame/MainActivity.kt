package ru.tzhack.facegame

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.tzhack.facegame.facetraking.FaceGameOverListener

private const val CAMERA_PERMISSION_REQUEST_CODE = 101


//TODO: ЗАДАНИЕ #2
/**
 * Задание №2.
 *
 * Создание Logo Screen.
 *
 * Данная Activity будет отвечать за всю координацию в нашем приложении.
 * Все переключения фрагментов будут выполняться именно здесь.
 *
 * В этом же месте, необходимо будет запросить Permission на камеру.
 *
 *
 * */

class MainActivity : AppCompatActivity(), /*BirdGameControlListener,*/ FaceGameOverListener, StartGameListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showMainFragment()
    }

    override fun onStartClick() {
        showGameFragment()
    }

    //TODO: это делают ребята с игрой
//    override fun onBirdGameOver() {
//        showMainFragment()
//    }

    override fun onFaceGameOverPositive() {
        showBonusGameFragment()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showGameFragment()
            } else {
                //TODO: Если не дали разрешение - закрываем игру
            }
        }
    }

    private fun hasCameraPermissions(): Boolean {
        //TODO: ContextCompat.checkSelfPermission(...)
        //TODO: PackageManager.PERMISSION_GRANTED
        return false
    }

    private fun showGameFragment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (hasCameraPermissions()) {
                //TODO: fragmentManager -> replace FaceTrackingFragment
            } else {
                //TODO: запрос разрешения на камеру
                //TODO: requestPermissions(...)
            }
        } else {
            //TODO: fragmentManager -> replace FaceTrackingFragment
        }
    }

    private fun showMainFragment() {
        //TODO: fragmentManager -> replace MainFragment (Logo Screen)
    }

    private fun showBonusGameFragment() {
        //TODO: fragmentManager -> replace BirdFragment
    }
}
