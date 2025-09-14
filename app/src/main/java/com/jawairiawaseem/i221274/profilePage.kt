package com.jawairiawaseem.i221274

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class profilePage : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_page)
        val editProfilebtn =findViewById<Button>(R.id.editProfilebtn)
        val h1=findViewById<ImageView>(R.id.h1)
        val h2=findViewById<ImageView>(R.id.h2)
        val h3=findViewById<ImageView>(R.id.h3)

        val homeiv = findViewById<ImageView>(R.id.homeiv)
        homeiv.setOnClickListener {
            val intent= Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }
        val exploreiv = findViewById<ImageView>(R.id.exploreiv)
        exploreiv.setOnClickListener {
            val intent = Intent(this, ExplorePage::class.java)
            startActivity(intent)
        }

        h1.setOnClickListener {
            val intent= Intent(this, highlight::class.java)
            startActivity(intent)
        }
        h2.setOnClickListener {
            val intent= Intent(this, highlight::class.java)
            startActivity(intent)
        }

        h3.setOnClickListener {
            val intent= Intent(this, highlight::class.java)
            startActivity(intent)
        }
        editProfilebtn.setOnClickListener {
            val intent= Intent(this, EditProfile::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}