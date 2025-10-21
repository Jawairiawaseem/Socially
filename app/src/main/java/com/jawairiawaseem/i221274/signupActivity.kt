package com.jawairiawaseem.i221274

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.jawairiawaseem.i221274.data.AuthRepository
import kotlinx.coroutines.launch


class signupActivity : AppCompatActivity() {

    private val authRepo = AuthRepository()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        val backbtn= findViewById<ImageView>(R.id.backbtn)
        backbtn.setOnClickListener {
            finish()
        }
        val email= findViewById<EditText>(R.id.email)
        val password= findViewById<EditText>(R.id.password)
        val btnsignup= findViewById<Button>(R.id.btnsignup)

        btnsignup.setOnClickListener {
            val email = email.text.toString()
            val pass = password.text.toString()

            if (email.isBlank() || pass.length < 6) {
                Toast.makeText(this, "Enter email & 6+ char password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                val result = authRepo.signUpEmail(email, pass)
                result.onSuccess {
                    // New user goes straight to Profile Setup
                    startActivity(android.content.Intent(this@signupActivity, EditProfile::class.java))
                    finishAffinity()
                }.onFailure {
                    Toast.makeText(this@signupActivity, it.localizedMessage ?: "Signup failed", Toast.LENGTH_LONG).show()
                }
            }
        }







        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}