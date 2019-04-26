package cm.dija.dp.videocompressor.adapter

import android.content.Context
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cm.dija.dp.videocompressor.R
import cm.dija.dp.videocompressor.data.MenuItem
import kotlinx.android.synthetic.main.menu_item.view.*

class MenuAdapter(val items: ArrayList<MenuItem>, val context: Context) : RecyclerView.Adapter<MenuViewHolder>() {

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