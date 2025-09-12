package com.jawairiawaseem.i221274

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // your splash layout

        android.os.Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity2::class.java))
            finish() // close splash so user cannot go back to it
        }, 2000) // delay in ms
    }

}