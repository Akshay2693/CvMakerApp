package com.blackbox.onepage.cvmaker.ui.adapter

/**
 * Created by umair on 07/06/2017.
 */
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blackbox.onepage.cvmaker.R
import com.blackbox.onepage.cvmaker.models.EducationInfo
import kotlinx.android.synthetic.main.item_education.view.*

class EducationListAdapter(val list: List<EducationInfo>, val itemClick: (EducationInfo) -> Unit) :
        RecyclerView.Adapter<EducationListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_education, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindInfo(list[position])
    }

    override fun getItemCount() = list.size

    class ViewHolder(view: View, val itemClick: (EducationInfo) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bindInfo(info: EducationInfo) {

            with(info) {
                itemView.txt_institute.text = info.institute
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}