package com.example.newsapps

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import com.example.newsapps.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY_MS = 3000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            initialCheck()
        }, SPLASH_DELAY_MS)
    }

    private fun initialCheck() {
       if (isInternetAvailable()) {
            startActivity(Intent(this@SplashActivity, DashboardNewsActivity::class.java))
        } else {
            showBadConnectionDialog()
        }
        finish()
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showBadConnectionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Bad Connection")
            .setMessage("Try again when you have a better connection.")
            .setPositiveButton("OK") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                finish()
            }
        .setCancelable(false)
        .show()
}
}
