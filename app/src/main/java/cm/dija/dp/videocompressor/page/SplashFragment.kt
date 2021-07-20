package cm.dija.dp.videocompressor.page

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import cm.dija.dp.videocompressor.R
import cm.dija.dp.videocompressor.repository.VideoRepository
import cm.dija.dp.videocompressor.viewmodel.SplashViewModel
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.splash_fragment.*
import java.io.File
import java.util.concurrent.TimeUnit


class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private lateinit var viewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        // TODO: Use the ViewModel

        val rxPermissions = RxPermissions(this)

        rxPermissions
            .request(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe { granted ->
                if (granted) { // Always true pre-M
                    GenerateVideoRepository()
                } else {

                    AlertDialog.Builder(this.context!!)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("Warning")
                        .setMessage("Video Compress application require storage permission to run the application, Please restart the application")
                        .setPositiveButton("Exit") { dialog, which -> System.exit(0) }
                        .show()
                }
            }
    }

    private fun GenerateVideoRepository() {

        val directory = File(Environment.getExternalStorageDirectory().toString(), "/ZXVideoCompress")

        if(!directory.exists()) {
            directory.mkdir()
        }

        Observable.fromCallable {
            VideoRepository.getVideoList(this.activity!!)
            false
        }.delay(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                SplashProgressBar.visibility = View.GONE
                view?.let {
                    Navigation.findNavController(it).navigate(R.id.action_splashFragment_to_menuFragment)
                }
            }
    }
}
