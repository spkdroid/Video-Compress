package cm.dija.dp.videoeditor.data

import android.graphics.Bitmap


data class Video(
    val videoPath: String,
    val thumbnailPath: String,
    val mimeType: String,
    val title: String,
    val thumbnailImage: Bitmap
)