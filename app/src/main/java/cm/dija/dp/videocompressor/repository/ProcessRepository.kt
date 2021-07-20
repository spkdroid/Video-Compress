package cm.dija.dp.videocompressor.repository

import android.content.Context
import com.github.hiteshsondhi88.libffmpeg.FFmpeg

object ProcessRepository {
    fun ffmpegInstance(ctx: Context): FFmpeg = FFmpeg.getInstance(ctx.applicationContext)
}