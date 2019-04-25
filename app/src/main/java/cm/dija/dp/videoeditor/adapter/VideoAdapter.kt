package cm.dija.dp.videoeditor.adapter

import android.content.Context
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import cm.dija.dp.videoeditor.R
import cm.dija.dp.videoeditor.data.Video
import kotlinx.android.synthetic.main.video_list_item.view.*

class VideoAdapter(private val items: ArrayList<Video>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.video_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.filename.text = items[position].mimeType
        holder.relative.text = items[position].thumbnailPath
        holder.videoimage.setImageBitmap(items[position].thumbnailImage)
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var filename = view.filename!!
    var relative = view.relativepath!!
    var videoimage = view.videoimage!!
}

interface ClickListener {
    fun onClick(view: View, position: Int)
}

internal class RecyclerTouchListener(
    context: Context,
    private val clicker: ClickListener?
) : RecyclerView.OnItemTouchListener {
    private val gestureDetector: GestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }
        })

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.x, e.y)
        if (child != null && clicker != null && gestureDetector.onTouchEvent(e)) {
            clicker.onClick(child, rv.getChildAdapterPosition(child))
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }
}