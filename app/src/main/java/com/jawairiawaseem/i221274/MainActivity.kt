package com.jawairiawaseem.i221274

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.jawairiawaseem.i221274.data.AuthRepository
import kotlinx.coroutines.launch
import java.util.logging.Handler
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {
    private val authRepo = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            if (!authRepo.isLoggedIn()) {
                startActivity(Intent(this@MainActivity, MainActivity2::class.java))
                finish()
                return@launch
            }

            val completed = try { authRepo.fetchProfileCompleted() } catch (_: Exception) { false }

            val next = if (completed) HomeScreen::class.java else EditProfile::class.java
            startActivity(Intent(this@MainActivity, next))
            finish()
        }
    }

}