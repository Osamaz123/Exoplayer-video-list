package com.example.exoplayr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.viewpager2.widget.ViewPager2
import com.example.exoplayr.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    // binding variable for the layout
    private lateinit var binding: ActivityMainBinding


    private lateinit var adapter: VideoAdapter

    // list for videos
    private val videos = ArrayList<Video>()

    // list for exoplayer items
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // adding videos to the list
        videos.add(
            Video(
                "Nature",
                "https://cdn.pixabay.com/vimeo/328940142/Buttercups%20-%2022634.mp4?width=1304&hash=2df4ff27ac821dcb2174355e8051bd782697fcb4"
            )
        )



        videos.add(
            Video(
                "Sunset",
                "https://cdn.pixabay.com/vimeo/230853041/Sunflower%20Field%20-%2011496.mp4?width=1920&hash=8968e544bc5065e97c92ce731c526c000bca6be3"
            )
        )
 videos.add(
            Video(
                "Butterfly",
                "https://cdn.pixabay.com/vimeo/452154245/Butterfly%20-%2047876.mp4?width=960&hash=9f555065212feeb68ca992a15986b86ec5227c09"
            )
        )
 videos.add(
            Video(
                "Country Side",
                "https://cdn.pixabay.com/vimeo/338863706/Flowers%20-%2023872.mp4?width=1920&hash=678cfac87bf9ea6d79b56f285067b1b3bf389751"
            )
        )

        //setting the adapter
        adapter = VideoAdapter(this, videos, object : VideoAdapter.OnVideoPreparedListener {
            override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
                exoPlayerItems.add(exoPlayerItem)
            }
        })

        binding.viewPager2.adapter = adapter


        // registering callback for page change
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // pausing the previous video that was playing
                val previousIndex = exoPlayerItems.indexOfFirst { it.exoPlayer.isPlaying }
                if (previousIndex != -1) {
                    val player = exoPlayerItems[previousIndex].exoPlayer
                    player.pause()
                    player.playWhenReady = false
                }

                // playing the new video
                val newIndex = exoPlayerItems.indexOfFirst { it.position == position }
                if (newIndex != -1) {
                    val player = exoPlayerItems[newIndex].exoPlayer
                    player.playWhenReady = true
                    player.play()
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()

        // pausing the video when the activity is paused

        val index = exoPlayerItems.indexOfFirst { it.position == binding.viewPager2.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.pause()
            player.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()
// resuming the video when the activity is resumed
        val index = exoPlayerItems.indexOfFirst { it.position == binding.viewPager2.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.playWhenReady = true
            player.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stopping and clearing the media items when the activity is destroyed
        if (exoPlayerItems.isNotEmpty()) {
            for (item in exoPlayerItems) {
                val player = item.exoPlayer
                player.stop()
                player.clearMediaItems()
            }
        }
    }
}