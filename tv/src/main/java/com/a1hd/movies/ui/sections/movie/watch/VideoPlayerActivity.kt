package com.a1hd.movies.ui.sections.movie.watch

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.a1hd.movies.databinding.ActivityVideoPlayerBinding
import com.a1hd.movies.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@UnstableApi
@AndroidEntryPoint
class VideoPlayerActivity : BaseActivity<ActivityVideoPlayerBinding>(ActivityVideoPlayerBinding::inflate), Player.Listener {

    private lateinit var simpleExoplayer: ExoPlayer
    private lateinit var videoUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        videoUrl = bundle?.getString(EXTRA_LINK).toString()
        fullScreen()
    }

    private fun initializePlayer() {
        simpleExoplayer = ExoPlayer.Builder(this).build()
        preparePlayer(videoUrl)
    }

    private fun preparePlayer(videoUrl: String) {
        val uri = Uri.parse(videoUrl)
        val mediaSource = buildMediaSource(uri)
        simpleExoplayer.setMediaSource(mediaSource, false)
        simpleExoplayer.playWhenReady = true
        simpleExoplayer.addListener(this)
        binding.playerViewFullscreen.player = simpleExoplayer
        simpleExoplayer.prepare()
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        return HlsMediaSource.Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.Builder()
                .setUri(uri)
                .setMimeType(MimeTypes.APPLICATION_M3U8)
                .build()
            )
    }

    private fun releasePlayer() {
        simpleExoplayer.pause()
        simpleExoplayer.stop()
        simpleExoplayer.release()
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING)
            binding.progressBar.visibility = View.VISIBLE
        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
            binding.progressBar.visibility = View.INVISIBLE
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        error.printStackTrace()
    }

    private fun fullScreen(){
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    companion object {

        private const val EXTRA_LINK = "EXTRA_LINK"

        fun setUrl(context: Context, url: String): Intent {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra(EXTRA_LINK, url)
            return intent
        }
    }
}
