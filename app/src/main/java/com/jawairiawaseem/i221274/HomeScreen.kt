package com.jawairiawaseem.i221274

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.jawairiawaseem.i221274.data.MediaEncoding
import com.jawairiawaseem.i221274.data.StoriesRepository
import com.jawairiawaseem.i221274.model.Story
import java.util.UUID

class HomeScreen : AppCompatActivity() {

    private lateinit var storiesBar: LinearLayout
    private lateinit var camIv: ImageView

    private val auth by lazy { FirebaseAuth.getInstance() }

    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) handlePickedMedia(uri)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        storiesBar = findViewById(R.id.storiesbar)
        camIv = findViewById(R.id.camiv)

        // Tap camera to add story (image or video)
        camIv.setOnClickListener {
            // allow both images and videos; picker will filter by type chosen by user
            pickMedia.launch("*/*")
        }

        // Load and display stories
        loadStoriesIntoBar()
    }

    private fun loadStoriesIntoBar() {
        StoriesRepository.fetchValidStories(
            onResult = { list ->
                storiesBar.removeAllViews()
                // Optional: show "Your Story" tile as the first item
                addYourStoryTile()
                list.forEach { addStoryChip(it) }
            },
            onError = {
                Toast.makeText(this, "Failed to load stories: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }


    private fun addYourStoryTile() {
        val container = FrameLayout(this).apply {
            layoutParams = ViewGroup.MarginLayoutParams(
                dp(80), dp(110)
            ).apply { rightMargin = dp(8) }
        }

        val img = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(80)
            )
            setImageResource(R.drawable.story) // your placeholder asset
            scaleType = ImageView.ScaleType.CENTER_CROP
            clipToOutline = true
            background = resources.getDrawable(R.drawable.round_corner_999, theme) // optional rounded bg, or skip
        }

        val tv = TextView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            ).apply { bottomMargin = dp(4) }
            text = "Your Story"
            setTextColor(Color.WHITE)
            textSize = 12f
            setTypeface(typeface, Typeface.BOLD)
        }

        container.addView(img)
        container.addView(tv)
        container.setOnClickListener {
            // Add new story via picker
            pickMedia.launch("*/*")
        }

        storiesBar.addView(container)
    }

    private fun addStoryChip(story: Story) {
        val container = FrameLayout(this).apply {
            layoutParams = ViewGroup.MarginLayoutParams(
                dp(80), dp(110)
            ).apply { rightMargin = dp(8) }
        }

        val img = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(80)
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
            clipToOutline = true
            background = resources.getDrawable(R.drawable.round_corner_999, theme) // if you have a circular/rounded bg
        }

        if (story.mediaType == "image") {
            MediaEncoding.bitmapFromBase64(story.base64)?.let { bmp ->
                img.setImageBitmap(bmp)
            } ?: img.setImageResource(R.drawable.story)
        } else {
            // a simple play icon if it's a video
            img.setImageResource(android.R.drawable.ic_media_play)
        }

        val tv = TextView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            ).apply { bottomMargin = dp(4) }
            text = if (story.username.isBlank()) "Story" else story.username
            setTextColor(Color.WHITE)
            textSize = 12f
            maxLines = 1
        }

        container.addView(img)
        container.addView(tv)
        container.setOnClickListener {
            // open your existing viewer
            val i = Intent(this, ViewStory::class.java).apply {
                putExtra("story", story) // Story is Serializable
            }
            startActivity(i)
        }

        storiesBar.addView(container)
    }

    private fun handlePickedMedia(uri: Uri) {
        val uid = auth.currentUser?.uid
        val uname = auth.currentUser?.displayName ?: "User"
        if (uid == null) {
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show()
            return
        }

        val mime = contentResolver.getType(uri) ?: guessMime(uri) ?: ""
        val isImage = mime.startsWith("image/")
        val isVideo = mime.startsWith("video/")
        if (!isImage && !isVideo) {
            Toast.makeText(this, "Pick an image or a video", Toast.LENGTH_SHORT).show()
            return
        }

        val base64 = if (isImage) {
            val bytes = MediaEncoding.readAllBytes(this, uri)
            MediaEncoding.imageBytesToBase64(bytes, 70)
        } else {
            contentResolver.openInputStream(uri)?.use { ins ->
                MediaEncoding.videoStreamToBase64(ins)
            } ?: ""
        }

        if (base64.isBlank()) {
            Toast.makeText(this, "Failed to read media", Toast.LENGTH_SHORT).show()
            return
        }

        val story = Story(
            storyId = UUID.randomUUID().toString(),
            userId = uid,
            username = uname,
            mediaType = if (isImage) "image" else "video",
            mime = mime,
            base64 = base64,
            createdAt = System.currentTimeMillis()
        )

        StoriesRepository.uploadStory(
            story,
            onDone = {
                Toast.makeText(this, "Story uploaded", Toast.LENGTH_SHORT).show()
                loadStoriesIntoBar()
            },
            onError = {
                Toast.makeText(this, "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun guessMime(uri: Uri): String? {
        val ext = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
    }

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()
}
