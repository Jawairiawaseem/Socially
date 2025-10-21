package com.jawairiawaseem.i221274

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.jawairiawaseem.i221274.data.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream

class EditProfile : AppCompatActivity() {

    private val authRepo = AuthRepository()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var ivProfile: ImageView
    private lateinit var tvChangePhoto: TextView
    private lateinit var tvEmailValue: TextView
    private lateinit var canceltv: TextView
    private lateinit var donetv: TextView
    private lateinit var etName: EditText
    private lateinit var etUsername: EditText

    private var selectedPhotoBase64: String? = null

    // Image picker (gallery)
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            // Display
            ivProfile.setImageURI(uri)
            // Convert to Base64 bytes for Realtime DB
            lifecycleScope.launch {
                selectedPhotoBase64 = withContext(Dispatchers.IO) { uriToBase64(uri) }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Bind views
        ivProfile = findViewById(R.id.ivProfile)
        tvChangePhoto = findViewById(R.id.tvChangePhoto)
        tvEmailValue = findViewById(R.id.tvEmailValue)
        canceltv = findViewById(R.id.canceltv)
        donetv = findViewById(R.id.donetv)
        etName = findViewById(R.id.etName)
        etUsername = findViewById(R.id.etUsername)

        // Show current user's email
        tvEmailValue.text = auth.currentUser?.email.orEmpty()

        // Pick image from gallery
        tvChangePhoto.setOnClickListener {
            pickImage.launch("image/*")
        }
        ivProfile.setOnClickListener {
            pickImage.launch("image/*")
        }

        // Cancel → close
        canceltv.setOnClickListener { finish() }

        // Done → save to Realtime DB and finish
        donetv.setOnClickListener {
            val name = etName.text?.toString()?.trim().orEmpty()
            val username = etUsername.text?.toString()?.trim().orEmpty()

            if (name.isEmpty() || username.isEmpty()) {
                Toast.makeText(this, "Please fill name and username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val fields = mutableMapOf<String, Any>(
                    "fullName" to name,
                    "username" to username
                )
                // include photo bytes if user picked one
                selectedPhotoBase64?.let { fields["photoBase64"] = it }

                val res = authRepo.markProfileCompleted(fields)
                res.onSuccess {
                    Toast.makeText(this@EditProfile, "Profile saved", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@EditProfile, HomeScreen::class.java)
                    startActivity(intent)
                    finish()

                }.onFailure {
                    Toast.makeText(this@EditProfile, it.localizedMessage ?: "Save failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /** Convert selected image Uri → Base64 string (JPEG ~80% quality) */
    private fun uriToBase64(uri: Uri): String? {
        return try {
            val input: InputStream? = contentResolver.openInputStream(uri)
            val bytes = input?.readBytes() ?: return null
            input.close()

            // Optionally downscale/compress for DB size
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            val out = ByteArrayOutputStream()
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 80, out)
            val compressed = out.toByteArray()
            Base64.encodeToString(compressed, Base64.NO_WRAP)
        } catch (e: Exception) {
            null
        }
    }
}
