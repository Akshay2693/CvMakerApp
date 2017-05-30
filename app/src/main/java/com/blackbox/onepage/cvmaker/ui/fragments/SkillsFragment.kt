package com.blackbox.onepage.cvmaker.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blackbox.onepage.cvmaker.R
import com.blackbox.onepage.cvmaker.models.BasicInfo


class SkillsFragment : Fragment()  {


    private var basicInfo: BasicInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            basicInfo = arguments.getParcelable(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_skills, container, false)
        parseInfo(view)
        return view
    }

    companion object {

        private val ARG_PARAM1 = "info"

        fun newInstance(param1: BasicInfo): SkillsFragment {
            val fragment = SkillsFragment()
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
}
