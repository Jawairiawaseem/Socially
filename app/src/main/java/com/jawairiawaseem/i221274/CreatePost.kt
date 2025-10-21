package com.jawairiawaseem.i221274

import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.jawairiawaseem.i221274.data.MediaEncoding
import com.jawairiawaseem.i221274.data.PostsRepository
import com.jawairiawaseem.i221274.model.Post
import java.util.*

class CreatePost : AppCompatActivity() {

    private lateinit var imgPreview: ImageView
    private lateinit var caption: EditText
    private lateinit var nextTv: TextView
    private lateinit var cancelTv: TextView

    private var pickedBase64: String = ""

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) handlePick(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_post)

        imgPreview = findViewById(R.id.postimage)
        caption = findViewById(R.id.etCaption)
        nextTv = findViewById(R.id.donetv)
        cancelTv = findViewById(R.id.canceltv)

        cancelTv.setOnClickListener { finish() }
        imgPreview.setOnClickListener { pickImage.launch("image/*") }

        nextTv.setOnClickListener { uploadPost() }

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
    }

    private fun handlePick(uri: Uri) {
        val bytes = MediaEncoding.readAllBytes(this, uri)
        if (bytes.isEmpty()) {
            Toast.makeText(this, "Could not read image", Toast.LENGTH_SHORT).show()
            return
        }
        pickedBase64 = MediaEncoding.imageBytesToBase64(bytes, 75)
        MediaEncoding.bitmapFromBase64(pickedBase64)?.let { imgPreview.setImageBitmap(it) }
    }

    private fun uploadPost() {
        if (pickedBase64.isBlank()) {
            Toast.makeText(this, "Tap the image to choose a photo first", Toast.LENGTH_SHORT).show()
            return
        }
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid ?: run {
            Toast.makeText(this, "Please log in", Toast.LENGTH_SHORT).show(); return
        }
        val uname = auth.currentUser?.displayName ?: "User"

        val post = Post(
            postId = UUID.randomUUID().toString(),
            userId = uid,
            username = uname,
            caption = caption.text.toString().trim(),
            imageBase64 = pickedBase64,
            createdAt = System.currentTimeMillis(),
            likeCount = 0,
            commentCount = 0
        )

        PostsRepository.createPost(post,
            onOk = { Toast.makeText(this, "Posted!", Toast.LENGTH_SHORT).show(); finish() },
            onErr = { Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_SHORT).show() }
        )
    }
}
