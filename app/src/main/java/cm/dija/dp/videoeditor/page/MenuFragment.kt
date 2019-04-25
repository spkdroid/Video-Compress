package cm.dija.dp.videoeditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cm.dija.dp.videoeditor.adapter.ClickListener
import cm.dija.dp.videoeditor.adapter.MenuAdapter
import cm.dija.dp.videoeditor.adapter.RecyclerTouchListener
import cm.dija.dp.videoeditor.data.MenuItem
import kotlinx.android.synthetic.main.menu_fragment.*
import android.view.KeyEvent
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog


class MenuFragment : Fragment() {

    companion object {
        fun newInstance() = MenuFragment()
    }

    private lateinit var viewModel: MenuViewModel

    private var menuItems: ArrayList<MenuItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuItems.clear()
        menuItems.add(MenuItem("Trim Video", resources.getDrawable(R.drawable.trim_video)))
        menuItems.add(MenuItem("Merge Video", resources.getDrawable(R.drawable.merge_video)))
        menuItems.add(MenuItem("Compress Video",resources.getDrawable(R.drawable.compress_video)))
        menuItems.add(MenuItem("Exported Video", resources.getDrawable(R.drawable.export_video)))
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.menu_fragment, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MenuViewModel::class.java)
        MenuItems()
    }

    private fun MenuItems() {

        val menuAdapter = MenuAdapter(menuItems, this.context!!)
        val llm = LinearLayoutManager(this.context)
        recyclerView.layoutManager = llm
        recyclerView.adapter = menuAdapter

        recyclerView.addOnItemTouchListener(
            RecyclerTouchListener(
                this.activity!!,
                object : ClickListener {
                    override fun onClick(view: View, position: Int) {
                        when (position) {
                            0 -> Navigation.findNavController(view).navigate(R.id.action_menuFragment_to_mainFragment)
                            1 -> Navigation.findNavController(view).navigate(R.id.action_menuFragment_to_mergeFragment)
                            2 -> Navigation.findNavController(view).navigate(R.id.action_menuFragment_to_compressFragment)
                            3 -> Navigation.findNavController(view).navigate(R.id.action_menuFragment_to_folderFragment)
                        }
                    }
                })
        )

    }

}
