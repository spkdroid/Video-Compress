package cm.dija.dp.videoeditor.page

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import cm.dija.dp.videoeditor.R
import cm.dija.dp.videoeditor.adapter.ClickListener
import cm.dija.dp.videoeditor.adapter.FolderAdapter
import cm.dija.dp.videoeditor.adapter.RecyclerTouchListener
import cm.dija.dp.videoeditor.data.FolderVideoHolder
import cm.dija.dp.videoeditor.viewmodel.FolderViewModel
import kotlinx.android.synthetic.main.folder_fragment.*
import java.io.File


class FolderFragment : Fragment() {

    companion object {
        fun newInstance() = FolderFragment()
        lateinit var folderAdapter: FolderAdapter
        val paths = ArrayList<FolderVideoHolder>()
    }

    private lateinit var viewModel: FolderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.folder_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FolderViewModel::class.java)
        // TODO: Use the ViewModel
        folderAdapter = FolderAdapter(getVideoFile(), this.context!!)
        val llm = LinearLayoutManager(this.context)
        recyclerViewFolderList.layoutManager = llm
        recyclerViewFolderList.adapter = folderAdapter

        recyclerViewFolderList.addOnItemTouchListener(
            RecyclerTouchListener(
                this.activity!!,
                object : ClickListener {
                    override fun onClick(view: View, position: Int) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paths[position].videoPath))
                        intent.setDataAndType(Uri.parse(paths[position].videoPath), "video/mp4")
                        startActivity(intent)
                    }
                })
        )
    }

    private fun getVideoFile(): ArrayList<FolderVideoHolder> {


        val directory = File(Environment.getExternalStorageDirectory().toString() + "/Mp4Editor/")

        val files = directory.listFiles()

        for (i in files.indices) {
            val a = FolderVideoHolder(
                files[i].toString().substringAfterLast("/"),
                files[i].absolutePath.toString(),
                getFileSize(files[i])
            )
            paths.add(a)
        }

        return paths
    }

    private fun getFileSize(file: File?): String {
        return (file!!.length() / (1024 * 1024)).toString() + " MB"
    }

}
