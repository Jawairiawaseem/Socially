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
import kotlin.jvm.java

class HomeScreen : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)

        val exploreiv = findViewById<ImageView>(R.id.exploreiv)
        val direct = findViewById<ImageView>(R.id.direct)
        val profileiv = findViewById<ImageView>(R.id.profileiv)
        val likesiv = findViewById<ImageView>(R.id.likesiv)
        val createiv = findViewById<ImageView>(R.id.createiv)
        val camiv = findViewById<ImageView>(R.id.camiv)
        val story5 = findViewById<ImageView>(R.id.story5)
        val postpfp =findViewById<ImageView>(R.id.postpfp)

        postpfp.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }
        story5.setOnClickListener {
            val intent = Intent(this, CraigStory::class.java)
            startActivity(intent)
        }


        camiv.setOnClickListener {
            val intent = Intent(this, takePhoto::class.java)
            startActivity(intent)
        }

        createiv.setOnClickListener {
            val intent = Intent(this, CreatePost::class.java)
            startActivity(intent)
        }
        likesiv.setOnClickListener {
            val intent = Intent(this, LikesPage::class.java)
            startActivity(intent)
        }

        profileiv.setOnClickListener {
            val intent= Intent(this, profilePage::class.java)
            startActivity(intent)
        }
        direct.setOnClickListener {
            val intent= Intent(this, directMessages::class.java)
            startActivity(intent)
        }
         exploreiv.setOnClickListener {
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