package cm.dija.dp.videocompressor.page

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import cm.dija.dp.videocompressor.R
import cm.dija.dp.videocompressor.repository.VideoRepository
import cm.dija.dp.videocompressor.service.FfmpegServiceImpl
import cm.dija.dp.videocompressor.viewmodel.CompressViewModel
import kotlinx.android.synthetic.main.compress_fragment.*
import java.io.File


class CompressFragment : Fragment(), SeekBar.OnSeekBarChangeListener {


    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        qualityTextView.text = (Math.abs(30 - p1)).toString()
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }

    companion object {
        fun newInstance() = CompressFragment()
    }

    private lateinit var viewModel: CompressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.compress_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CompressViewModel::class.java)
        // TODO: Use the ViewModel

        selectVideoButton.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, 1)
        }

        videocompressButton.setOnClickListener {
            val ffmpegService = FfmpegServiceImpl()

            val selectedId: String

            val a = videoSpeed.checkedRadioButtonId

            selectedId = when (a) {
                radioSlow.id -> "slow"
                radioFast.id -> "fast"
                radioVeryFast.id -> "veryfast"
                else -> "ultrafast"
            }


            if (exportCompressInputText.text!!.isNotEmpty()) {
                if (videocompressEditText.text.isNotEmpty())
                    ffmpegService.compressVideoFile(
                        this.context!!,
                        videocompressEditText.text.toString(),
                        exportCompressInputText.text.toString(),
                        qualityTextView.text.toString(),
                        selectedId
                    )
                else
                    Toast.makeText(context, "Error!! Please Enter the file name", Toast.LENGTH_LONG).show()
            } else {
                exportCompressInputText.error = "Please Enter a file name"
                Toast.makeText(context, "Error!! Please Enter the file name", Toast.LENGTH_LONG).show()
            }
        }

        videoQualitySeekBar.setOnSeekBarChangeListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                VideoRepository.videoList.forEach {
                    if (getPath(data!!.data).equals(it.thumbnailPath)) {
                        videocompressEditText.setText(it.thumbnailPath)
                        videoCompressResultText.text = getVideoSize(it.thumbnailPath)
                    }
                }
            }
        }
    }

    private fun getVideoSize(videoPath: String): String {
        val file = File(videoPath)
        var length = file.length()
        length /= (1024 * 1024)
        return "The selected video size:$length MB"
    }

    fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context!!.contentResolver.query(uri, projection, null, null, null)

        return if (cursor != null) {
            val columnData = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(columnData)
        } else
            null
    }

}
