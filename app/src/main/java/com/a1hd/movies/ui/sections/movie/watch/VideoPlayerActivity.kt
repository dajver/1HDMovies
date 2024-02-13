package com.a1hd.movies.ui.sections.movie.watch

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.LoadControl
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.upstream.DefaultAllocator
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
import com.a1hd.movies.databinding.ActivityVideoPlayerBinding
import com.a1hd.movies.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

//Minimum Video you want to buffer while Playing
private const val MIN_BUFFER_DURATION = 2000
//Max Video you want to buffer during PlayBack
private const val MAX_BUFFER_DURATION = 5000
//Min Video you want to buffer before start Playing it
private const val MIN_PLAYBACK_START_BUFFER = 1500
//Min video You want to buffer when user resumes video
private const val MIN_PLAYBACK_RESUME_BUFFER = 2000

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
        val loadControl: LoadControl = DefaultLoadControl.Builder()
            .setAllocator(DefaultAllocator(true, 16))
            .setBufferDurationsMs(MIN_BUFFER_DURATION, MAX_BUFFER_DURATION, MIN_PLAYBACK_START_BUFFER, MIN_PLAYBACK_RESUME_BUFFER)
            .setTargetBufferBytes(-1)
            .setPrioritizeTimeOverSizeThresholds(true).build()
        val trackSelector = DefaultTrackSelector(this).apply {
            parameters = parameters.buildUpon()
                .setMaxVideoSize(1280, 720)
                .build()
        }
        trackSelector.setParameters(
            trackSelector.buildUponParameters().setMaxVideoSizeSd()
        )

        simpleExoplayer = ExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .setLoadControl(loadControl)
            .build()
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
