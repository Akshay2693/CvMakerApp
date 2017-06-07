package com.blackbox.onepage.cvmaker.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.arch.persistence.room.Room
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.badoualy.stepperindicator.StepperIndicator
import com.blackbox.onepage.cvmaker.R
import com.blackbox.onepage.cvmaker.db.AppDatabase
import com.blackbox.onepage.cvmaker.models.BasicInfo
import com.blackbox.onepage.cvmaker.rxBus.RxBus
import com.blackbox.onepage.cvmaker.ui.adapter.PagerAdapter
import com.blackbox.onepage.cvmaker.utils.Constants
import com.blackbox.onepage.cvmaker.utils.Preference
import com.linkedin.platform.APIHelper
import com.linkedin.platform.LISessionManager
import com.linkedin.platform.errors.LIApiError
import com.linkedin.platform.errors.LIAuthError
import com.linkedin.platform.listeners.ApiListener
import com.linkedin.platform.listeners.ApiResponse
import com.linkedin.platform.listeners.AuthListener
import com.linkedin.platform.utils.Scope
import com.thebluealliance.spectrum.SpectrumDialog
import org.json.JSONObject
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class CVCreaterActivity : AppCompatActivity() {

    val TAG: String = "CVCreaterActivity"


    val threadExecutor: Executor = Executors.newFixedThreadPool(5)

    var url: String = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,headline,picture-url,industry,summary,specialties,positions:(id,title,summary,start-date,end-date,is-current,company:(id,name,type,size,industry,ticker)),educations:(id,school-name,field-of-study,start-date,end-date,degree,activities,notes),associations,interests,num-recommenders,date-of-birth,publications:(id,title,publisher:(name),authors:(id,name),date,url,summary),patents:(id,title,summary,number,status:(id,name),office:(name),inventors:(id,name),date,url),languages:(id,language:(name),proficiency:(level,name)),skills:(id,skill:(name)),certifications:(id,name,authority:(name),number,start-date,end-date),courses:(id,name,number),recommendations-received:(id,recommendation-type,recommendation-text,recommender),honors-awards,three-current-positions,three-past-positions,volunteer)?format=json"

    private var progressDialog: ProgressDialog? = null
    private var message: String? = null

    var pager: ViewPager? = null

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

    private var _rxBus: RxBus? = null

    val rxBusSingleton: RxBus
        get() {
            val res = _rxBus ?: RxBus()
            if (_rxBus == null) _rxBus = res
            return res;
        }

    private var _db: AppDatabase? = null

    val dbSingleton: AppDatabase
        get() {
            val res = _db ?: Room.databaseBuilder(applicationContext, AppDatabase::class.java, "user-database").build()
            if (_db == null) _db = res
            return res;
        }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cv_creater)

        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 2121)
        }

        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 2122)
        }

        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
            message = getString(R.string.message_loading)
        }

        if (!Preference.getBoolean(this, Constants.SP_DATA_DOWNLOADED, false)) {

            show()

            val thisActivity = this
            LISessionManager.getInstance(applicationContext).init(thisActivity, buildScope(), object : AuthListener {
                override fun onAuthSuccess() {
                    getProfileInfo()
                }

                override fun onAuthError(error: LIAuthError) {
                    Log.e(TAG, "Error: " + error.toString());
                    dismiss()
                }
            }, true)
        } else {
            pager = findViewById(R.id.pager) as ViewPager
            val indicator = findViewById(R.id.stepper_indicator) as StepperIndicator
            pager!!.adapter = PagerAdapter(supportFragmentManager)

            indicator.setViewPager(pager, true)

            indicator.addOnStepClickListener(StepperIndicator.OnStepClickListener {
                fun OnStepClicked(step: Int) {
                    pager!!.setCurrentItem(step, true);
                }
            })
        }

        val background = findViewById(R.id.main_content) as CoordinatorLayout
        val fab = findViewById(R.id.fab) as FloatingActionButton
        val fabView = findViewById(R.id.fab_view) as FloatingActionButton

        fab.setOnClickListener { view ->
            pickColor(background)
        }

        fabView.setOnClickListener { view ->
            val intent = Intent(this, CVViewerActivity::class.java)
            startActivity(intent)
        }

    }

    fun saveData(basicInfo: BasicInfo) {
        try {
            threadExecutor.execute {
                Log.i(TAG, "Saving Data..");
                AppDatabase.getInstance(applicationContext)?.userDao()?.save(basicInfo)
                Preference.save(this, Constants.SP_DATA_DOWNLOADED, true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun pickColor(view: View) {
        SpectrumDialog.Builder(this)
                .setColors(R.array.demo_colors)
                .setSelectedColorRes(R.color.md_blue_100)
                .setDismissOnColorSelected(false)
                .setOutlineWidth(2)
                .setOnColorSelectedListener(SpectrumDialog.OnColorSelectedListener { positiveResult, color ->
                    if (positiveResult) {
                        view.setBackgroundColor(color)
                    }
                }).build().show(supportFragmentManager, "dialog")
    }

    fun buildScope(): Scope {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        LISessionManager.getInstance(applicationContext).onActivityResult(this, requestCode, resultCode, data)
    }


    fun getProfileInfo() {

        val apiHelper = APIHelper.getInstance(applicationContext)
        apiHelper.getRequest(this, url, object : ApiListener {
            override fun onApiSuccess(result: ApiResponse) {

                Log.i(TAG, "Result: " + result)

                try {
                    showResult(result.responseDataAsJson)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onApiError(error: LIApiError) {
                Log.i(TAG, "Error: " + error)
            }
        })
    }

    fun showResult(response: JSONObject) {

        try {
            val id = response.getString("id")
            val fName = response.getString("firstName")
            val lName = response.getString("lastName")
            val title = response.getString("headline")
            val summary = response.getString("summary")
            val industry = response.getString("industry")
            val imageURL = response.getString("pictureUrl")

            val basicInfo = BasicInfo(id, fName, lName, title, summary, industry, imageURL)
            saveData(basicInfo)

            dismiss()

            pager = findViewById(R.id.pager) as ViewPager
            val indicator = findViewById(R.id.stepper_indicator) as StepperIndicator
            pager!!.adapter = PagerAdapter(supportFragmentManager)

            indicator.setViewPager(pager, true)

            indicator.addOnStepClickListener(StepperIndicator.OnStepClickListener {
                fun OnStepClicked(step: Int) {
                    pager!!.setCurrentItem(step, true);
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0])
            //resume tasks needing this permission
        }
    }
}
