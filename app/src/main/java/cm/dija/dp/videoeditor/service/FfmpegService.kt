package cm.dija.dp.videoeditor.service

import android.content.Context

interface FfmpegService {

    fun trimVideoFile(filename: String, ctx: Context, startIndex: String, endIndex: String, exportedFileName: String)

    fun mergeVideoFile(files: ArrayList<String>, ctx: Context, filename: String)

    fun compressVideoFile(ctx:Context,filePath: String,exportedFileName: String,videoQuality:String,videoEncodeSpeed:String)
}