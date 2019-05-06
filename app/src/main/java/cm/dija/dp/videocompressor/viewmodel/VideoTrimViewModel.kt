package cm.dija.dp.videocompressor.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.lifecycle.ViewModel
import cm.dija.dp.videocompressor.service.FfmpegServiceImpl
import com.google.android.material.textfield.TextInputLayout
import io.apptik.widget.MultiSlider
import java.util.concurrent.TimeUnit


class VideoTrimViewModel : ViewModel(), MediaPlayer.OnPreparedListener, MultiSlider.OnThumbValueChangeListener {

    var isPlaying = false

    lateinit var ctx: Context

    lateinit var RangeBar: MultiSlider
        private set

    lateinit var video: VideoView
        private set

    lateinit var start: TextView
        private set

    lateinit var startIndex: String
        private set

    lateinit var endIndex: String
        private set

    lateinit var end: TextView
        private set

    lateinit var filePath: String
        private set

    lateinit var toggleImage: ImageView
        private set

    lateinit var textInput: TextInputLayout

    var currentTimeUnit: String
        get() = getVideoCurrent(video.currentPosition.toLong())
        set(value) {
            currentTimeUnit = value
        }

    companion object {
        var IsChanged: Boolean = false
            private set
    }

    fun initVideoPlayer(
        videoPath: String,
        videoView: VideoView,
        startTimeView: TextView,
        endTimeView: TextView,
        videoRangeBar: MultiSlider,
        playpauseImage: ImageView,
        textInputLayout: TextInputLayout
    ) {

        videoView.setVideoPath(videoPath)
        startTimeView.text = "00:00"
        endTimeView.text = getVideoLength(videoPath)


        video = videoView
        isPlaying = videoView.isPlaying
        RangeBar = videoRangeBar

        start = startTimeView
        end = endTimeView

        toggleImage = playpauseImage

        videoView.setOnPreparedListener(this)
        RangeBar.setOnThumbValueChangeListener(this)
        textInput = textInputLayout
        filePath = videoPath
    }


    private fun getVideoLength(videoPath: String): String {
        val mp = MediaPlayer.create(ctx, Uri.parse(videoPath))
        val duration = mp.duration
        mp.release()
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(duration.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    duration.toLong()
                )
            )
        )
    }

    private fun getVideoCurrent(duration: Long): String {
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(duration),
            TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    duration
                )
            )
        )
    }

    fun assignContext(activity: Context?) {
        ctx = activity!!
    }

    fun playVideo(videoView: VideoView) {
        videoView.seekTo(RangeBar.getThumb(0).value)
        videoView.start()
        if (IsChanged) {
            RangeBar.postDelayed(onEverySecond, 1000)
        }

    }

    fun pauseVideo(videoView: VideoView) {
        videoView.pause()
    }


    override fun onValueChanged(multiSlider: MultiSlider?, thumb: MultiSlider.Thumb?, thumbIndex: Int, value: Int) {

        IsChanged = true

        if (thumbIndex == 0) {
            start.text = getVideoCurrent(multiSlider!!.getThumb(0).value.toLong())
            startIndex = start.text.toString()
        } else {
            end.text = getVideoCurrent(multiSlider!!.getThumb(1).value.toLong())
            endIndex = end.text.toString()
        }
    }


    override fun onPrepared(mp: MediaPlayer?) {
        RangeBar.max = video.duration
    }

    fun trim(ctx: Context) {
        val ffmpegService = FfmpegServiceImpl()
        if (textInput.editText!!.text.isNotEmpty())
            ffmpegService.trimVideoFile(filePath, ctx, startIndex, endIndex, textInput.editText!!.text.toString())
        else
            Toast.makeText(ctx, "Error!! Please Enter the file name", Toast.LENGTH_LONG).show()
    }

    private val onEverySecond = object : Runnable {
        override fun run() {
            start.text = getVideoCurrent(video.currentPosition.toLong())
            RangeBar.postDelayed(this, 1000)

            if (getVideoCurrent(RangeBar.getThumb(1).value.toLong()) == start.text && video.isPlaying) {
                video.pause()
                toggleImage.setImageDrawable(ctx.getDrawable(cm.dija.dp.videocompressor.R.drawable.pause))
            }
        }
    }
}