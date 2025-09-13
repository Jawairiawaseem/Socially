package com.jawairiawaseem.i221274

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class loginform : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginform)
        val tvsignup1 = findViewById<TextView>(R.id.tvsignup1)
        val backbtn= findViewById<ImageView>(R.id.backbtn)

        val loginbtn = findViewById<Button>(R.id.loginbtn)

        loginbtn.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }

        tvsignup1.setOnClickListener {
            val intent = Intent(this, signupActivity::class.java)
            startActivity(intent)
        }


        backbtn.setOnClickListener {
            finish()
        }

    }


}