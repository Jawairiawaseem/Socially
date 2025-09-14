package com.jawairiawaseem.i221274

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeScreen : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)

        val explore = findViewById<ImageView>(R.id.explore)
        val direct = findViewById<ImageView>(R.id.direct)
        val profileiv = findViewById<ImageView>(R.id.profileiv)

        profileiv.setOnClickListener {
            val intent= Intent(this, profilePage::class.java)
            startActivity(intent)
        }
        direct.setOnClickListener {
            val intent= Intent(this, directMessages::class.java)
            startActivity(intent)
        }
         explore.setOnClickListener {
             val intent= Intent(this, ExplorePage::class.java)
             startActivity(intent)
         }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}