package cm.dija.dp.videocompressor.repository

import android.app.Activity
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import cm.dija.dp.videocompressor.data.Video

object VideoRepository {
    var CardScan: Boolean = false
        private set

    var videoList: ArrayList<Video> = ArrayList()

    fun getVideoList(activity: Activity): ArrayList<Video> {
        if (!CardScan) {
            val thumbCols =
                arrayOf(MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails.VIDEO_ID)

            val videoCols = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE
            )

            val cursor: Cursor =
                activity.contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    videoCols,
                    null,
                    null,
                    null
                )

            if (cursor.moveToFirst()) {
                do {
                    var thumbnailPath = ""
                    val id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                    val thumbCursor = activity.contentResolver.query(
                        MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbCols,
                        MediaStore.Video.Thumbnails.VIDEO_ID + "=" + id,
                        null,
                        null
                    )
                    if (thumbCursor.moveToFirst()) {
                        thumbnailPath =
                            thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA))
                    }

                    val videoPath: String = cursor.getString(
                        cursor
                            .getColumnIndex(MediaStore.Video.Media.DATA)
                    )
                    val title: String = cursor.getString(
                        cursor
                            .getColumnIndex(MediaStore.Video.Media.TITLE)
                    )
                    val mimetype: String = cursor.getString(
                        cursor
                            .getColumnIndex(MediaStore.Video.Media.MIME_TYPE)
                    )

                    val metadataRetriever = MediaMetadataRetriever()
                    try {
                        metadataRetriever.setDataSource(videoPath)
                        val duration =
                            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                        val time = java.lang.Long.valueOf(duration) / 2
                        val bitmap = metadataRetriever.getFrameAtTime(
                            time,
                            MediaMetadataRetriever.OPTION_NEXT_SYNC
                        )
                        var resizedBitmap = Bitmap.createScaledBitmap(bitmap, 750, 500, true)
                        videoList.add(
                            Video(
                                thumbnailPath,
                                videoPath,
                                title,
                                mimetype,
                                resizedBitmap
                            )
                        )
                    } catch (ex: Exception) {
                    }
                } while (cursor.moveToNext())
            }
            CardScan = true
            return videoList
        } else {
            return videoList
        }
    }
}