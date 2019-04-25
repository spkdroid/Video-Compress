package cm.dija.dp.videoeditor.adapter

import android.content.Context
import android.view.*

import androidx.recyclerview.widget.RecyclerView
import cm.dija.dp.videoeditor.R
import cm.dija.dp.videoeditor.data.FolderVideoHolder
import kotlinx.android.synthetic.main.folder_item.view.*

class FolderAdapter(val items: ArrayList<FolderVideoHolder>, val context: Context) : RecyclerView.Adapter<FolderView>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderView {
        return FolderView(LayoutInflater.from(context).inflate(R.layout.folder_item, parent, false))
    }

    override fun onBindViewHolder(holder: FolderView, position: Int) {
        holder.folderfileTitle.text = items[position].videoName
        holder.folderfilePath.text = items[position].videoPath
        holder.folderfileSize.text = items[position].videoSize
    }
}

class FolderView(view: View) : RecyclerView.ViewHolder(view) {

    val folderfileTitle = view.filenameTextView!!
    val folderfileSize = view.fileSizeTextView!!
    val folderfilePath = view.filepathTextView!!

}


