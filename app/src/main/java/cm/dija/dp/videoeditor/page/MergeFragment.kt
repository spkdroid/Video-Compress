package cm.dija.dp.videoeditor.page

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Canvas
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cm.dija.dp.videoeditor.R
import cm.dija.dp.videoeditor.adapter.MergeAdapter
import cm.dija.dp.videoeditor.data.Video
import cm.dija.dp.videoeditor.repository.VideoRepository
import cm.dija.dp.videoeditor.service.FfmpegServiceImpl
import cm.dija.dp.videoeditor.viewmodel.MergeViewModel
import kotlinx.android.synthetic.main.merge_fragment.*
import java.util.concurrent.TimeUnit


class MergeFragment : Fragment() {

    companion object {
        fun newInstance() = MergeFragment()
        var a: ArrayList<Video> = ArrayList()
        lateinit var mergeAdapter: MergeAdapter
        var mergeVideoList: ArrayList<String> = ArrayList()
        var videoLengthCount: Long = 0

    }

    private lateinit var viewModel: MergeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.merge_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MergeViewModel::class.java)
        // TODO: Use the ViewModel

        viewModel.initialize(this.context!!)

        mergeAdapter = MergeAdapter(a, this.context!!)
        val llm = LinearLayoutManager(this.context)
        videoMergeList.layoutManager = llm
        videoMergeList.adapter = mergeAdapter

        val itemTouchHelper = ItemTouchHelper(createItemTouchHelper())
        itemTouchHelper.attachToRecyclerView(videoMergeList)


        pickButton.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, 1)
        }

        mergeButton.setOnClickListener {

            for (video in a) {
                mergeVideoList.add(video.thumbnailPath)
            }

            val ffmpegService = FfmpegServiceImpl()

            val builder = AlertDialog.Builder(this.context!!)

            builder.setTitle("Exported Filename")

            builder.setMessage("Please Enter a file name for the merge video file")

            val viewInflated =
                LayoutInflater.from(context).inflate(R.layout.text_input, view as ViewGroup?, false)

            val input = viewInflated.findViewById(R.id.inputText) as EditText

            builder.setView(viewInflated)

            builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                dialog.dismiss()
                val mergerFileName = input.text.toString()
                if (!mergerFileName.isEmpty())
                    ffmpegService.mergeVideoFile(mergeVideoList, this.context!!, mergerFileName)
                else
                    Toast.makeText(context, "File name cannot be empty", Toast.LENGTH_LONG).show()
            }

            builder.setNegativeButton(
                android.R.string.cancel
            )
            { dialog, _ ->
                dialog.cancel()
            }
            builder.show()
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper.Callback {
        return object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                mergeAdapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mergeAdapter.removeItem(viewHolder.adapterPosition)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val alpha = 1 - Math.abs(dX) / recyclerView.width
                    viewHolder.itemView.alpha = alpha
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                VideoRepository.videoList.forEach {

                    if (getPath(data!!.data).equals(it.thumbnailPath)) {
                        a.add(it)
                        mergeAdapter.notifyDataSetChanged()
                        videoNumbers.text = a.size.toString()
                        val mp = MediaPlayer.create(context, Uri.parse(it.thumbnailPath))
                        videoLengthCount += mp.duration
                        mp.release()
                        videoTimeView.text = getVideoCurrent(videoLengthCount)
                    }
                }
            }
        }
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


    fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context!!.contentResolver.query(uri, projection, null, null, null)

        if (cursor != null) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } else
            return null
    }

}
