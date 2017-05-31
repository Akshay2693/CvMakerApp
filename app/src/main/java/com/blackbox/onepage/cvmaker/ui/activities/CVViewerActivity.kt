package com.blackbox.onepage.cvmaker.ui.activities

import android.app.ProgressDialog
import android.arch.persistence.room.Room
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatTextView
import android.util.Log
import android.view.View
import android.view.View.GONE
import com.blackbox.onepage.cvmaker.R
import com.blackbox.onepage.cvmaker.db.AppDatabase
import com.blackbox.onepage.cvmaker.models.BasicInfo
import com.hendrix.pdfmyxml.PdfDocument
import com.hendrix.pdfmyxml.utils.FileUtils
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.io.IOException
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class CVViewerActivity : AppCompatActivity() {

    val TAG: String = "CVViewerActivity"

    val threadExecutor: Executor = Executors.newFixedThreadPool(5)

    val db: AppDatabase = Room.databaseBuilder(this, AppDatabase::class.java, "user-database").build()

    private var basicInfo: BasicInfo? = null

    private var progressDialog: ProgressDialog? = null
    private var message: String? = null

    @Synchronized fun show() {
        progressDialog!!.setMessage(message)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.show()
    }

    @Synchronized fun dismiss() {
        if (progressDialog == null) {
            return
        }

        progressDialog!!.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cv_viewer)

        getData()

        val btnExport = findViewById(R.id.btn_export) as AppCompatButton

        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
            message = getString(R.string.message_loading)
        }

        btnExport.setOnClickListener { view ->
            createPDF()
        }

    }

    fun loadData()
    {
        val editName = findViewById(R.id.txt_user_name) as AppCompatTextView
        val editTitle = findViewById(R.id.txt_user_title) as AppCompatTextView
        val editSummary = findViewById(R.id.txt_user_summary) as AppCompatTextView
        val image = findViewById(R.id.img_profile) as CircleImageView

        editName.setText(basicInfo?.firstName + " " + basicInfo?.lastName)
        editTitle.setText(basicInfo?.title)
        editSummary.setText(basicInfo?.summary)
        runOnUiThread(Runnable {
            Picasso.with(this).load(basicInfo?.pictureUrl).into(image)
        })

    }

    fun loadDataInView(view:View)
    {
        val btnExport = view.findViewById(R.id.btn_export) as AppCompatButton
        val editName = view.findViewById(R.id.txt_user_name) as AppCompatTextView
        val editTitle = view.findViewById(R.id.txt_user_title) as AppCompatTextView
        val editSummary = view.findViewById(R.id.txt_user_summary) as AppCompatTextView
        val image = view.findViewById(R.id.img_profile) as CircleImageView

        btnExport.visibility=GONE
        editName.setText(basicInfo?.firstName + " " + basicInfo?.lastName)
        editTitle.setText(basicInfo?.title)
        editSummary.setText(basicInfo?.summary)
        runOnUiThread(Runnable {
            Picasso.with(this).load(basicInfo?.pictureUrl).into(image)
        })
    }

    fun createPDF() {
        val page = object : AbstractViewRenderer(this, R.layout.activity_cv_viewer) {

            override fun initView(view: View) {
                loadDataInView(view)
            }
        }

        // you can reuse the bitmap if you want
        page.isReuseBitmap = true


        PdfDocument.Builder(this).addPage(page).filename("test").orientation(PdfDocument.A4_MODE.PORTRAIT)
                .progressMessage(R.string.gen_pdf_file).progressTitle(R.string.gen_please_wait).renderWidth(2480).renderHeight(3508)
                .listener(object : PdfDocument.Callback {
                    override fun onComplete(file: File) {
                        Log.i(PdfDocument.TAG_PDF_MY_XML, "Complete")

                        val output = Environment.getExternalStorageDirectory().toString() + File.separator + "Documents" + File.separator + "MY_PDF.pdf"

                        try {
                            FileUtils.copy(file, File(output))
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }

                    override fun onError(e: Exception) {
                        Log.i(PdfDocument.TAG_PDF_MY_XML, "Error")
                    }
                }).create().createPdf(this)
    }

    fun getData()
    {
        try {
            threadExecutor.execute {
                Log.i(TAG, "Fetching Data..");
                val db: AppDatabase = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "user-database").build()

                val list: List<BasicInfo> = db.userDao().getAll()
                if (list.isNotEmpty()) {
                    basicInfo = list[0]
                    loadData()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
