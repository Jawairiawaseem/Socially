package com.jawairiawaseem.i221274

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LikesPage : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_likes_page)

        val homeiv = findViewById<ImageView>(R.id.homeiv)
        val exploreiv = findViewById<ImageView>(R.id.exploreiv)
        val createiv = findViewById<ImageView>(R.id.createiv)

        val profileiv = findViewById<ImageView>(R.id.profileiv)

        val youtv = findViewById<TextView>(R.id.youtv)

        youtv.setOnClickListener {
            val intent = Intent(this, LikesPage2::class.java)
            startActivity(intent)
        }


        homeiv.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }
        exploreiv.setOnClickListener {
            val intent = Intent(this, ExplorePage::class.java)
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