package com.jawairiawaseem.i221274

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.jvm.java

class MainActivity2 : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val loginbtn = findViewById<Button>(R.id.loginbtn)
        val gotologinform = findViewById<TextView>(R.id.gotologinform)
        val signupText = findViewById<TextView>(R.id.tvsignup)


            loginbtn.setOnClickListener {
                val intent = Intent(this, loginform::class.java)
                startActivity(intent)
        }
            gotologinform.setOnClickListener {
            val intent = Intent(this, loginform::class.java)
            startActivity(intent)
        }

            signupText.setOnClickListener {
                val intent = Intent(this, signupActivity::class.java)
                startActivity(intent)
        }
    }
}