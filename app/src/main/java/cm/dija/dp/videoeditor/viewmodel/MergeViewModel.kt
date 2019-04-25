package cm.dija.dp.videoeditor.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel


class MergeViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    private val SELECT_VIDEO = 1

    lateinit var ctx: Context
        private set

    fun initialize(ctx: Context) {
        this.ctx = ctx
    }

    fun pickVideo() {
        // val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        // ctx.applicationContext.startActivities()
        //ctx.startActivityForResult(i, SELECT_VIDEO)
    }


}
