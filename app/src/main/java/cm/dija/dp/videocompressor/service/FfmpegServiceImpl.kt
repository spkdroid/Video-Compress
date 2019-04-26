package cm.dija.dp.videocompressor.service

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Environment
import cm.dija.dp.videocompressor.repository.ProcessRepository
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException
import java.io.*
import java.util.*


class FfmpegServiceImpl : FfmpegService {

    override fun trimVideoFile(
        filePath: String,
        ctx: Context,
        startIndex: String,
        endIndex: String,
        exportedFileName: String
    ) {


        var outputPath =
            Environment.getExternalStorageDirectory().path + "/" + "Mp4Editor" + "/" + exportedFileName + ".mp4"
        val args = arrayOf("-y", "-i", filePath, "-ss", startIndex, "-to", endIndex, outputPath)

        ffmpegExecute(ctx, args, outputPath)

    }


    override fun compressVideoFile(ctx:Context,filePath: String,exportedFileName: String,videoQuality:String,videoEncodeSpeed:String)
    {
        var outputPath = Environment.getExternalStorageDirectory().path + "/" + "ZXVideoCompress" + "/" + exportedFileName + ".mp4"
        val args = arrayOf( "-i",filePath,"-crf",videoQuality,"-preset",videoEncodeSpeed,"-b:a","96k",outputPath)
        ffmpegExecute(ctx, args, outputPath)
    }



    private fun showUnsupportedExceptionDialog(ctx: Context) {
        AlertDialog.Builder(ctx)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Error")
            .setMessage("Error")
            .setCancelable(false)
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    override fun mergeVideoFile(files: ArrayList<String>, ctx: Context, filename: String) {

        val fout = File(Environment.getExternalStorageDirectory().toString() + "/Mp4Editor/input.txt")
        try {
            fout.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val input = Environment.getExternalStorageDirectory().toString() + "/Mp4Editor/input.txt"
        val target = Environment.getExternalStorageDirectory().toString() + "/Mp4Editor/" + filename + ".mp4"

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(fout)
        } catch (e: FileNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }


        val bw = BufferedWriter(OutputStreamWriter(fos))

        for (i in 0 until files.size) {
            try {
                bw.write("file" + " " + "'" + files[i] + "'")
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

            try {
                bw.newLine()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }

        try {
            bw.close()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        val args = arrayOf("-f", "concat", "-safe", "0", "-i", input, "-c", "copy", target)

        ffmpegExecute(ctx, args, target)

    }


    private fun ffmpegExecute(ctx: Context, args: Array<String>, outputPath: String) {

        val rootPath = Environment.getExternalStorageDirectory().absolutePath + "/Mp4Editor/"

        val file = File(rootPath)

        if (!file.exists()) {
            file.mkdirs()
        }

        var ffmpegCommand = ProcessRepository.ffmpegInstance(ctx)

        try {
            ffmpegCommand.loadBinary(object : LoadBinaryResponseHandler() {
                override fun onFailure() {
                    showUnsupportedExceptionDialog(ctx)
                }
            })
        } catch (e: FFmpegNotSupportedException) {
            showUnsupportedExceptionDialog(ctx)
        }


        var progressDialog = ProgressDialog(ctx)
        progressDialog.setIcon(cm.dija.dp.videocompressor.R.drawable.export_video)
        progressDialog.setCancelable(false)
        progressDialog.setTitle("Mp4 Editor Started")


        try {
            ffmpegCommand.execute(args, object : ExecuteBinaryResponseHandler() {

                override fun onFailure(s: String?) {
                    val builder = AlertDialog.Builder(ctx)
                    builder.setTitle("Failed")
                    builder.setMessage("Error Please Retry Again $s")
                    builder.create()
                    builder.show()
                }

                override fun onSuccess(s: String?) {
                    val builder = AlertDialog.Builder(ctx)
                    builder.setTitle("Successful")
                    builder.setMessage("The Exported file can be found on the following path $outputPath")
                    builder.create()
                    builder.show()
                }

                override fun onProgress(s: String?) {
                    progressDialog.setMessage("Processing\n$s")
                }

                override fun onStart() {
                    progressDialog.show()
                }

                override fun onFinish() {
                    progressDialog.dismiss()
                }
            })
        } catch (e: FFmpegCommandAlreadyRunningException) {
            // do nothing for now
        }
    }

    @Throws(Exception::class)
    private fun createTransactionID(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

}