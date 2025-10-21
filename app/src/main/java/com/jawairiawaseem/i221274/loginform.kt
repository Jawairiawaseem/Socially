package com.jawairiawaseem.i221274

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.jawairiawaseem.i221274.data.AuthRepository
import kotlinx.coroutines.launch

class loginform : AppCompatActivity() {

    private val authRepo = AuthRepository()

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginform)

        // Views
        val tvsignup1: TextView = findViewById(R.id.tvsignup1)
        val backbtn: ImageView = findViewById(R.id.backbtn)
        val loginbtn: MaterialButton = findViewById(R.id.loginbtn)

        val tilEmail: TextInputLayout = findViewById(R.id.tilEmail)
        val etEmail: TextInputEditText = findViewById(R.id.etEmail)
        val tilPassword: TextInputLayout = findViewById(R.id.tilPassword)
        val etPassword: TextInputEditText = findViewById(R.id.etPassword)

        // Actions
        loginbtn.setOnClickListener {
            val email = etEmail.text?.toString()?.trim().orEmpty()
            val pass = etPassword.text?.toString().orEmpty()

            // simple validation + errors on TextInputLayout
            var hasError = false
            if (email.isEmpty()) {
                tilEmail.error = "Email required"
                hasError = true
            } else {
                tilEmail.error = null
            }

            if (pass.isEmpty()) {
                tilPassword.error = "Password required"
                hasError = true
            } else {
                tilPassword.error = null
            }

            if (hasError) return@setOnClickListener

            loginbtn.isEnabled = false

            lifecycleScope.launch {
                val result = authRepo.loginEmail(email, pass)
                result.onSuccess {
                    // go to your Splash so it decides Home vs ProfileSetup
                    startActivity(Intent(this@loginform, MainActivity::class.java))
                    finishAffinity()
                }.onFailure {
                    Toast.makeText(
                        this@loginform,
                        it.localizedMessage ?: "Login failed",
                        Toast.LENGTH_LONG
                    ).show()
                    loginbtn.isEnabled = true
                }
            }
        }

        tvsignup1.setOnClickListener {
            startActivity(Intent(this@loginform, signupActivity::class.java))
        }

        backbtn.setOnClickListener { finish() }
    }
}
