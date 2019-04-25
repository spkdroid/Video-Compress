package cm.dija.dp.videoeditor.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import cm.dija.dp.videoeditor.R
import cm.dija.dp.videoeditor.viewmodel.VideoTrimViewModel
import kotlinx.android.synthetic.main.video_trim_fragment.*

class VideoTrimFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        if (v == playpauseImage && !videoView.isPlaying) {
            viewModel.playVideo(videoView)
            playpauseImage.setImageDrawable(resources.getDrawable(R.drawable.pause))
        } else {
            viewModel.pauseVideo(videoView)
            playpauseImage.setImageDrawable(resources.getDrawable(R.drawable.play))
        }
    }

    companion object {
        fun newInstance() = VideoTrimFragment()
    }

    private lateinit var viewModel: VideoTrimViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.video_trim_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VideoTrimViewModel::class.java)
        // TODO: Use the ViewModelå
        val videoPath = arguments!!.getString("VideoPath")

        viewModel.assignContext(this.context)
        viewModel.initVideoPlayer(
            videoPath,
            videoView,
            startTimeView,
            endTimeView,
            videoRangeBar,
            playpauseImage,
            textInputLayout
        )
        playpauseImage.setOnClickListener(this)

        trimbutton.setOnClickListener {
            viewModel.trim(this.context!!)
        }


    }
}
