package cm.dija.dp.videoeditor.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cm.dija.dp.videoeditor.R
import cm.dija.dp.videoeditor.data.Video
import kotlinx.android.synthetic.main.merge_item.view.*


class MergeAdapter(private val items: ArrayList<Video>, val context: Context) :
    RecyclerView.Adapter<MergeViewHolder>() {

    override fun onBindViewHolder(holder: MergeViewHolder, position: Int) {
        holder.videoimage.setImageBitmap(resizeBitmap(items[position].thumbnailImage,120,80))
        holder.filename.text = items[position].mimeType
        holder.relative.text = items[position].thumbnailPath
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MergeViewHolder {
        return MergeViewHolder(LayoutInflater.from(context).inflate(R.layout.merge_item, parent, false))
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
    }

    fun moveItem(oldPos: Int, newPos: Int) {
        val item = items[oldPos]
        items.removeAt(oldPos)
        items.add(newPos, item)
        notifyItemMoved(oldPos, newPos)
    }

      private fun resizeBitmap(bitmap: Bitmap, width:Int, height:Int):Bitmap{

        return Bitmap.createScaledBitmap(
            bitmap,
            width,
            height,
            false
        )
    }

}

class MergeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var filename = view.VideoName!!
    var relative = view.VideoPath!!
    var videoimage = view.VideoImg!!
}