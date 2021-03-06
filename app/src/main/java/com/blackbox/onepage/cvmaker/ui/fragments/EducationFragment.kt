package com.blackbox.onepage.cvmaker.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.blackbox.onepage.cvmaker.R
import com.blackbox.onepage.cvmaker.db.AppDatabase
import com.blackbox.onepage.cvmaker.models.BasicInfo
import com.blackbox.onepage.cvmaker.models.EducationInfo
import com.blackbox.onepage.cvmaker.rxBus.RxBus
import com.blackbox.onepage.cvmaker.ui.activities.CVCreaterActivity
import com.blackbox.onepage.cvmaker.ui.adapter.EducationListAdapter
import com.blackbox.onepage.cvmaker.ui.dialogs.EducationDialog
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class EducationFragment : Fragment() {

    val TAG: String = EducationFragment::class.java.name

    private lateinit var _rxBus: RxBus
    private lateinit var _subscriptions: CompositeSubscription

    private var newsList: RecyclerView? = null

    val threadExecutor: Executor = Executors.newFixedThreadPool(5)


    private var adapter: EducationListAdapter? = null
    private var basicInfo: BasicInfo? = null
    private var infoList: List<EducationInfo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            basicInfo = arguments.getParcelable(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_education, container, false)
        parseInfo(view)

        val addButton = view.findViewById(R.id.btn_add) as LinearLayout

        addButton.setOnClickListener { view ->
            EducationDialog.show(activity)
        }

        newsList = view.findViewById(R.id.list_education) as RecyclerView?
        newsList?.setHasFixedSize(true)
        newsList?.layoutManager = LinearLayoutManager(context)

        fetchData()

        return view
    }

    companion object {

        private val ARG_PARAM1 = "info"

        fun newInstance(param1: BasicInfo): EducationFragment {
            val fragment = EducationFragment()
            val args = Bundle()
            args.putParcelable(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }

    fun parseInfo(view: View) {

        try {

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        _rxBus = (activity as CVCreaterActivity).rxBusSingleton
    }

    override fun onStart() {
        super.onStart()
        _subscriptions = CompositeSubscription()

        _subscriptions.add(
                _rxBus.toObserverable()
                        .subscribe { event ->
                            val info: EducationInfo = event as EducationInfo
                            Log.i(TAG, "Info: " + info.institute)
                            saveData(info)

                            threadExecutor.execute {
                                Log.i(TAG, "Fetching Data..");

                                val list: List<EducationInfo> = AppDatabase.getInstance(activity)?.educationDao()?.getAll()!!

                                for (item in list) {
                                    Log.i(TAG, "Institute: " + item.institute);
                                }

                                if (list.isNotEmpty()) {
                                    infoList = list
                                }
                            }
                            adapter?.notifyDataSetChanged()
                        })
    }

    fun saveData(basicInfo: EducationInfo) {
        try {
            threadExecutor.execute {
                Log.i(TAG, "Saving Data..");
                AppDatabase.getInstance(activity)?.educationDao()?.save(basicInfo)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun fetchData() {
        try {
            threadExecutor.execute {
                Log.i(TAG, "Fetching Data..");

                val list: List<EducationInfo> = AppDatabase.getInstance(activity)?.educationDao()?.getAll()!!

                for (item in list) {
                    Log.i(TAG, "Institute: " + item.institute);
                }

                if (list.isNotEmpty()) {
                    infoList = list
                    adapter = EducationListAdapter(infoList!!) {

                    }
                    newsList?.adapter = adapter

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        _subscriptions.unsubscribe()
    }
}
