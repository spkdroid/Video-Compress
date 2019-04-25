package cm.dija.dp.videoeditor.page

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import cm.dija.dp.videoeditor.R
import cm.dija.dp.videoeditor.adapter.ClickListener
import cm.dija.dp.videoeditor.adapter.RecyclerTouchListener
import cm.dija.dp.videoeditor.adapter.VideoAdapter
import cm.dija.dp.videoeditor.repository.VideoRepository
import cm.dija.dp.videoeditor.viewmodel.PickFileViewModel
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.main_fragment.*
import java.io.File

class PickFileFragment : Fragment() {

    companion object {
        fun newInstance() = PickFileFragment()
    }

    private lateinit var viewModel: PickFileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PickFileViewModel::class.java)
        // TODO: Use the ViewModel

        val rxPermissions = RxPermissions(this)

        rxPermissions
            .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe { granted ->
                if (granted) { // Always true pre-M

                    var videoRepository = VideoRepository
                    var b = videoRepository.videoList

                    val directory = File(Environment.getExternalStorageDirectory().toString() + "/Mp4Editor/")

                    if(!directory.exists())
                    {
                        directory.mkdir()
                    }

                    rv_animal_list.apply {
                        layoutManager = GridLayoutManager(context, 3)
                        adapter = VideoAdapter(b, context)
                    }

                    rv_animal_list.addOnItemTouchListener(
                        RecyclerTouchListener(
                            this.activity!!,
                            object : ClickListener {
                                override fun onClick(view: View, position: Int) {

                                    var bundle = Bundle()
                                    bundle.putString("VideoPath", b[position].thumbnailPath)
                                    Navigation.findNavController(view)
                                        .navigate(R.id.action_mainFragment_to_videoTrimFragment, bundle)
                                }
                            })
                    )

                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show()
                }
            }

    }
}
