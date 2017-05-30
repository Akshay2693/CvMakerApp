package com.blackbox.onepage.cvmaker.ui.fragments

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatImageView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blackbox.onepage.cvmaker.R
import com.blackbox.onepage.cvmaker.db.AppDatabase
import com.blackbox.onepage.cvmaker.models.BasicInfo
import com.squareup.picasso.Picasso
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class BasicFragment : Fragment() {

    val TAG: String = "BasicFragment"

    val threadExecutor: Executor = Executors.newFixedThreadPool(5)

    private var basicInfo: BasicInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            basicInfo = arguments.getParcelable(ARG_PARAM1)
        }

        try {
            threadExecutor.execute {
                Log.i(TAG, "Fetching Data..");
                val db: AppDatabase = Room.databaseBuilder(activity, AppDatabase::class.java, "user-database").build()

                val list: List<BasicInfo> = db.userDao().getAll()
                if (list.isNotEmpty()) {
                    basicInfo = list[0]
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_basic, container, false)
        parseInfo(view)
        return view
    }

    companion object {

        private val ARG_PARAM1 = "info"

        fun newInstance(param1: BasicInfo): BasicFragment {
            val fragment = BasicFragment()
            val args = Bundle()
            args.putParcelable(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }

    fun parseInfo(view: View) {

        try {
            val editName = view.findViewById(R.id.txt_user_name) as AppCompatEditText
            val editTitle = view.findViewById(R.id.txt_user_title) as AppCompatEditText
            val editSummary = view.findViewById(R.id.txt_user_summary) as AppCompatEditText
            val image = view.findViewById(R.id.img_user) as AppCompatImageView

            editName.setText(basicInfo?.firstName + " " + basicInfo?.lastName)
            editTitle.setText(basicInfo?.title)
            editSummary.setText(basicInfo?.summary)
            Picasso.with(activity).load(basicInfo?.pictureUrl).into(image)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
