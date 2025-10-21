package com.jawairiawaseem.i221274

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jawairiawaseem.i221274.data.MediaEncoding
import com.jawairiawaseem.i221274.model.Story
import com.google.android.material.imageview.ShapeableImageView


class ViewStory : AppCompatActivity() {

    private lateinit var img: ShapeableImageView
    private lateinit var video: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_story)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        img = findViewById(R.id.storyImage)
        video = findViewById(R.id.storyVideo)

        // Optional: set title and time if you want
        findViewById<TextView?>(R.id.tvTitle)?.let { t ->
            (intent.getSerializableExtra("story") as? Story)?.username?.let { t.text = it }
        }
        findViewById<ImageView?>(R.id.closeiv)?.setOnClickListener { finish() }

        val story = intent.getSerializableExtra("story") as? Story ?: return

        if (story.mediaType == "image") {
            img.visibility = android.view.View.VISIBLE
            video.visibility = android.view.View.GONE
            val bmp = MediaEncoding.bitmapFromBase64(story.base64)
            img.setImageBitmap(bmp)
        } else {
            img.visibility = android.view.View.GONE
            video.visibility = android.view.View.VISIBLE
            val uri: Uri = MediaEncoding.base64ToTempVideoUri(this, story.base64, ".mp4")
            video.setVideoURI(uri)
            video.setOnPreparedListener { video.start() }
        }
    }
}
