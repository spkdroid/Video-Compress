package cm.dija.dp.videocompressor.adapter

import android.content.Context
import android.view.*

import androidx.recyclerview.widget.RecyclerView
import cm.dija.dp.videocompressor.R
import cm.dija.dp.videocompressor.data.MenuItem
import kotlinx.android.synthetic.main.menu_item.view.*

class MenuAdapter(private val items: ArrayList<MenuItem>, val context: Context) : RecyclerView.Adapter<MenuViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false))
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.menuItemImage.setImageDrawable(items[position].MenuImage)
        holder.menuItemText.text = items[position].MenuItem
    }
}

class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val menuItemImage = view.MenuImage!!
    val menuItemText = view.MenuText!!
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