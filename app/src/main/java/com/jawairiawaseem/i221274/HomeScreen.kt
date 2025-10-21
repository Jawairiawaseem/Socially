package com.jawairiawaseem.i221274

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MimeTypeMap
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.jawairiawaseem.i221274.data.MediaEncoding
import com.jawairiawaseem.i221274.data.PostsRepository
import com.jawairiawaseem.i221274.data.StoriesRepository
import com.jawairiawaseem.i221274.model.Post
import com.jawairiawaseem.i221274.model.Story
import java.util.*

class HomeScreen : AppCompatActivity() {

    // STORIES UI
    private lateinit var storiesBar: LinearLayout
    private lateinit var camIv: ImageView

    // POSTS UI
    private lateinit var rvPosts: RecyclerView
    private lateinit var postAdapter: PostAdapter

    // Firebase
    private val auth by lazy { FirebaseAuth.getInstance() }

    // listeners
    private var postsListener: com.google.firebase.database.ValueEventListener? = null

    // media picker (for stories add from camera icon)
    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> if (uri != null) handlePickedStoryMedia(uri) }

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

        // ===== stories strip =====
        storiesBar = findViewById(R.id.storiesbar)
        camIv = findViewById(R.id.camiv)
        camIv.setOnClickListener { pickMedia.launch("*/*") }
        loadStoriesIntoBar()

        // ===== posts feed =====
        rvPosts = findViewById(R.id.rvPosts)
        postAdapter = PostAdapter(
            mutableListOf(),
            onLike = { post ->
                val uid = auth.currentUser?.uid ?: return@PostAdapter
                PostsRepository.toggleLike(post.postId, uid)
            },
            onComment = { post ->
                startActivity(Intent(this, CommentsActivity::class.java).putExtra("post", post))
            }
        )
        rvPosts.layoutManager = LinearLayoutManager(this)
        rvPosts.adapter = postAdapter

        // open create post
        findViewById<ImageView>(R.id.createiv)?.setOnClickListener {
            startActivity(Intent(this, CreatePost::class.java))
        }

        // start listening to posts
        postsListener = PostsRepository.listenToPosts { list -> postAdapter.submit(list) }
    }

    // ===== STORIES helpers =====
    private fun loadStoriesIntoBar() {
        StoriesRepository.fetchValidStories(
            onResult = { list ->
                storiesBar.removeAllViews()
                addYourStoryTile()
                list.forEach { addStoryChip(it) }
            },
            onError = { Toast.makeText(this, "Failed to load stories: ${it.message}", Toast.LENGTH_SHORT).show() }
        )
    }

    private fun addYourStoryTile() {
        val container = FrameLayout(this).apply {
            layoutParams = ViewGroup.MarginLayoutParams(dp(80), dp(110)).apply { rightMargin = dp(8) }
        }
        val img = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(80))
            setImageResource(R.drawable.story)
            scaleType = ImageView.ScaleType.CENTER_CROP
            clipToOutline = true
            // optional rounded background; remove if you don't have the drawable
            // background = resources.getDrawable(R.drawable.round_corner_999, theme)
        }
        val tv = TextView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            ).apply { bottomMargin = dp(4) }
            text = "Your Story"
            setTextColor(Color.WHITE)
            textSize = 12f
            setTypeface(typeface, Typeface.BOLD)
        }
        container.addView(img); container.addView(tv)
        container.setOnClickListener { pickMedia.launch("*/*") }
        storiesBar.addView(container)
    }

    private fun addStoryChip(story: Story) {
        val container = FrameLayout(this).apply {
            layoutParams = ViewGroup.MarginLayoutParams(dp(80), dp(110)).apply { rightMargin = dp(8) }
        }
        val img = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(80))
            scaleType = ImageView.ScaleType.CENTER_CROP
            clipToOutline = true
            // background = resources.getDrawable(R.drawable.round_corner_999, theme)
        }

        if (story.mediaType == "image") {
            MediaEncoding.bitmapFromBase64(story.base64)?.let { img.setImageBitmap(it) }
                ?: img.setImageResource(R.drawable.story)
        } else {
            img.setImageResource(android.R.drawable.ic_media_play)
        }

        val tv = TextView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            ).apply { bottomMargin = dp(4) }
            text = if (story.username.isBlank()) "Story" else story.username
            setTextColor(Color.WHITE)
            textSize = 12f
            maxLines = 1
        }

        container.addView(img); container.addView(tv)
        container.setOnClickListener {
            startActivity(Intent(this, ViewStory::class.java).putExtra("story", story))
        }
        storiesBar.addView(container)
    }

    private fun handlePickedStoryMedia(uri: Uri) {
        val uid = auth.currentUser?.uid
        val uname = auth.currentUser?.displayName ?: "User"
        if (uid == null) { Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show(); return }

        val mime = contentResolver.getType(uri) ?: guessMime(uri) ?: ""
        val isImage = mime.startsWith("image/")
        val isVideo = mime.startsWith("video/")
        if (!isImage && !isVideo) {
            Toast.makeText(this, "Pick an image or a video", Toast.LENGTH_SHORT).show(); return
        }

        val base64 = if (isImage) {
            val bytes = MediaEncoding.readAllBytes(this, uri)
            MediaEncoding.imageBytesToBase64(bytes, 70)
        } else {
            contentResolver.openInputStream(uri)?.use { ins -> MediaEncoding.videoStreamToBase64(ins) } ?: ""
        }
        if (base64.isBlank()) { Toast.makeText(this, "Failed to read media", Toast.LENGTH_SHORT).show(); return }

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
            onDone = { Toast.makeText(this, "Story uploaded", Toast.LENGTH_SHORT).show(); loadStoriesIntoBar() },
            onError = { Toast.makeText(this, "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show() }
        )
    }

    private fun guessMime(uri: Uri): String? {
        val ext = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
    }

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()

    override fun onDestroy() {
        super.onDestroy()
        postsListener?.let { PostsRepository.removePostsListener(it) }
    }
}
