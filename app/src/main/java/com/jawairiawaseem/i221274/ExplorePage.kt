package com.jawairiawaseem.i221274

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ExplorePage : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_explore_page)


        val likesiv = findViewById<ImageView>(R.id.likesiv)
        val homeiv = findViewById<ImageView>(R.id.homeiv)

        val createiv = findViewById<ImageView>(R.id.createiv)

        val profileiv = findViewById<ImageView>(R.id.profileiv)

        likesiv.setOnClickListener {
            val intent = Intent(this, LikesPage::class.java)
            startActivity(intent)
        }
        homeiv.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }

        createiv.setOnClickListener {
            val intent = Intent(this, CreatePost::class.java)
            startActivity(intent)

        }
        profileiv.setOnClickListener {
            val intent = Intent(this, profilePage::class.java)
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}